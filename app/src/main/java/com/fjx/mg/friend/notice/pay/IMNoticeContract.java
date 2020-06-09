package com.fjx.mg.friend.notice.pay;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.IMNoticeListModel;

public interface IMNoticeContract {
    interface View extends BaseView {
        void showNoticeList(IMNoticeListModel data);

        void loadError();

    }

    abstract class Presenter extends BasePresenter<IMNoticeContract.View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void getNoticeList(int page);
    }


}
