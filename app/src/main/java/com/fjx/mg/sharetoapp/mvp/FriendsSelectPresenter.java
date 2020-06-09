package com.fjx.mg.sharetoapp.mvp;

import android.text.TextUtils;
import android.util.Log;

import com.library.common.utils.JsonUtil;
import com.library.common.utils.LanguageConvent;
import com.library.common.utils.StringUtil;
import com.library.repository.models.FriendContactSectionModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMGroupSystemElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.TUIKit;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;
import com.tencent.qcloud.uikit.business.session.model.SessionInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Author    by hanlz
 * Date      on 2019/11/6.
 * Description：
 */
public class FriendsSelectPresenter extends FriendsSelectContract.Presenter {


    public FriendsSelectPresenter(FriendsSelectContract.View view) {
        super(view);
    }

    @Override
    public void requestSearchResult(String name, List<SessionInfo> infos) {
        List<SessionInfo> searchResult = new ArrayList<>();
        for (SessionInfo session : infos) {
            if (session.getTitle().contains(name)) {
                searchResult.add(session);
            }
        }
        if (mView != null) {
            mView.responseSearchResult(searchResult);
        }
    }

    @Override
    public void requestConversationList(String type) {
        List<TIMConversation> TIMSessions = TIMManager.getInstance().getConversationList();
        ArrayList<SessionInfo> infos = new ArrayList<>();
        for (int i = 0; i < TIMSessions.size(); i++) {
            TIMConversation conversation = TIMSessions.get(i);
            //将imsdk TIMConversation转换为UIKit SessionInfo
            SessionInfo sessionInfo = TIMConversation2SessionInfo(conversation);

            if (sessionInfo != null) {
                if (TextUtils.equals("1", type) || TextUtils.equals("2", type)) {
                    if (sessionInfo.isGroup()) continue;
                }
                infos.add(sessionInfo);
            }
        }
        List<TIMFriend> allFriend = TUIKit.getAllFriend();
        for (int i = 0; i < allFriend.size(); i++) {
            for (int j = 0; j < infos.size(); j++) {
                if (infos.get(j).getPeer().equals(allFriend.get(i).getIdentifier())) {
                    infos.get(j).setTitle(allFriend.get(i).getRemark().equals("") ? allFriend.get(i).getTimUserProfile().getNickName() : allFriend.get(i).getRemark());
                }
            }
        }

        if (mView != null) {
            mView.responseConversationList(infos);
        }
    }

    /**
     * TIMConversation转换为SessionInfo
     *
     * @param session
     * @return
     */
    private SessionInfo TIMConversation2SessionInfo(TIMConversation session) {
        if (session != null) {
            if (TextUtils.isEmpty(session.getPeer())) { // 没有peer的会话，点击进去会有异常，这里做拦截
                return null;
            }
        }

        TIMConversationExt ext = new TIMConversationExt(session);
        TIMMessage message = ext.getLastMsg();
        if (message == null)
            return null;
        SessionInfo info = new SessionInfo();
        TIMConversationType type = session.getType();
        if (type == TIMConversationType.System) {
            if (message.getElementCount() > 0) {
                TIMElem ele = message.getElement(0);
                TIMElemType eleType = ele.getType();
            }
            return null;
        }

        boolean isGroup = type == TIMConversationType.Group;
        info.setLastMessageTime(message.timestamp() * 1000);
        MessageInfo msg = MessageInfoUtil.TIMMessage2MessageInfo(message, isGroup);
        info.setLastMessage(msg);
        if (isGroup)
            info.setTitle(session.getGroupName());
        else {
            info.setTitle(StringUtil.phoneText(session.getPeer()));
        }

        info.setPeer(session.getPeer());
        info.setGroup(session.getType() == TIMConversationType.Group);
        if (ext.getUnreadMessageNum() > 0)
            info.setUnRead((int) ext.getUnreadMessageNum());
        return info;
    }

}
