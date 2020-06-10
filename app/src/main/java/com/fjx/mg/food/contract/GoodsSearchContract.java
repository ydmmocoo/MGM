package com.fjx.mg.food.contract;

import android.widget.ImageView;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CouponBean;
import com.library.repository.models.GoodsSearchBean;
import com.library.repository.models.ShopingCartBean;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public interface GoodsSearchContract {

    interface View extends BaseView {

        void getGoodsListSuccess(List<GoodsSearchBean.GoodsListBean> data,boolean hasNext);

        void getShopCartDataSuccess(ShopingCartBean data);

        void addShopCartSuccess(ImageView ivAdd);

        void clearShopCartSuccess();
    }

    public abstract class Presenter extends BasePresenter<GoodsSearchContract.View> {

        public Presenter(GoodsSearchContract.View view) {
            super(view);
        }

        public abstract void getGoodsList(String sId,String name,int page);

        public abstract void getShopCartData(String sId);

        public abstract void addShopCart(String sId,String gId,String gName,String seId,String seName,
                                         String aIds,String aNames,String price,String num,String img,ImageView ivAdd);

        public abstract void clearShopCart(String sId);
    }
}
