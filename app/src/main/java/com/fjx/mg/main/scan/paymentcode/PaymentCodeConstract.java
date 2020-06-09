package com.fjx.mg.main.scan.paymentcode;

import android.content.Context;
import android.graphics.Bitmap;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.PayCodeModel;

/**
 * Author    by hanlz
 * Date      on 2020/4/2.
 * Description：
 */
public interface PaymentCodeConstract {

    interface View extends BaseView {
        void showBarCodeBitmap(Bitmap bitmap);

        void showQRCodeBitmap(Bitmap bitmap);

        void setPayCodeStutasSuc();

        void getPayCodeSuc(PayCodeModel data);
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void createBarCode(Context context, String contents);

        public abstract void createQRCode(String contents);

        public abstract void setPayCode(String type);

        public abstract void getPayCode();
    }
}
