package com.fjx.mg.nearbycity.mvp;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.NearbyCItyGetListModel;
import com.library.repository.models.NearbyCityConfigModel;
import com.library.repository.models.NearbyCityHotCompanyModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UnReadCountBean;

public interface NearbyCityContract {

    interface View extends BaseView {

        void responseFailed(ResponseModel model);

        void responseConfigDatas(NearbyCityConfigModel model);

        void responseHotCompanyDatas(NearbyCityHotCompanyModel model);

        void responseNearbyCityList(NearbyCItyGetListModel model);

        void responsePraise();

    }

    abstract class Presenter extends BasePresenter<NearbyCityContract.View> {

        Presenter(NearbyCityContract.View view) {
            super(view);
        }


        public abstract void requestConfig();

        public abstract void requestHotCompany();


        public abstract void requestNearbyCityList(String page, String k, String typeId);

        public abstract void requestPraise(String commentId, String replyId, String cId);

    }
}
