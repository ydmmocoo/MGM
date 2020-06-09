package com.fjx.mg.friend.notice.sys;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.IMNoticeListModel;

public interface SysNoticeContract {
    interface View extends BaseView {
        void showNoticeList(IMNoticeListModel data);

        void loadError();

    }

    abstract class Presenter extends BasePresenter<SysNoticeContract.View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void getNoticeList(int page);
    }


}
