package com.fjx.mg.me.qr;

import android.content.Context;
import android.graphics.Bitmap;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

public interface QrCodeContract {
    interface View extends BaseView {

        void showQrBitmap(Bitmap bitmap);

    }

    abstract class Presenter extends BasePresenter<QrCodeContract.View> {

        Presenter(QrCodeContract.View view) {
            super(view);
        }

        abstract void getBitmap(String uImg, String url, Context context);

        abstract void getInviteCode(String uImg, String uid, Context context);

    }
}
