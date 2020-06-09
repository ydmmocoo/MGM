package com.fjx.mg.job.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.job.detail.JobDetailActivity;
import com.fjx.mg.main.search.SearchActivity;
import com.fjx.mg.utils.AdClickHelper;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.common.base.BaseMvpFragment;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.view.BannerView;
import com.library.common.view.dropmenu.ContentType;
import com.library.common.view.dropmenu.DrapMenuTab;
import com.library.common.view.dropmenu.DropContentView;
import com.library.common.view.dropmenu.DropMenuCLickClickter;
import com.library.common.view.dropmenu.DropMenuHelper;
import com.library.common.view.dropmenu.DropMenuModel;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.AdListModel;
import com.library.repository.models.AdModel;
import com.library.repository.models.JobConfigModel;
import com.library.repository.models.JobListModel;
import com.library.repository.models.JobModel;
import com.library.repository.repository.RepositoryFactory;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class JobHuntinFragment extends BaseMvpFragment<JobHuntinPresenter> implements JobHuntinContract.View {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.dmtType)
    DrapMenuTab dmtType;
    @BindView(R.id.dmtArea)
    DrapMenuTab dmtArea;
    @BindView(R.id.dmtExp)
    DrapMenuTab dmtExp;
    @BindView(R.id.dmtEdu)
    DrapMenuTab dmtEdu;

    @BindView(R.id.llSearch)
    LinearLayout llSearch;
    @BindView(R.id.tvSearch)
    TextView tvSearch;

    @BindView(R.id.bannerView)
    BannerView bannerView;

    @BindView(R.id.dcview)
    DropContentView dcview;
    private JobHuntinAdapter mAdapter;
    private String mTypeId, priceArea, expId, eduId;
    private JobConfigModel configModel;

    public static JobHuntinFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        JobHuntinFragment fragment = new JobHuntinFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected JobHuntinPresenter createPresenter() {
        return new JobHuntinPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_job_huntin;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final int type = getArguments().getInt("type");
        mPresenter.getConfig();
        configModel = RepositoryFactory.getLocalRepository().getJobConfig();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        mAdapter = new JobHuntinAdapter();
        mAdapter.setSelfPadding(true);
        recycler.setAdapter(mAdapter);
        CommonImageLoader.load(R.drawable.job_banner).into(imageView);
        List<JobModel> dataList = DBDaoFactory.getJobListModelDao().queryList(type);
        mAdapter.setList(dataList);
        refreshView.autoRefresh();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                //                if (configModel == null) return;
                startActivity(JobDetailActivity.newInstance(getCurContext(),
                        mAdapter.getItem(position).getJobId(), type == 1));
            }
        });
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getJobList(page, type, mTypeId, priceArea, expId, eduId);
            }
        });


        llSearch.setBackgroundColor(ContextCompat.getColor(getCurContext(), R.color.transWhite1));
        GradientDrawableHelper.whit(tvSearch).setColor(R.color.transWhite2).setCornerRadius(10);
        if (configModel == null) {
            return;
        }
        try {
            SharedPreferencesHelper sp = new SharedPreferencesHelper(getActivity());
            Gson gson = new Gson();
            String json = sp.getString("jsonshowJobBanners");
            if (!json.equals("")) {
                Log.e("json:", "" + json);
                AdListModel statusLs = gson.fromJson(json, new TypeToken<AdListModel>() {
                }.getType());
                showBanners(statusLs);
            }
            sp.close();

        } catch (Exception e) {
            Log.e("Exception:", "" + e.toString());
        }
        initSelectTab();
        mPresenter.getAd();

    }

    private void initSelectTab() {
        DropMenuHelper.getInstance().add(dmtType).add(dmtArea).add(dmtExp).add(dmtEdu);
        dmtType.attactDropView(dcview, new DropMenuCLickClickter() {
            @Override
            public void onCLickDropItem(ContentType contentType, DropMenuModel firstItem, DropMenuModel secondItem) {
                mTypeId = firstItem.getTypeId();
                dmtType.setText(firstItem.getTypeName());
                if (TextUtils.isEmpty(firstItem.getTypeId())) {
                    dmtType.setText("");
                } else {
                    dmtType.setText(firstItem.getTypeName());
                }
                refreshView.doRefresh();
            }
        });
        dmtType.setDataList(mPresenter.getTab1Datalist(), ContentType.SINGLE);

        dmtArea.attactDropView(dcview, new DropMenuCLickClickter() {
            @Override
            public void onCLickDropItem(ContentType contentType, DropMenuModel firstItem, DropMenuModel secondItem) {
                priceArea = firstItem.getTypeId();
                if (TextUtils.isEmpty(firstItem.getTypeId())) {
                    dmtArea.setText("");
                } else {
                    dmtArea.setText(firstItem.getTypeName());
                }
                refreshView.doRefresh();
            }
        });
        dmtArea.setDataList(mPresenter.getTab2Datalist(), ContentType.SINGLE);

        dmtExp.attactDropView(dcview, new DropMenuCLickClickter() {
            @Override
            public void onCLickDropItem(ContentType contentType, DropMenuModel firstItem, DropMenuModel secondItem) {
                expId = firstItem.getTypeId();
                if (TextUtils.isEmpty(firstItem.getTypeId())) {
                    dmtExp.setText("");
                } else {
                    dmtExp.setText(firstItem.getTypeName());
                }
                refreshView.doRefresh();
            }
        });
        dmtExp.setDataList(mPresenter.getTab3Datalist(), ContentType.SINGLE);

        dmtEdu.attactDropView(dcview, new DropMenuCLickClickter() {
            @Override
            public void onCLickDropItem(ContentType contentType, DropMenuModel firstItem, DropMenuModel secondItem) {
                eduId = firstItem.getTypeId();
                if (TextUtils.isEmpty(firstItem.getTypeId())) {
                    dmtEdu.setText("");
                } else {
                    dmtEdu.setText(firstItem.getTypeName());
                }

                refreshView.doRefresh();
            }
        });
        dmtEdu.setDataList(mPresenter.getTab4Datalist(), ContentType.SINGLE);
    }


    @Override
    public void showJobListModel(JobListModel data) {
        refreshView.noticeAdapterData(mAdapter, data.getJobsList(), data.isHasNext());
    }

    @Override
    public void showConfigTab(JobConfigModel configModel) {
        this.configModel = configModel;
        initSelectTab();
    }

    @Override
    public void loadError() {
        refreshView.finishLoading();
    }

    @Override
    public void showBanners(final AdListModel data) {
        if (data == null || data.getAdList().isEmpty()) {
            CommonImageLoader.load(R.drawable.job_banner).into(imageView);
            List<Integer> imageUrl = new ArrayList<>();
            imageUrl.add(R.drawable.job_banner);
            bannerView.showImagesRes(imageUrl);
            return;
        }
        SharedPreferencesHelper sp = new SharedPreferencesHelper(getActivity());
        Gson gson = new Gson();
        sp.putString("jsonshowJobBanners", gson.toJson(data));
        sp.close();


        List<String> imageUrl = new ArrayList<>();
        for (AdModel model : data.getAdList()) {
            imageUrl.add(model.getImg());
        }
        bannerView.showImages(imageUrl);
        bannerView.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                AdModel adModel = data.getAdList().get(position);
                AdClickHelper.clickAd(adModel);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshView.autoRefresh();
    }


    @OnClick(R.id.tvSearch)
    public void onViewClicked() {
        startActivity(SearchActivity.newInstance(getCurContext()));
    }
}
