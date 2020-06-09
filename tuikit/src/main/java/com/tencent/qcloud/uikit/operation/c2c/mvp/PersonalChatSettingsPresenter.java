package com.tencent.qcloud.uikit.operation.c2c.mvp;

import android.widget.CheckBox;

import com.library.common.utils.CommonToast;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupChatManager;
import com.tencent.qcloud.uikit.operation.message.UIKitRequest;
import com.tencent.qcloud.uikit.operation.message.UIKitRequestDispatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author    by hanlz
 * Date      on 2020/3/10.
 * Description：
 */
public class PersonalChatSettingsPresenter extends PersonalChatSettingsContact.Presenter {


    public PersonalChatSettingsPresenter(PersonalChatSettingsContact.View view) {
        super(view);
    }

    /**
     * 置顶聊天 保存置顶状态
     *
     * @param isTop
     */
    @Override
    public void setTopSession(boolean isTop, String uId) {
        UIKitRequest request = new UIKitRequest();
        request.setModel(UIKitRequestDispatcher.MODEL_SESSION);
        request.setAction(UIKitRequestDispatcher.SESSION_ACTION_SET_TOP);
        Map requestData = new HashMap();
        requestData.put("peer", uId);
        requestData.put("topFlag", isTop);
        request.setRequest(requestData);
        UIKitRequestDispatcher.getInstance().dispatchRequest(request);
    }

    /**
     * 获取置顶状态 且设置cb的值
     *
     * @param uId
     */
    public void setChecked(String uId, CheckBox cb) {

        UIKitRequest topRequest = new UIKitRequest();
        topRequest.setAction(UIKitRequestDispatcher.SESSION_ACTION_GET_TOP);
        topRequest.setRequest(uId);
        topRequest.setModel(UIKitRequestDispatcher.MODEL_SESSION);

        Object res = UIKitRequestDispatcher.getInstance().dispatchRequest(topRequest);
        if (res == null)
            return;
        boolean isTop = (boolean) res;
        cb.setChecked(isTop);
    }


    @Override
    public void queryUserProfile(List<String> users) {
        mView.createAndShowDialog();
        TIMFriendshipManager.getInstance().getUsersProfile(users, false, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {
                if (mView != null) {
                    mView.destoryAndDismissDialog();
                }
                CommonToast.toast(s);
            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                if (mView != null) {
                    mView.queryUserProfileSuc(timUserProfiles.get(0));
                    mView.destoryAndDismissDialog();
                }
            }
        });
    }

}
