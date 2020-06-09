package com.fjx.mg.moments.all;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.fjx.mg.friend.imuser.NewImUserDetailActivity;
import com.fjx.mg.main.yellowpage.YellowPageAdapter;
import com.fjx.mg.main.yellowpage.detail.YellowPageDetailActivity;
import com.fjx.mg.main.yellowpage.publish.YellowPagePublicActivity;
import com.fjx.mg.nearbycity.NearbyCityDetailActivity;
import com.fjx.mg.nearbycity.PublisherNearbyCityActivity;
import com.fjx.mg.nearbycity.adapter.NearbyCityAdapter;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.widget.recyclerview.ItemDecoration;
import com.library.common.base.BaseMvpActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.data.UserCenter;
import com.library.repository.models.CompanyListModel;
import com.library.repository.models.NearbyCItyGetListModel;
import com.library.repository.models.NearbyCityConfigModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.TotalCityCircleListModel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.TimConfig;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author    by hanlz
 * Date      on 2020/3/9.
 * Description：
 */
public class AllCityCircleAndYellowPageActivity extends BaseMvpActivity<AllCityCircleAndYellowPagePresenter> implements AllCityCircleAndYellowPageContract.View {

    @BindView(R.id.rvCommon)
    RecyclerView mRvCommon;
    @BindView(R.id.refreshView)
    CustomRefreshView mRefreshView;
    @BindView(R.id.ivPublisher)
    ImageView mIvPublisher;
    private String mType, mUId;
    private NearbyCityAdapter mCityCircleAdapter;
    private YellowPageAdapter mYellowPageAdapter;

    /**
     * @param context
     * @param type    1----马岛服务 2----黄页
     * @return
     */
    public static Intent newIntent(Context context, String type, String uid, boolean isShowPulisher) {
        Intent intent = new Intent(context, AllCityCircleAndYellowPageActivity.class);
        intent.putExtra(IntentConstants.TYPE, type);
        intent.putExtra(IntentConstants.UID, uid);
        intent.putExtra(IntentConstants.IS_MY_PUBLISH, isShowPulisher);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_all_city_circle_and_yellow_page;
    }

    @Override
    protected void initView() {
        super.initView();
        ToolBarManager manager = ToolBarManager.with(this);
        if (getIntent() == null) {
            return;
        }
        if (getIntent().getBooleanExtra(IntentConstants.IS_MY_PUBLISH, false)) {
            mIvPublisher.setVisibility(View.VISIBLE);
        } else {
            mIvPublisher.setVisibility(View.GONE);
        }
        mCityCircleAdapter = new NearbyCityAdapter();
        mYellowPageAdapter = new YellowPageAdapter();
        mRvCommon.addItemDecoration(new ItemDecoration(this, ItemDecoration.VERTICAL_LIST));
        mType = getIntent().getStringExtra(IntentConstants.TYPE);
        mUId = getIntent().getStringExtra(IntentConstants.UID);
        if (TextUtils.equals("1", mType)) {
            manager.setTitle(getString(R.string.my_nearby_city));
            mRvCommon.setAdapter(mCityCircleAdapter);
            mCityCircleAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    TotalCityCircleListModel model = mCityCircleAdapter.getItem(position);
                    startActivity(NearbyCityDetailActivity.newIntent(view.getContext(), model.getcId()));
                }
            });
            mCityCircleAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    TotalCityCircleListModel item = (TotalCityCircleListModel) adapter.getItem(position);
                    if (view.getId() == R.id.tvLike) {
                        TextView tvLike = (TextView) view;
                        String num = tvLike.getText().toString();
                        if (item.isLike()) {
                            item.setLike(false);
                            ViewUtil.setDrawableLeft(tvLike, R.drawable.like_gray);
                            tvLike.setText(StringUtil.subtract(num, "1", 0L));
                            mPresenter.requestCancelPraise("", "", item.getcId());
                        } else {
                            item.setLike(true);
                            ViewUtil.setDrawableLeft(tvLike, R.drawable.like_red);
                            tvLike.setText(StringUtil.add(num, "1"));
                            mPresenter.requestPraise("", "", item.getcId());
                        }
                    }else if (view.getId()==R.id.ivHeaderPic){
                        String uid = "";
                        if (TimConfig.isRelease) {
                            uid = "MGM".concat(item.getuId());
                        } else {
                            uid = "fjx".concat(item.getuId());
                        }
                        TIMFriend friend = UserCenter.getFriend(uid);
                        if (friend==null){
                            startActivity(AddFriendActivity.newInstance(getCurContext(),uid));
                        }else {
                            startActivity(NewImUserDetailActivity.newInstance(getCurContext(),uid));
                        }
                    }
                }
            });
        } else {
            manager.setTitle(getString(R.string.yellow_page));
            mRvCommon.setAdapter(mYellowPageAdapter);
            mYellowPageAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    startActivity(YellowPageDetailActivity.newInstance(getCurContext(), mYellowPageAdapter.getItem(position).getCId(), "", true, mYellowPageAdapter.getItem(position).getUId()));
                }
            });
        }
        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (TextUtils.equals("1", mType)) {
                    mPresenter.requestCityList("1", "", "", mUId);
                } else {
                    mPresenter.requestCompanyList("1", mUId);
                }
            }
        });
        mRefreshView.autoRefresh();
    }

    @Override
    public void responseFailed(ResponseModel data) {
        mRefreshView.finishLoading();
    }

    @Override
    public void responseCityList(NearbyCItyGetListModel data) {
        mRefreshView.noticeAdapterData(mCityCircleAdapter, data.getCityCircleList(), data.isHasNext());
    }

    @Override
    public void responseCompanyList(CompanyListModel data) {
        mRefreshView.noticeAdapterData(mYellowPageAdapter, data.getCompanyList(), data.isHasNext());
    }


    @Override
    protected AllCityCircleAndYellowPagePresenter createPresenter() {
        return new AllCityCircleAndYellowPagePresenter(this);
    }

    @OnClick({R.id.ivPublisher})
    public void onViewClick(View view) {
//        if (UserCenter.needLogin()) return;
        if (UserCenter.hasLogin()){
            if (TextUtils.equals("1", mType)) {
                startActivity(PublisherNearbyCityActivity.newIntent(getCurContext(), new NearbyCityConfigModel()));
            } else {
                startActivityForResult(YellowPagePublicActivity.newInstance(getCurContext()), 11);
            }
        }else {
            new DialogUtil().showAlertDialog(this, R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    UserCenter.goLoginActivity();
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) mRefreshView.doRefresh();
    }
}
