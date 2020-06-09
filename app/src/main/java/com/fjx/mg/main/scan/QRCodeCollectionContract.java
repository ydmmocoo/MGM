package com.fjx.mg.main.scan;

import android.content.Context;
import android.graphics.Bitmap;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

public interface QRCodeCollectionContract {


    interface View extends BaseView {
        void showQrBitmap(Bitmap bitmap);
        void downloadBitmapFailed();
    }

    abstract class Presenter extends BasePresenter<QRCodeCollectionContract.View> {

        Presenter(QRCodeCollectionContract.View view) {
            super(view);
        }

        abstract void getBitmap(String url, String uimg, Context context);

        abstract void QRCollectionCode(String price, String uimg, Context context);

        abstract void Band(String price);

    }
}
