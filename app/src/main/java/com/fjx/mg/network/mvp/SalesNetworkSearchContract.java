package com.fjx.mg.network.mvp;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.SearchAgentListModel;

import java.util.List;


public interface SalesNetworkSearchContract {

    interface View extends BaseView {
        void responseFailed(ResponseModel data);

        void responseAgentList(List<SearchAgentListModel> items,String lng, String lat,String sName);
    }

    abstract class Presenter extends BasePresenter<SalesNetworkSearchContract.View> {

        public Presenter(View view) {
            super(view);
        }

        /**
         * @param lng    经度  是
         * @param lat    纬度  是
         * @param type   3:店铺，4:营业网点
         * @param remark 备注搜索
         */
        public abstract void requestAgentList(String lng, String lat, String type, String remark,String price,String sName);

    }

}
