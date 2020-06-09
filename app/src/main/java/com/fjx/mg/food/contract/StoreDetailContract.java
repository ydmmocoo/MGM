package com.fjx.mg.food.contract;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.StoreShopInfoBean;
import com.library.repository.models.ShopingCartBean;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public interface StoreDetailContract {

    interface View extends BaseView {

        void getShopInfoSuccess(StoreShopInfoBean data);

        void getShopCartDataSuccess(ShopingCartBean data);

        void addShopCartSuccess();

        void clearShopCartSuccess();

        void collectSuccess();

        void cancelCollectSuccess();
    }

    public abstract class Presenter extends BasePresenter<StoreDetailContract.View> {

        public Presenter(StoreDetailContract.View view) {
            super(view);
        }

        public abstract void getShopInfo(String sId);

        public abstract void getShopCartData(String sId);

        public abstract void addShopCart(String sId, String gId, String gName, String seId, String seName,
                         String aIds, String aNames, String price, String num,String img);

        public abstract void clearShopCart(String sId);

        public abstract void collect(String sId);

        public abstract void cancelCollect(String sId);
    }
}
