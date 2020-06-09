package com.fjx.mg.food.contract;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.StoreEvaluateBean;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public interface StoreEvaluateContract {

    interface View extends BaseView {

        void getEvaluateListSuccess(StoreEvaluateBean data);

    }

    public abstract class Presenter extends BasePresenter<StoreEvaluateContract.View> {

        public Presenter(StoreEvaluateContract.View view) {
            super(view);
        }

        public abstract void getEvaluateList(String sId,String searchType,int page);
    }
}
