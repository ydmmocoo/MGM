package com.fjx.mg.sharetoapp.mvp;

import com.library.common.utils.StringUtil;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.TUIKit;
import com.tencent.qcloud.uikit.business.session.model.SessionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/11/6.
 * Description：
 */
public class AllFriendsSelectPresenter extends AllFriendsSelectContract.Presenter {
    public AllFriendsSelectPresenter(AllFriendsSelectContract.View view) {
        super(view);
    }

    @Override
    public void requestAllFriends() {
        mView.createAndShowDialog();
        RepositoryFactory.getChatRepository().getAllFriend(new TIMValueCallBack<List<TIMFriend>>() {
            @Override
            public void onError(int i, String s) {
                if (mView == null) return;
                mView.destoryAndDismissDialog();
            }

            @Override
            public void onSuccess(List<TIMFriend> timFriends) {
                if (mView == null) return;
                mView.destoryAndDismissDialog();
                List<SessionInfo> infos = new ArrayList<>();
                RepositoryFactory.getLocalRepository().saveAllFriend(timFriends);
                List<TIMFriend> allFriend = TUIKit.getAllFriend();
                for (int i = 0; i < allFriend.size(); i++) {
                    SessionInfo info = new SessionInfo();
                    String nickname = "";
                    if (StringUtil.isNotEmpty(allFriend.get(i).getRemark())) {
                        nickname = allFriend.get(i).getRemark();
                    } else {
                        nickname = allFriend.get(i).getTimUserProfile().getNickName();
                    }
                    info.setTitle(nickname);
                    info.setIconUrl(allFriend.get(i).getTimUserProfile().getFaceUrl());
                    info.setPeer(timFriends.get(i).getIdentifier());
                    infos.add(info);
                }

                if (mView != null) {
                    mView.responseAllFriends(infos);
                }

            }
        });
    }

    @Override
    public void requestSearchAllFriends(String name, List<SessionInfo> datas) {
        List<SessionInfo> sessionInfos = new ArrayList<>();
        for (SessionInfo session : datas) {
            if (session.getTitle().contains(name)) {
                sessionInfos.add(session);
            }
        }
        if (mView != null) {
            mView.responseSearchAllFriends(sessionInfos);
        }
    }

}
