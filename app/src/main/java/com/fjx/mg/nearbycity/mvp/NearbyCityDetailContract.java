package com.fjx.mg.nearbycity.mvp;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.NearbyCityCommentModel;
import com.library.repository.models.NearbyCityInfoDetailModel;
import com.library.repository.models.ResponseModel;

public interface NearbyCityDetailContract {

    interface View extends BaseView {
        void responseDetails(NearbyCityInfoDetailModel data);

        void responseFailed(ResponseModel model);

        void addCommentSuccess();

        void addCommentFailed();

        void responsePraise();

        void responseCancelPraise();

    }

    abstract class Presenter extends BasePresenter<NearbyCityDetailContract.View> {

        Presenter(NearbyCityDetailContract.View view) {
            super(view);
        }


        public abstract void requestDetails(String cId);


        public abstract void addComment(String cId, String content);

        public abstract void requestPraise(String commentId, String replyId, String cId);

        public abstract void requestCancelPraise(String commentId, String replyId, String cId);

        public abstract void requestUpdateReadNum(String cId);
    }
}
