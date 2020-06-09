package com.tencent.qcloud.uikit.operation.group.mvp;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.ext.group.TIMGroupSelfInfo;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupChatInfo;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/12/3.
 * Description：
 */
public interface GroupChatSettingsContact {

    interface View extends BaseView {
        void setGroupName(String name);

        void setNickname(String name);

        void setNameCard(TIMGroupSelfInfo timGroupSelfInfo);

        void getGroupChatInfo(GroupChatInfo groupChatInfo);

        void getTimUserProfile(List<TIMUserProfile> datas);//用户信息

        void messageSate(String groupId, String userId, boolean open);
    }

    abstract class Persenter extends BasePresenter<View> {

        public Persenter(View view) {
            super(view);
        }
    }
}
