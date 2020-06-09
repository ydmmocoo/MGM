package com.fjx.mg.main.fragment.friendtab;

import com.library.repository.models.MomentsReplyListModel;
import com.library.repository.models.UnReadCountBean;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

public interface TabFriendContract {

    interface View extends BaseView {
        void isShowRedPoint(UnReadCountBean bean);
        void friendsMomentsReply(MomentsReplyListModel data,String momentsFriendCount);
    }

    abstract class Presenter extends BasePresenter<TabFriendContract.View> {

        Presenter(TabFriendContract.View view) {
            super(view);
        }

        abstract void requestIsShowRedPoint();
    }

}
