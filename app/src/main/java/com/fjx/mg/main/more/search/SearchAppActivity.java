package com.fjx.mg.main.more.search;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
import com.fjx.mg.main.payment.PayMentActivity;
import com.fjx.mg.main.payment.PayMentContract;
import com.fjx.mg.main.rate.RateActivity;
import com.fjx.mg.main.translate.TranslateActivity;
import com.fjx.mg.main.yellowpage.YellowPageActivityV1;
import com.fjx.mg.recharge.center.RechargeCenterActivityx;
import com.fjx.mg.setting.feedback.FeedBackActivity;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonToast;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StatusBarManager;
import com.library.repository.models.AppModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchAppActivity extends BaseMvpActivity<SearchAppPresenter> implements SearchAppContract.View {

    @BindView(R.id.listAllApp)
    RecyclerView listAllApp;

    @BindView(R.id.etSearch)
    EditText etSearch;

    @BindView(R.id.ivClear)
    ImageView ivClear;

    @BindView(R.id.tvSearch)
    TextView tvSearch;

    private ArrayList<AppModel> statusLs = new ArrayList<>();
    private ArrayList<AppModel> AppAllList = new ArrayList<>();
    private ArrayList<AppModel> aftersearch = new ArrayList<>();
    private SearchAppAdapter allappAdapter;

    public static Intent newInstance(Context context) {
        return new Intent(context, SearchAppActivity.class);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_search_app;
    }

    @Override
    protected SearchAppPresenter createPresenter() {
        return new SearchAppPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        SetToolBar();
        SetAllData(AppAllList);
        ShowAll();
    }

    private void ShowAll() {
        allappAdapter = new SearchAppAdapter(this);
        listAllApp.setLayoutManager(new GridLayoutManager(getCurContext(), 1));
        listAllApp.addItemDecoration(new SpacesItemDecoration(0, 0));
        listAllApp.setAdapter(allappAdapter);
        allappAdapter.setList(aftersearch);
        allappAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                ItemClick(allappAdapter.getItem(position).getId());
            }
        });
    }


    private void SetToolBar() {
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        mPresenter.bindTextWatcher(etSearch);
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

    private void ItemClick(int id) {
        switch (id) {
            case R.drawable.ic_home_cz://充值中心
                mPresenter.recUseApp("1");
                startActivity(RechargeCenterActivityx.newInstance(getCurContext()));
                break;
            case R.drawable.ic_home_zs://房屋租售
                mPresenter.recUseApp("2");
                startActivity(HouseHomeActivity.newInstance(getCurContext()));
                break;
            case R.drawable.ic_home_zp://求职招聘
                mPresenter.recUseApp("3");
                startActivity(JobHomeActivity.newInstance(getCurContext()));
                break;
            case R.drawable.ic_home_hy://企业黄页
                mPresenter.recUseApp("4");
                startActivity(YellowPageActivityV1.newInstance(getCurContext()));
                break;
            case R.drawable.ic_home_search://汇率查询
                mPresenter.recUseApp("5");
                startActivity(RateActivity.newInstance(getCurActivity()));
                break;
            case R.drawable.ic_home_fy://在线翻译
                mPresenter.recUseApp("6");
                startActivity(TranslateActivity.newInstance(getCurActivity()));
                break;
            case R.drawable.home_feedback://意见反馈
                mPresenter.recUseApp("7");
                startActivity(FeedBackActivity.newInstance(getCurContext()));
                break;
            case R.drawable.payment_problem://有偿问答
                mPresenter.recUseApp("8");
                startActivity(PayMentActivity.newInstance(getCurContext()));
                break;
        }
    }

    @OnClick({R.id.ivBack, R.id.ivClear, R.id.tvSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.ivClear:
                etSearch.setText("");
                break;
            case R.id.tvSearch:
                doSearch();
                break;
        }
    }

    private void doSearch() {
        String content = etSearch.getText().toString();
        if (TextUtils.isEmpty(content)) {
            CommonToast.toast(getString(R.string.hint_input_search_content));
            return;
        }
        SearchLocal(content);
        SoftInputUtil.hideSoftInput(getCurActivity());
    }

    private void SearchLocal(String content) {
        aftersearch.clear();
        for (AppModel model : AppAllList) {
            if (model.getAppname().contains(content)) {
                aftersearch.add(model);
            }
        }
        allappAdapter.setList(aftersearch);
    }

    @Override
    public void showClearImage(boolean enableShow) {
        ivClear.setVisibility(enableShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void watchSearch(String search) {
        if (search.equals("")) {
            aftersearch.clear();
            allappAdapter.setList(aftersearch);
        } else {
            SearchLocal(search);
        }
    }

    @Override
    public void showUsed(String s) {
        ItemClick(s);
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
        }

    }
}

