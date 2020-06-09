package com.fjx.mg.nearbycity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.nearbycity.adapter.TopTypeDetailAdapter;
import com.fjx.mg.nearbycity.mvp.TopTypeDetailContract;
import com.fjx.mg.nearbycity.mvp.TopTypeDetailPresenter;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.widget.recyclerview.ItemDecoration;
import com.library.common.base.BaseMvpActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.data.UserCenter;
import com.library.repository.models.NearbyCItyGetListModel;
import com.library.repository.models.NearbyCityConfigModel;
import com.library.repository.models.NearbyCityTypeListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.TotalCityCircleListModel;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Author    by hanlz
 * Date      on 2019/10/18.
 * Description：顶部分类列表
 */
public class TopTypeDetailActivity extends BaseMvpActivity<TopTypeDetailPresenter> implements TopTypeDetailContract.View {

    private View mHeaderView;
    private NearbyCityTypeListModel mTypeList;
    private NearbyCityConfigModel mConfigModel;
    private int mPage = 1;

    @BindView(R.id.rvTopType)
    RecyclerView mRvTopType;
    private TopTypeDetailAdapter mAdapter;

    @BindView(R.id.refreshView)
    CustomRefreshView mRefreshView;


    public static Intent newIntent(Context context, NearbyCityTypeListModel model, NearbyCityConfigModel configModel) {
        Intent intent = new Intent(context, TopTypeDetailActivity.class);
        intent.putExtra(IntentConstants.TYPE_LIST, model);
        intent.putExtra(IntentConstants.CONFIG, configModel);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.act_top_type_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        final Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mTypeList = intent.getParcelableExtra(IntentConstants.TYPE_LIST);
        mConfigModel = intent.getParcelableExtra(IntentConstants.CONFIG);

        mRefreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPage = page;
                mPresenter.requestNearbyCityList(page + "", "", mTypeList.getcId());
            }
        });
        mRefreshView.autoRefresh();
        mAdapter = new TopTypeDetailAdapter();
        mRvTopType.addItemDecoration(new ItemDecoration(this, ItemDecoration.VERTICAL_LIST));
        mRvTopType.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                TotalCityCircleListModel item = (TotalCityCircleListModel) adapter.getItem(position);
                startActivity(NearbyCityDetailActivity.newIntent(getCurContext(), item.getcId()));
            }
        });
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
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
                }
            }
        });
        initHeaderView();
        mAdapter.addHeaderView(mHeaderView);
    }

    /**
     * 分类顶部
     */
    public void initHeaderView() {
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.item_top_type_header_layout, null);
        if (mHeaderView.getParent() != null) {
            ViewGroup parent = (ViewGroup) mHeaderView.getParent();
            parent.removeView(mHeaderView);
        }
        TextView tvTypeName = mHeaderView.findViewById(R.id.tvTopTag);
        CircleImageView imageView = mHeaderView.findViewById(R.id.ivTopIcon);
        if (mTypeList != null) {
            String typeName = mTypeList.getTypeName();
            if (StringUtil.isNotEmpty(typeName)) {
                tvTypeName.setText(typeName);
            }
            String img = mTypeList.getImg();
            if (StringUtil.isNotEmpty(img)) {
                CommonImageLoader.load(img).placeholder(R.drawable.food_default).into(imageView);
            }

        }
    }

    @OnClick({R.id.ivBack, R.id.tvSearch, R.id.ivPublisher})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack://返回
                finish();
                break;
            case R.id.tvSearch://搜索
                startActivity(NearbyCitySearchActivity.newIntent(getCurContext()));
                break;
            case R.id.ivPublisher://发布
                if (UserCenter.hasLogin()){
                    startActivity(PublisherNearbyCityActivity.newIntent(getCurContext(), mConfigModel));
                }else {
                    new DialogUtil().showAlertDialog(this, R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserCenter.goLoginActivity();
                        }
                    });
                }
                break;
            default:
        }
    }

    @Override
    protected TopTypeDetailPresenter createPresenter() {
        return new TopTypeDetailPresenter(this);
    }


    @Override
    public void responseNearbyCityList(NearbyCItyGetListModel model) {
        mRefreshView.finishLoading();
        if (mPage == 1) {//直接替换数据
            mAdapter.replaceData(model.getCityCircleList());
        } else {//刷新数据
            mRefreshView.noticeAdapterData(mAdapter, model.getCityCircleList(), model.isHasNext());//考虑实际，不显示无更多数据，hasNext=true
            mRvTopType.invalidateItemDecorations();
        }
    }

    @Override
    public void responsePraise() {
//        mPresenter.requestNearbyCityList("1", "", mTypeList.getcId());
//        CommonToast.toast(getString(R.string.praise_success));
    }

    @Override
    public void responseCancelPraise() {
//        mPresenter.requestNearbyCityList("1", "", mTypeList.getcId());
//        CommonToast.toast(getString(R.string.cancelPraise));
    }


    @Override
    public void responseFailed(ResponseModel model) {
        mRefreshView.finishLoading();
    }
}
