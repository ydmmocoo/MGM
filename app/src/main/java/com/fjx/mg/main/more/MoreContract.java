package com.fjx.mg.main.more;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.RecAppListModel;

public interface MoreContract {
    interface View extends BaseView {
        void showUsed(RecAppListModel s);

        void showUsed(String s);
    }

    abstract class Presenter extends BasePresenter<MoreContract.View> {
        public Presenter(View view) {
            super(view);
        }

        abstract void recUseApp(String appId);

        abstract void getRecAppList();

    }
}
