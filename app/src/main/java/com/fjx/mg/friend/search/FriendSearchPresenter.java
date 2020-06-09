package com.fjx.mg.friend.search;

import android.text.TextUtils;
import android.util.Log;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.models.FriendContactSectionModel;
import com.library.repository.models.SearchTimFriendModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.common.utils.TIMStringUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class FriendSearchPresenter extends FriendSearchContract.Presenter {
    List<TIMFriend> friendList;
    List<TIMGroupBaseInfo> mTimGroupBaseInfos;
    WeakReference<FriendSearchContract.View> reference;
    List<TIMConversation> mTimSessions;
    List<TIMFriend> mTimConversations;

    public FriendSearchPresenter(FriendSearchContract.View view) {
        super(view);
        reference = new WeakReference<>(view);
//        friendList = RepositoryFactory.getLocalRepository().getAllFriend();
        RepositoryFactory.getChatRepository().getAllFriend(new TIMValueCallBack<List<TIMFriend>>() {

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMFriend> timFriends) {
                friendList = timFriends;
                mTimSessions = new ArrayList<>();
                mTimConversations = new ArrayList<>();
                List<TIMConversation> timSessions = TIMManager.getInstance().getConversationList();
                for (TIMConversation conversation : timSessions) {
                    TIMConversationType type = conversation.getType();
                    if (type == TIMConversationType.C2C) {
                        mTimSessions.add(conversation);
                    }
                }
                for (int i = 0; i < mTimSessions.size(); i++) {
                    for (int j = 0; j < friendList.size(); j++) {
                        if (mTimSessions.get(i).getPeer().equals(friendList.get(j).getIdentifier())) {
                            mTimConversations.add(friendList.get(j));
                        }
                    }
                }
            }
        });
        TIMGroupManager.getInstance().getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
            @Override
            public void onError(int i, String s) {
                CommonToast.toast(s);
            }

            @Override
            public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {
                mTimGroupBaseInfos = timGroupBaseInfos;
            }
        });

    }

    @Override
    void searchFriend(String content) {
        FriendSearchContract.View view = reference.get() == null ? mView : reference.get();
        if (view == null) return;
        if (friendList == null) {
            view.showFriends(null);
            CommonToast.toast(R.string.failed_to_get_friend_information);
            return;
        }

        List<SearchTimFriendModel> result = new ArrayList<>();
        for (TIMFriend friend : friendList) {
            String phone = "";
            byte[] bytes = friend.getTimUserProfile().getCustomInfo().get("phone");
            if (bytes != null)
                phone = new String(bytes);
            boolean b = !TextUtils.isEmpty(phone) && phone.contains(content.toUpperCase());
            SearchTimFriendModel friendModel = new SearchTimFriendModel();
            if (friend.getIdentifier().toUpperCase().contains(content.toUpperCase())) {
                //优先匹配MGM号
                friendModel.setType("1");
                friendModel.setTimFriend(friend);
                result.add(friendModel);
            } else if (b) {
                friendModel.setType("2");
                friendModel.setTimFriend(friend);
                result.add(friendModel);
            } else if (friend.getRemark().toUpperCase().contains(content.toUpperCase())
                    || friend.getTimUserProfile().getNickName().toUpperCase().contains(content.toUpperCase())) {
                friendModel.setType("3");
                friendModel.setTimFriend(friend);
                result.add(friendModel);
            }
        }

        view.showFriends(result);


        List<TIMGroupBaseInfo> timGroupBaseInfos = new ArrayList<>();
        if (mTimGroupBaseInfos == null) {
            view.showGroups(null);
            //此处不影响其他搜索结果
            Log.d("FriendSearchPresenter", "没有创建群/群内容拉取失败");
        } else {
            //有群内容进行显示
            for (TIMGroupBaseInfo info : mTimGroupBaseInfos) {
                if (info.getGroupId().toUpperCase().contains(content)
                        || info.getGroupName().toUpperCase().contains(content.toUpperCase())) {
                    timGroupBaseInfos.add(info);
                }
            }
            view.showGroups(timGroupBaseInfos);
        }

        List<SearchTimFriendModel> friends = new ArrayList<>();
        if (mTimConversations == null || mTimConversations.size() <= 0) {
            //可能存在长时间没使用聊天此处可以为空
            view.showConverser(null);
        } else {
            for (TIMFriend conversation : mTimConversations) {
                String phone = "";
                byte[] bytes = conversation.getTimUserProfile().getCustomInfo().get("phone");
                if (bytes != null)
                    phone = new String(bytes);
                boolean b = !TextUtils.isEmpty(phone) && phone.contains(content.toUpperCase());
                SearchTimFriendModel friendModel = new SearchTimFriendModel();
                if (conversation.getIdentifier().toUpperCase().contains(content.toUpperCase())) {
                    friendModel.setType("1");
                    friendModel.setTimFriend(conversation);
                    friends.add(friendModel);
                } else if (b) {
                    friendModel.setType("2");
                    friendModel.setTimFriend(conversation);
                    friends.add(friendModel);
                } else if (conversation.getRemark().toUpperCase().contains(content.toUpperCase())
                        || conversation.getTimUserProfile().getNickName().toUpperCase().contains(content.toUpperCase())) {
                    friendModel.setType("3");
                    friendModel.setTimFriend(conversation);
                    friends.add(friendModel);
                }
            }
            view.showConverser(friends);
        }

        if (result.size()==0&&timGroupBaseInfos.size()==0&&friends.size()==0){
            CommonToast.toast(mView.getCurContext().getString(R.string.no_user));
        }
    }
}
