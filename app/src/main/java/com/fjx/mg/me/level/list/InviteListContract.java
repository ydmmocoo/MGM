package com.fjx.mg.me.level.list;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.InviteListModel;
import com.library.repository.models.LevelHomeModel;

public interface InviteListContract {

    interface IView extends BaseView {

        void showInviteList(InviteListModel model);

        void loadError();


    }

    abstract class Presenter extends BasePresenter<IView> {
        public Presenter(IView view) {
            super(view);
        }

        abstract void myInvite(int page);

        abstract void showInviteNumDialog(String num);
    }

}
