package com.fjx.mg.sharetoapp.mvp;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.FriendContactSectionModel;
import com.tencent.qcloud.uikit.business.session.model.SessionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/11/6.
 * Description：
 */
public interface FriendsSelectContract {

    interface View extends BaseView {

        void responseSearchResult(List<SessionInfo> session);

        void responseConversationList(List<SessionInfo> session);
    }


    abstract class Presenter extends BasePresenter<View> {
        public Presenter(View view) {
            super(view);
        }

        /**
         * 关键字搜索最近聊天列表的好友
         *
         * @param name  用户名字
         * @param infos 最近列表里的好友
         */
        public abstract void requestSearchResult(String name, List<SessionInfo> infos);

        /**
         * 请求最近聊天列表的好友
         */
        public abstract void requestConversationList(String type);
    }
}
