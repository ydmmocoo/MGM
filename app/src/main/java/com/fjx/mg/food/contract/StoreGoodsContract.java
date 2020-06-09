package com.fjx.mg.food.contract;

import android.widget.ImageView;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.HotShopBean;
import com.library.repository.models.ShopTypeBean;
import com.library.repository.models.ShopingCartBean;
import com.library.repository.models.StoreGoodsBean;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public interface StoreGoodsContract {

    interface View extends BaseView {

        void getGoodsDataSuccess(List<StoreGoodsBean.CateListBean> data);

        void addShopCartSuccess(ImageView ivAdd);

    }

    public abstract class Presenter extends BasePresenter<StoreGoodsContract.View> {

        public Presenter(StoreGoodsContract.View view) {
            super(view);
        }

        public abstract void getGoodsData(String sId);

        public abstract void addShopCart(String sId, String gId, String gName, String seId, String seName,
                                         String aIds, String aNames, String price, String num, String img,
                                         ImageView ivAdd);
    }
}
