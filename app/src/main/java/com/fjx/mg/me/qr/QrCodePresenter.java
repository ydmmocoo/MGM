package com.fjx.mg.me.qr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.fjx.mg.R;
import com.fjx.mg.utils.ImageUtils;
import com.library.common.utils.CacheManager;
import com.library.common.utils.DimensionUtil;
import com.library.repository.Constant;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class QrCodePresenter extends QrCodeContract.Presenter {

    QrCodePresenter(QrCodeContract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    void getBitmap(final String uImg, final String url, final Context context) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> e) throws Exception {
                Bitmap bitmap1 = CacheManager.getInstance(context).getBitmap(uImg);
                if (bitmap1 != null) {
                }
                Bitmap logos = BitmapFactory.decodeResource(mView.getCurActivity().getResources(), R.drawable.invite_logo);
                Bitmap bitMBitmap = ImageUtils.getBitMBitmap(uImg);
                int width = DimensionUtil.dip2px(274);
//                Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(url, width, Color.BLACK, uImg.equals("") ? logos : bitMBitmap);
                Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(url, width, Color.BLACK, bitmap1 != null ? bitmap1 : bitMBitmap);
                CacheManager.getInstance(context).putBitmap(url, bitmap);
                CacheManager.getInstance(context).putBitmap(uImg, bitMBitmap);
                e.onNext(bitmap);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                        if (mView != null && bitmap != null) {
                            mView.showQrBitmap(bitmap);
                        }
                    }
                });
    }

    @Override
    void getInviteCode(String uImg, String uid, Context context) {
        getBitmap(uImg, uid, context);
    }


}
