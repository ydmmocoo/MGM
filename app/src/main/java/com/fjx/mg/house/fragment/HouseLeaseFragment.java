package com.fjx.mg.house.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.house.detail.HouseDetailActivity;
import com.fjx.mg.utils.AdClickHelper;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.common.base.BaseMvpFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonImageLoader;
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
import com.library.repository.models.HouseDetailModel;
import com.library.repository.models.HouseListModel;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HouseLeaseFragment extends BaseMvpFragment<HouseLeasePresenter> implements HouseLeaseContract.View {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    @BindView(R.id.dmtType)
    DrapMenuTab dmtType;
    @BindView(R.id.dmtArea)
    DrapMenuTab dmtArea;
    @BindView(R.id.dcview)
    DropContentView dcview;
    @BindView(R.id.dmtHType)
    DrapMenuTab dmtHType;
    @BindView(R.id.dmtSort)
    DrapMenuTab dmtSort;
    @BindView(R.id.bannerView)
    BannerView bannerView;

    @BindView(R.id.image)
    ImageView imageView;
    private HouseLeaseAdapter mAdapter;

    private String mType = "", mCityId, mHType, mSort;
    private int type = 1;

    public static HouseLeaseFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        HouseLeaseFragment fragment = new HouseLeaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected HouseLeasePresenter createPresenter() {
        return new HouseLeasePresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_house_lease;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        mAdapter = new HouseLeaseAdapter();
        mAdapter.setSale(type == 2);
        recycler.setAdapter(mAdapter);
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getHouseLIst(mType, type, mCityId, mHType, mSort, page);
            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String hid = mAdapter.getItem(position).getHid();
                startActivity(HouseDetailActivity.newInstance(getCurContext(), hid));
            }
        });

        List<HouseDetailModel> dataList = DBDaoFactory.getHouseDetailDao().queryList(type);
        mAdapter.setList(dataList);


        refreshView.autoRefresh();
        DropMenuHelper.getInstance().add(dmtType).add(dmtArea).add(dmtHType).add(dmtSort);

        dmtType.attactDropView(dcview, new DropMenuCLickClickter() {
            @Override
            public void onCLickDropItem(ContentType contentType, DropMenuModel firstItem, DropMenuModel secondItem) {
                mType = firstItem.getTypeId();
                refreshView.doRefresh();
            }
        });

        dmtType.setDataList(mPresenter.getTab1Datalist(), ContentType.SINGLE);


        dmtArea.attactDropView(dcview, new DropMenuCLickClickter() {
            @Override
            public void onCLickDropItem(ContentType contentType, DropMenuModel firstItem, DropMenuModel secondItem) {
                mCityId = secondItem.getTypeId();
                refreshView.doRefresh();
            }
        });
        dmtArea.setDataList(mPresenter.getTab2Datalist(), ContentType.DOUBLE);

        dmtHType.attactDropView(dcview, new DropMenuCLickClickter() {
            @Override
            public void onCLickDropItem(ContentType contentType, DropMenuModel firstItem, DropMenuModel secondItem) {
                mHType = firstItem.getTypeId();
                refreshView.doRefresh();
            }
        });
        dmtHType.setDataList(mPresenter.getTab3Datalist(), ContentType.SINGLE);


        dmtSort.attactDropView(dcview, new DropMenuCLickClickter() {
            @Override
            public void onCLickDropItem(ContentType contentType, DropMenuModel firstItem, DropMenuModel secondItem) {
                mSort = firstItem.getTypeId();
                refreshView.doRefresh();
            }
        });
        dmtSort.setDataList(mPresenter.getTab4Datalist(), ContentType.SINGLE);
//        CommonImageLoader.load("http://zt.xm.fzyjzs.com/html/zt/xm/www/799/pz/kt.jpg").into(imageView);
        CommonImageLoader.load(R.drawable.house_banner).into(imageView);
        Log.e("是否请求广告业","是否请求广告业");
        try {
            SharedPreferencesHelper sp = new SharedPreferencesHelper(getActivity());
            Gson gson = new Gson();
            String json = sp.getString("jsonshowHouseBanners");
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
        mPresenter.getAd();
    }

    @Override
    public void onDestroy() {
        DropMenuHelper.getInstance().release();
        super.onDestroy();
    }

    @Override
    public void showHouseListModel(HouseListModel data) {
        refreshView.noticeAdapterData(mAdapter, data.getHouseList(), data.isHasNext());

    }

    @Override
    public void loadError() {
        refreshView.finishLoading();

    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void showBanners(final AdListModel data) {
        if (data == null || data.getAdList().isEmpty()) {
            CommonImageLoader.load(R.drawable.house_banner).into(imageView);
            List<Integer> imageUrl = new ArrayList<>();
            imageUrl.add(R.drawable.house_banner);
            bannerView.showImagesRes(imageUrl);
            return;
        }
        SharedPreferencesHelper sp = new SharedPreferencesHelper(getActivity());
        Gson gson = new Gson();
        sp.putString("jsonshowHouseBanners", gson.toJson(data));
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
        refreshView.doRefresh();
    }
}
