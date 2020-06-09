package com.fjx.mg.food.contract;

import com.fjx.mg.main.fragment.home.HomeContract;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.AdListModel;
import com.library.repository.models.AdModel;
import com.library.repository.models.HomeShopListBean;
import com.library.repository.models.HotShopBean;
import com.library.repository.models.ShopTypeBean;
import com.library.repository.models.UserInfoModel;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public interface FoodContract {

    interface View extends BaseView {

        void getShopTypeListSuccess(List<ShopTypeBean.ShopTypeListBean> data);

        void getBannerDataSuccess(List<AdModel> data);

        void getHotShopsSuccess(List<HotShopBean.ShopListBean> data);

        void getShopListSuccess(List<HomeShopListBean.ShopListBean> data,boolean hasNext);

        void getShopListFailure();
    }

    public abstract class Presenter extends BasePresenter<FoodContract.View> {

        public Presenter(FoodContract.View view) {
            super(view);
        }

        public abstract void getShopTypeList(String pId);

        public abstract void getAd();

        public abstract void getHotShops();

        public abstract void getShopsList(String serviceId,String secondServiceId,String order,
        int page);

    }
}
