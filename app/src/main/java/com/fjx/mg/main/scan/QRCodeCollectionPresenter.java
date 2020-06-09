package com.fjx.mg.main.scan;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;

import com.fjx.mg.R;
import com.fjx.mg.utils.HttpUtil;
import com.fjx.mg.utils.ImageUtils;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.common.utils.CacheManager;
import com.library.common.utils.CommonToast;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ZXingUtils;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.PaymentCodeModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.qcloud.uikit.TimConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

public class QRCodeCollectionPresenter extends QRCodeCollectionContract.Presenter {


    private SharedPreferencesHelper sharePrefrenceHelper;

    QRCodeCollectionPresenter(QRCodeCollectionContract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    void getBitmap(final String url, final String uimg, final Context qrCodeCollectionActivity) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> e) throws Exception {
                int width = DimensionUtil.dip2px(274);
                final Bitmap bitmap = ZXingUtils.createQRImage(url, width, width);
                CacheManager.getInstance(qrCodeCollectionActivity).putBitmap(url, bitmap);
                e.onNext(bitmap);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                        if (mView != null && bitmap != null) {
                            mView.showQrBitmap(bitmap);
                            mView.destoryAndDismissDialog();
                        }
                    }
                });
    }

    /**
     * 设置金额使用次方法生成二维码
     *
     * @param price
     * @param qrCodeCollectionActivity
     */
    void QRCode(String price, final Context qrCodeCollectionActivity) {
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().getQRCollectionCode(price)
                .compose(RxScheduler.<ResponseModel<PaymentCodeModel>>toMain())
                .as(mView.<ResponseModel<PaymentCodeModel>>bindAutoDispose())
                .subscribe(new CommonObserver<PaymentCodeModel>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onSuccess(final PaymentCodeModel data) {
                        Observable.create(new ObservableOnSubscribe<Bitmap>() {
                            @Override
                            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> e) throws Exception {
                                int width = DimensionUtil.dip2px(274);
                                final Bitmap bitmap = ZXingUtils.createQRImage(data.getPayCode(), width, width);
                                e.onNext(bitmap);
                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Consumer<Bitmap>() {
                                    @Override
                                    public void accept(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                                        if (mView != null && bitmap != null) {
                                            mView.showQrBitmap(bitmap);
                                            mView.hideLoading();
                                        }
                                    }
                                });

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.hideLoading();
                            mView.downloadBitmapFailed();
                        }
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        Log.d("AppLog", data.getMsg());
                        CommonToast.toast(data.getMsg());
                        if (mView != null) {
                            mView.hideLoading();
                            mView.downloadBitmapFailed();
                        }
                    }
                });
    }

    @Override
    void QRCollectionCode(String price, final String uimg, final Context qrCodeCollectionActivity) {
        sharePrefrenceHelper = new SharedPreferencesHelper(qrCodeCollectionActivity);
        String qrurl = sharePrefrenceHelper.getString("QRCodeCollection" + UserCenter.getUserInfo().getIdentifier());
        Bitmap bitmap1 = null;
        if (!qrurl.equals("")) {
            bitmap1 = CacheManager.getInstance(qrCodeCollectionActivity).getBitmap(qrurl);
            if (bitmap1 != null) {
                if (mView != null) {
                    mView.showQrBitmap(bitmap1);
                }
            } else {
                mView.createAndShowDialog();
            }
        } else {
            mView.createAndShowDialog();
        }


        RepositoryFactory.getRemoteAccountRepository().getQRCollectionCode(price)
                .compose(RxScheduler.<ResponseModel<PaymentCodeModel>>toMain())
                .as(mView.<ResponseModel<PaymentCodeModel>>bindAutoDispose())
                .subscribe(new CommonObserver<PaymentCodeModel>() {
                    @Override
                    public void onSuccess(PaymentCodeModel data) {
                        sharePrefrenceHelper.putString("QRCodeCollection" + UserCenter.getUserInfo().getIdentifier(), data.getPayCode());
                        sharePrefrenceHelper.close();
                        getBitmap(data.getPayCode(), uimg, qrCodeCollectionActivity);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.hideLoading();
                            mView.downloadBitmapFailed();
                        }
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        Log.d("AppLog", data.getMsg());
                        CommonToast.toast(data.getMsg());
                        if (mView != null) {
                            mView.hideLoading();
                            mView.downloadBitmapFailed();
                        }
                    }
                });
    }

    @Override
    void Band(String price) {
        RepositoryFactory.getRemoteAccountRepository().workBind(price)
                .compose(RxScheduler.toMain())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        Gson gson = new Gson();
                        ResponseModel statusLs = gson.fromJson(o.toString(), new TypeToken<ResponseModel>() {
                        }.getType());
                        Log.e("" + statusLs.getCode(), "" + statusLs.getMsg());
                        if (statusLs.getCode() == 10000) {
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                        Log.e("accept失败", "" + throwable.toString());
                    }
                });
    }


}
