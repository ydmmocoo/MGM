package com.fjx.mg.sharetoapp.mvp;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.tencent.qcloud.uikit.business.session.model.SessionInfo;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/11/6.
 * Description：
 */
public interface AllFriendsSelectContract {

    interface View extends BaseView {
        void responseAllFriends(List<SessionInfo> datas);

        void responseSearchAllFriends(List<SessionInfo> datas);
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        /**
         * 请求所有好友数据
         */
        abstract void requestAllFriends();

        /**
         * 搜索所有好友里的某个好友
         *
         * @param name
         * @param datas
         */
        abstract void requestSearchAllFriends(String name, List<SessionInfo> datas);
    }
}
