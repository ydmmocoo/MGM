package com.fjx.mg.me.invite;

import android.content.Intent;
import android.graphics.Bitmap;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.InviteModel;

public interface InviteContract {


    interface View extends BaseView {

        void showQrBitmap(Bitmap bitmap);

        void showInviteMessage(InviteModel model);

        void showSelectPhoneNUm(String number);
    }

    abstract class Presenter extends BasePresenter<InviteContract.View> {

        Presenter(InviteContract.View view) {
            super(view);
        }

        abstract void getBitmap(String url);

        abstract void getInviteCode(InviteActivity inviteActivity);

        abstract void getPhoneNum(Intent data);
    }
}
