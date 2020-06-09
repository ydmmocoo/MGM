package com.tencent.qcloud.uikit.operation.c2c.mvp;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.tencent.imsdk.TIMUserProfile;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2020/3/10.
 * Description：
 */
public interface PersonalChatSettingsContact {

    interface View extends BaseView {

        void queryUserProfileSuc(TIMUserProfile profile);
    }

    abstract class Presenter extends BasePresenter<PersonalChatSettingsContact.View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void setTopSession(boolean isTop, String uId);

        public abstract void queryUserProfile(List<String> users);

    }
}
