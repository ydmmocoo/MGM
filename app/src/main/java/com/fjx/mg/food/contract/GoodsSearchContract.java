package com.fjx.mg.food.contract;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CouponBean;
import com.library.repository.models.GoodsSearchBean;

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

    }

    public abstract class Presenter extends BasePresenter<GoodsSearchContract.View> {

        public Presenter(GoodsSearchContract.View view) {
            super(view);
        }

        public abstract void getGoodsList(String sId,String name,int page);
    }
}
