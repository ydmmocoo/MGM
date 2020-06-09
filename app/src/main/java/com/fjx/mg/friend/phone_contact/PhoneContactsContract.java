package com.fjx.mg.friend.phone_contact;

import android.widget.EditText;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.PhoneContactModel;
import com.tencent.imsdk.TIMUserProfile;

import java.util.List;

public interface PhoneContactsContract {
    interface View extends BaseView {
        void showContactList(List<PhoneContactModel> contactModels);

        void getImUserSuccess(TIMUserProfile profile);

        void inviteContent(String text);


    }

    abstract class Presenter extends BasePresenter<PhoneContactsContract.View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void getPhoneContactList();

        abstract void batchCheckUser(List<PhoneContactModel> datas);

        abstract void findImUser(String imUId);

        public abstract void searchWatcher(EditText etSearch);
    }


}
