package com.fjx.mg.main.scan.paymentcode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;

import com.common.paylibrary.model.UsagePayMode;
import com.library.common.utils.CacheManager;
import com.library.common.utils.CommonToast;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.LogTUtil;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ZXingUtils;
import com.library.common.view.editdialog.PayFragment;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.PayCodeModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Author    by hanlz
 * Date      on 2020/4/2.
 * Description：
 */
public class PaymentCodePresenter extends PaymentCodeConstract.Presenter {

    public PaymentCodePresenter(PaymentCodeConstract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void createBarCode(final Context context, final String contents) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> e) throws Exception {
                int width = DimensionUtil.dip2px(600);
                int height = DimensionUtil.dip2px(130);
                final Bitmap bitmap = ZXingUtils.creatBarcode(context, contents, width, height, false);
                e.onNext(bitmap);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                        if (mView != null) {
                            mView.showBarCodeBitmap(bitmap);
                        }
                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void createQRCode(final String contents) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Bitmap> e) throws Exception {
                int width = DimensionUtil.dip2px(190);
                int height = DimensionUtil.dip2px(190);
                final Bitmap bitmap = ZXingUtils.createQRImage(contents, width, height);
                e.onNext(bitmap);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Bitmap bitmap) throws Exception {
                        if (mView != null) {
                            mView.showQRCodeBitmap(bitmap);
                        }
                    }
                });
    }

    @Override
    public void setPayCode(String type) {
        if (mView != null) {
            mView.createAndShowDialog();
            RepositoryFactory.getRemoteAccountRepository().setPayCode(type)
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {

                        @Override
                        public void onSuccess(Object data) {
                            UserInfoModel userInfo = UserCenter.getUserInfo();
                            if (userInfo != null) {
                                String isUsePayCode = userInfo.getIsUsePayCode();
                                if (StringUtil.isNotEmpty(isUsePayCode)) {
                                    if (StringUtil.equals("1", isUsePayCode)) {
                                        userInfo.setIsUsePayCode("2");
                                    } else {
                                        userInfo.setIsUsePayCode("1");
                                    }
                                    UserCenter.saveUserInfo(userInfo);
                                }
                            }
                            if (mView != null) {
                                mView.destoryAndDismissDialog();
                                mView.setPayCodeStutasSuc();
                            }
                        }

                        @Override
                        public void onError(ResponseModel data) {
                            CommonToast.toast(data.getMsg());
                            if (mView != null) {
                                mView.destoryAndDismissDialog();
                            }
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            CommonToast.toast(data.getMsg());
                            if (mView != null) {
                                mView.destoryAndDismissDialog();
                            }
                        }
                    });
        }
    }

    @Override
    public void getPayCode() {
        if (mView != null) {
            mView.createAndShowDialog();
            RepositoryFactory.getRemoteAccountRepository()
                    .getPayCode()
                    .compose(RxScheduler.<ResponseModel<PayCodeModel>>toMain())
                    .as(mView.<ResponseModel<PayCodeModel>>bindAutoDispose())
                    .subscribe(new CommonObserver<PayCodeModel>() {
                        @Override
                        public void onSuccess(PayCodeModel data) {
                            if (mView != null) {
                                mView.destoryAndDismissDialog();
                                mView.getPayCodeSuc(data);
                            }
                        }

                        @Override
                        public void onError(ResponseModel data) {
                            if (mView != null) {
                                mView.destoryAndDismissDialog();
                            }
                            CommonToast.toast(data.getMsg());
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            if (mView != null) {
                                mView.destoryAndDismissDialog();
                            }
                            CommonToast.toast(data.getMsg());
                        }
                    });
        }
    }

    public void getPayCode2() {
        if (mView != null) {
            RepositoryFactory.getRemoteAccountRepository()
                    .getPayCode()
                    .compose(RxScheduler.<ResponseModel<PayCodeModel>>toMain())
                    .as(mView.<ResponseModel<PayCodeModel>>bindAutoDispose())
                    .subscribe(new CommonObserver<PayCodeModel>() {
                        @Override
                        public void onSuccess(PayCodeModel data) {
                            if (mView != null) {
                                mView.getPayCodeSuc(data);
                            }
                        }

                        @Override
                        public void onError(ResponseModel data) {
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                        }
                    });
        }
    }

    void checkPayPasswoed(String password, final PaymentCodeFragment fragment) {
        if (mView == null) return;
        mView.createAndShowDialog();
        RepositoryFactory.getRemotePayRepository().checkPayPassword(password)
                .compose(RxScheduler.<ResponseModel<String>>toMain())
                .as(mView.<ResponseModel<String>>bindAutoDispose())
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onSuccess(String data) {
                        if (mView != null) {
                            mView.destoryAndDismissDialog();
                        }
                        setPayCode("1");
                        fragment.dismiss();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null)
                            mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null)
                            mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }
                });

    }
}
