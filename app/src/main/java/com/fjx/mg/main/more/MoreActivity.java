package com.fjx.mg.main.more;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.house.HouseHomeActivity;
import com.fjx.mg.job.JobHomeActivity;
import com.fjx.mg.main.more.search.SearchAppActivity;
import com.fjx.mg.main.payment.PayMentActivity;
import com.fjx.mg.main.rate.RateActivity;
import com.fjx.mg.main.translate.TranslateActivity;
import com.fjx.mg.main.yellowpage.YellowPageActivityV1;
import com.fjx.mg.recharge.center.RechargeCenterActivityx;
import com.fjx.mg.setting.feedback.FeedBackActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.StatusBarManager;
import com.library.common.utils.ViewUtil;
import com.library.repository.models.AppModel;
import com.library.repository.models.RecAppListModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MoreActivity extends BaseMvpActivity<MorePresenter> implements MoreContract.View {

    @BindView(R.id.tvRecentlyUsed)
    TextView tvRecentlyUsed;

    @BindView(R.id.tvAllApp)
    TextView tvAllApp;

    @BindView(R.id.listRecentlyUsed)
    RecyclerView listRecentlyUsed;

    @BindView(R.id.listAllApp)
    RecyclerView listAllApp;

    @BindView(R.id.tvLocation)
    TextView tvLocation;

    @BindView(R.id.ivRightIcon)
    ImageView ivRightIcon;
    @BindView(R.id.tvSearch)
    TextView tvSearch;

    private ArrayList<AppModel> AppAllList = new ArrayList<>();
    private RecentlyUsedAdapter recentlyUsedAdapter;
    private AllAppAdapter allappAdapter;

    public static Intent newInstance(Context context) {
        return new Intent(context, MoreActivity.class);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_more;
    }

    @Override
    protected MorePresenter createPresenter() {
        return new MorePresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        SetToolBar();
        SetAllData(AppAllList);
        ShowUsed();
        ShowAll();
    }


    private void ShowAll() {
        allappAdapter = new AllAppAdapter(this);
        listAllApp.setLayoutManager(new GridLayoutManager(getCurContext(), 4));
        listAllApp.addItemDecoration(new SpacesItemDecoration(0, 0));
        listAllApp.setAdapter(allappAdapter);
        allappAdapter.setList(AppAllList);
        allappAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                mPresenter.recUseApp("" + (position + 1));
            }
        });
    }

    private void ShowUsed() {
        recentlyUsedAdapter = new RecentlyUsedAdapter(this);
        listRecentlyUsed.setLayoutManager(new GridLayoutManager(getCurContext(), 4));
        listRecentlyUsed.addItemDecoration(new SpacesItemDecoration(0, 0));
        listRecentlyUsed.setAdapter(recentlyUsedAdapter);
        recentlyUsedAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                List<RecAppListModel.AccessListBean> data = recentlyUsedAdapter.getData();
                String appId = data.get(position).getAppId();
                mPresenter.recUseApp(appId);
            }
        });
    }

    private void SetToolBar() {
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        ViewUtil.setDrawableLeft(tvLocation, R.drawable.iv_back);
        ivRightIcon.setVisibility(View.GONE);
        tvLocation.setVisibility(View.VISIBLE);
        tvLocation.setText("");
        tvSearch.setText(getResources().getString(R.string.all_application));
    }


    private void SetAllData(ArrayList<AppModel> appUsedList) {
        appUsedList.add(new AppModel(getResources().getString(R.string.recharge_center), R.drawable.ic_home_cz));
        appUsedList.add(new AppModel(getResources().getString(R.string.house_rent), R.drawable.ic_home_zs));
        appUsedList.add(new AppModel(getResources().getString(R.string.employment), R.drawable.ic_home_zp));
        appUsedList.add(new AppModel(getResources().getString(R.string.yellow_page), R.drawable.ic_home_hy));
        appUsedList.add(new AppModel(getResources().getString(R.string.exchange_rate_inquiry), R.drawable.ic_home_search));
        appUsedList.add(new AppModel(getResources().getString(R.string.translate_online), R.drawable.ic_home_fy));
        appUsedList.add(new AppModel(getResources().getString(R.string.feedback), R.drawable.home_feedback));
        appUsedList.add(new AppModel(getResources().getString(R.string.payment_problem), R.drawable.payment_problem));
    }

    private void ItemClick(String id) {
        switch (id) {
            case "1"://充值中心
                startActivity(RechargeCenterActivityx.newInstance(getCurContext()));
                break;
            case "2"://房屋租售
                startActivity(HouseHomeActivity.newInstance(getCurContext()));
                break;
            case "3"://求职招聘
                startActivity(JobHomeActivity.newInstance(getCurContext()));
                break;
            case "4"://企业黄页
                startActivity(YellowPageActivityV1.newInstance(getCurContext()));
                break;
            case "5"://汇率查询
                startActivity(RateActivity.newInstance(getCurActivity()));
                break;
            case "6"://在线翻译
                startActivity(TranslateActivity.newInstance(getCurActivity()));
                break;
            case "7"://意见反馈
                startActivity(FeedBackActivity.newInstance(getCurContext()));
                break;
            case "8"://有偿问答
                startActivity(PayMentActivity.newInstance(getCurContext()));
                break;
            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getRecAppList();
    }

    @OnClick({R.id.tvLocation, R.id.tvSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvLocation://退出
                finish();
                break;
            case R.id.tvSearch://搜索
                startActivity(SearchAppActivity.newInstance(getCurContext()));
                break;
        }
    }

    @Override
    public void showUsed(RecAppListModel s) {
        if (s.getAccessList() == null && s.getAccessList().size() == 0) {
            tvRecentlyUsed.setVisibility(View.GONE);
        } else {
            tvRecentlyUsed.setVisibility(View.VISIBLE);
        }
        recentlyUsedAdapter.setList(s.getAccessList());
    }

    @Override
    public void showUsed(String s) {
        ItemClick(s);
    }
}
