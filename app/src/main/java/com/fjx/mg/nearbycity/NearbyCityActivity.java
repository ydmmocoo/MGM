package com.fjx.mg.nearbycity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.nearbycity.adapter.NearbyCityMultiTypeAdapter;
import com.fjx.mg.nearbycity.event.PraiseEvent;
import com.fjx.mg.nearbycity.mvp.NearbyCityContract;
import com.fjx.mg.nearbycity.mvp.NearbyCityPresenter;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.widget.recyclerview.ItemDecoration;
import com.library.common.base.BaseMvpActivity;
import com.library.repository.data.UserCenter;
import com.library.repository.db.base.DBHelper;
import com.library.repository.db.model.DaoSession;
import com.library.repository.models.NearbyCItyGetListModel;
import com.library.repository.models.NearbyCityConfigModel;
import com.library.repository.models.NearbyCityHotCompanyModel;
import com.library.repository.models.ResponseModel;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author hanlz
 * @date 2019-10-16 17:04:35
 * description:同城首页
 */
public class NearbyCityActivity extends BaseMvpActivity<NearbyCityPresenter> implements NearbyCityContract.View {

    @BindView(R.id.ivPublisher)
    ImageView mIvPublisher;
    @BindView(R.id.rvTopType)
    RecyclerView mRvTopType;
    @BindView(R.id.refreshView)
    SmartRefreshLayout mRefreshView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private int mPage = 1;
    private NearbyCityMultiTypeAdapter mAdapter;
    private NearbyCityConfigModel mNearbyCityConfigModel;
    private NearbyCityHotCompanyModel mNearbyCityHotCompanyModel;
    private NearbyCItyGetListModel mNearbyCItyGetListModel;

    public static Intent newIntent(Context context, String json) {
        Intent intent = new Intent(context, NearbyCityActivity.class);
        intent.putExtra("json", json);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_nearby_city;
    }

    @Override
    protected NearbyCityPresenter createPresenter() {
        return new NearbyCityPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        mIvPublisher.setEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.ic_back_black);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                setResult(-1);
            }
        });
        EventBus.getDefault().register(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvTopType.setLayoutManager(layoutManager);
        mRvTopType.addItemDecoration(new ItemDecoration(this, ItemDecoration.VERTICAL_LIST));
        //有缓存先显示缓存
        setData();
        mRefreshView.autoRefresh();
        mRefreshView.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPage++;
                mPresenter.requestConfig();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage=1;
                mPresenter.requestConfig();
            }
        });
    }

    private void setData() {
        DaoSession daoSession = DBHelper.getInstance().getDaoSession();
        List<NearbyCityConfigModel> nearbyCityConfigModels = daoSession.getNearbyCityConfigModelDao().loadAll();
        List<NearbyCityHotCompanyModel> nearbyCityHotCompanyModels = daoSession.getNearbyCityHotCompanyModelDao().loadAll();
        List<NearbyCItyGetListModel> nearbyCItyGetListModels = daoSession.getNearbyCItyGetListModelDao().loadAll();
        if (nearbyCityConfigModels != null && nearbyCityConfigModels.size() > 0) {
            if (nearbyCityHotCompanyModels != null && nearbyCityHotCompanyModels.size() > 0) {
                if (nearbyCItyGetListModels != null && nearbyCItyGetListModels.size() > 0) {
                    mAdapter = new NearbyCityMultiTypeAdapter(nearbyCityConfigModels.get(0), nearbyCityHotCompanyModels.get(0), nearbyCItyGetListModels.get(0), getIntent().getStringExtra("json"));
                    mRvTopType.setAdapter(mAdapter);
                }
            }
        }
    }

    @OnClick({R.id.ivPublisher, R.id.cvSearch})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.ivPublisher://发布同城信息
                if (UserCenter.hasLogin()) {
                    startActivity(PublisherNearbyCityActivity.newIntent(getCurContext(), mNearbyCityConfigModel));
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
            case R.id.cvSearch://搜索
                startActivity(NearbyCitySearchActivity.newIntent(getCurContext()));
                break;
            default:
                break;
        }
    }

    @Override
    public void responseFailed(ResponseModel model) {
        if (mPage==1) {
            mRefreshView.finishRefresh();
        }else {
            mRefreshView.finishLoadMore();
        }
    }

    @Override
    public void responseConfigDatas(NearbyCityConfigModel model) {
        mNearbyCityConfigModel = model;
        mIvPublisher.setEnabled(true);
        mPresenter.requestHotCompany();
    }

    @Override
    public void responseHotCompanyDatas(NearbyCityHotCompanyModel model) {
        mNearbyCityHotCompanyModel = model;
        mPresenter.requestNearbyCityList(mPage + "", "", "");
    }

    @Override
    public void responseNearbyCityList(NearbyCItyGetListModel model) {
        if (model.isHasNext()) {
            mRefreshView.setEnableLoadMore(true);
        }else {
            mRefreshView.setEnableLoadMore(false);
        }
        String json = getIntent().getStringExtra("json");
        if (mPage==1){
            mNearbyCItyGetListModel = model;
            mRefreshView.finishRefresh();
        }else {
            mNearbyCItyGetListModel.getCityCircleList().addAll(model.getCityCircleList());
            mRefreshView.finishLoadMore();
        }
        if (!mNearbyCItyGetListModel.getCityCircleList().isEmpty()) {
            mAdapter = new NearbyCityMultiTypeAdapter(mNearbyCityConfigModel, mNearbyCityHotCompanyModel, mNearbyCItyGetListModel, json);
            mRvTopType.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new NearbyCityMultiTypeAdapter(mNearbyCityConfigModel, mNearbyCityHotCompanyModel, mNearbyCItyGetListModel, json);
            mRvTopType.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mRefreshView.finishLoadMoreWithNoMoreData();
        }
    }


    @Override
    public void responsePraise() {
        mPresenter.requestNearbyCityList(mPage + "", "", "");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPraiseEvent(PraiseEvent event) {
        mPresenter.requestPraise(event.getCommentId(), event.getReplyId(), event.getcId());
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(-1);
        super.onBackPressed();
    }
}
