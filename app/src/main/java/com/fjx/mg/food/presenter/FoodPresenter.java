package com.fjx.mg.food.presenter;

import android.util.Log;

import com.fjx.mg.food.contract.FoodContract;
import com.fjx.mg.utils.HttpUtil;
import com.google.gson.Gson;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.AdListModel;
import com.library.repository.models.GoogleMapGeocodeSearchBean;
import com.library.repository.models.HotShopBean;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.HomeShopListBean;
import com.library.repository.models.ShopTypeBean;
import com.library.repository.repository.RepositoryFactory;
import com.library.repository.util.LogUtil;
import com.tencent.qcloud.uikit.TimConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.library.common.utils.RxJavaUtls.runOnUiThread;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class FoodPresenter extends FoodContract.Presenter {

    public FoodPresenter(FoodContract.View view) {
        super(view);
    }

    @Override
    public void getShopTypeList(String id) {
        RepositoryFactory.getRemoteFoodApi().getShopTypeList(id)
                .compose(RxScheduler.<ResponseModel<ShopTypeBean>>toMain())
                .as(mView.<ResponseModel<ShopTypeBean>>bindAutoDispose())
                .subscribe(new CommonObserver<ShopTypeBean>() {
                    @Override
                    public void onSuccess(ShopTypeBean data) {
                        if (mView != null) {
                            mView.getShopTypeListSuccess(getMenuList(data.getShopTypeList()));
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null)
                            mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    public void getAd() {
        RepositoryFactory.getRemoteRepository().getAd("10")
                .compose(RxScheduler.<ResponseModel<AdListModel>>toMain())
                .as(mView.<ResponseModel<AdListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AdListModel>() {
                    @Override
                    public void onSuccess(AdListModel data) {
                        if (mView != null) {
                            mView.getBannerDataSuccess(data.getAdList());
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                    }
                });
    }

    @Override
    public void getHotShops() {
        RepositoryFactory.getRemoteFoodApi().getHotShops()
                .compose(RxScheduler.<ResponseModel<HotShopBean>>toMain())
                .as(mView.<ResponseModel<HotShopBean>>bindAutoDispose())
                .subscribe(new CommonObserver<HotShopBean>() {
                    @Override
                    public void onSuccess(HotShopBean data) {
                        if (mView!=null){
                            mView.getHotShopsSuccess(data.getShopList());
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                    }
                });
    }

    @Override
    public void getShopsList(String serviceId, String secondServiceId, String order,int page) {
        String lng=RepositoryFactory.getLocalRepository().getLongitude();
        String lat=RepositoryFactory.getLocalRepository().getLatitude();
        RepositoryFactory.getRemoteFoodApi().getShopList(lng,lat,1,serviceId,secondServiceId,
                order,1,0,"",page)
                .compose(RxScheduler.<ResponseModel<HomeShopListBean>>toMain())
                .as(mView.<ResponseModel<HomeShopListBean>>bindAutoDispose())
                .subscribe(new CommonObserver<HomeShopListBean>() {
                    @Override
                    public void onSuccess(HomeShopListBean data) {
                        if (mView != null) {
                            mView.getShopListSuccess(data.getShopList(),data.isHaxNext());
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null) {
                            mView.getShopListFailure();
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null)
                            mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    public void getAddress(String lat, String lon) {
        String language = RepositoryFactory.getLocalRepository().getLangugeType();
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon + "+"
                + "&key=AIzaSyAOtsgwEAwJ7SjsM1oHDVmp6oLfOS24Rj4&language=" + language;

        new HttpUtil().sendPost(url, new HttpUtil.OnRequestListener() {
            @Override
            public void onSuccess(String json) {
                runOnUiThread(() -> {
                    GoogleMapGeocodeSearchBean data=JsonUtil.strToModel(json,GoogleMapGeocodeSearchBean.class);
                    if (data!=null) {
                        mView.getAddressSuccess(data.getResults().get(0).getFormatted_address());
                    }
                });
            }

            @Override
            public void onFailed() {
            }

            @Override
            public void onSuccess(Response response) {
            }
        });
    }

    public List<ShopTypeBean.ShopTypeListBean> getMenuList(List<ShopTypeBean.ShopTypeListBean> list){
        List<ShopTypeBean.ShopTypeListBean> menuList=new ArrayList<>();
        if (list==null){
            return menuList;
        }
        for (int i=0;i<5;i++){
            menuList.add(list.get(i));
        }
        for (int i=0;i<list.size();i++){
            for (int j=0;j<list.get(i).getSecondList().size();j++){
                ShopTypeBean.ShopTypeListBean bean=new ShopTypeBean.ShopTypeListBean();
                bean.setCId(list.get(i).getCId());
                bean.setSecondId(list.get(i).getSecondList().get(j).getSecondId());
                bean.setName(list.get(i).getSecondList().get(j).getSecondName());
                bean.setImg(list.get(i).getSecondList().get(j).getImg());
                menuList.add(bean);
            }
        }
        return menuList;
    }
}
