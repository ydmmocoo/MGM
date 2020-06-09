package com.fjx.mg.setting.feedback;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.List;

public interface FeedBackContract {
    interface View extends BaseView {
        void uploadImageSucces(List<String> remoteUrl);

        void feedbackSuccess();
    }

    abstract class Presenter extends BasePresenter<FeedBackContract.View> {

        public Presenter(FeedBackContract.View view) {
            super(view);
        }

        abstract void updateImage(String content, final List<String> filePaths, int type,String identifier);

        abstract void feedback(String content, String filePaths, int type,String identifier);

    }
}
