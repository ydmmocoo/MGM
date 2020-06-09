package com.fjx.mg.nearbycity.mvp;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.NearbyCItyGetListModel;
import com.library.repository.models.NearbyCityConfigModel;
import com.library.repository.models.ResponseModel;

public interface TopTypeDetailContract {

    interface View extends BaseView {

        void responseFailed(ResponseModel model);

        void responseNearbyCityList(NearbyCItyGetListModel model);


        void responsePraise();

        void responseCancelPraise();


    }

    abstract class Presenter extends BasePresenter<TopTypeDetailContract.View> {

        Presenter(TopTypeDetailContract.View view) {
            super(view);
        }


        public abstract void requestNearbyCityList(String page, String k, String typeId);

        public abstract void requestPraise(String commentId, String replyId, String cId);

        public abstract void requestCancelPraise(String commentId, String replyId, String cId);


    }
}
