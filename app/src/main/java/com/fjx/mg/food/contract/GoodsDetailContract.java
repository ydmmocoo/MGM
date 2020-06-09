package com.fjx.mg.food.contract;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CouponBean;
import com.library.repository.models.GoodsDetailBean;
import com.library.repository.models.ShopingCartBean;
import com.library.repository.models.StoreEvaluateBean;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public interface GoodsDetailContract {

    interface View extends BaseView {

        void getGoodsInfoSuccess(GoodsDetailBean data);

        void getShopCartDataSuccess(ShopingCartBean data);

        void addShopCartSuccess();

        void clearShopCartSuccess();

        void getEvaluateListSuccess(StoreEvaluateBean data);
    }

    public abstract class Presenter extends BasePresenter<GoodsDetailContract.View> {

        public Presenter(GoodsDetailContract.View view) {
            super(view);
        }

        public abstract void getGoodsInfo(String gId);

        public abstract void getShopCartData(String sId);

        public abstract void addShopCart(String sId,String gId,String gName,String seId,String seName,
                                         String aIds,String aNames,String price,String num,String img);

        public abstract void clearShopCart(String sId);

        public abstract void getEvaluateList(String sId,String searchType,int page);
    }
}
