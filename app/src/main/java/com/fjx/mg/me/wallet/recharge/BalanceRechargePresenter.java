package com.fjx.mg.me.wallet.recharge;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.fjx.mg.R;
import com.fjx.mg.dialog.UserWithDrawalQrCodeDialog;
import com.fjx.mg.utils.ImageUtils;
import com.library.common.utils.CacheManager;
import com.library.common.utils.CommonToast;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ZXingUtils;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.BalanceModel;
import com.library.repository.models.ConvertModel;
import com.library.repository.models.PaymentCodeModel;
import com.library.repository.models.RechargeModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BalanceRechargePresenter extends BalanceRechargeContract.Presenter {


    private UserWithDrawalQrCodeDialog dialog;
    private WeakReference<BalanceRechargeContract.View> weakReference;

    BalanceRechargePresenter(BalanceRechargeContract.View view) {
        super(view);
        weakReference = new WeakReference<>(view);
    }

    @Override
    void QRCollectionCode(String price) {
        RepositoryFactory.getRemoteAccountRepository().getQRCollectionCode(price, "1")//	1:表示代理点充值
                .compose(RxScheduler.<ResponseModel<PaymentCodeModel>>toMain())
                .as(mView.<ResponseModel<PaymentCodeModel>>bindAutoDispose())
                .subscribe(new CommonObserver<PaymentCodeModel>() {
                    @Override
                    public void onSuccess(PaymentCodeModel data) {
                        if (mView != null) {
                            mView.closeDialog();
                        }
                        getBitmap(data);
                    }


                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.closeDialog();
                            mView.responseFailed(data);
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.closeDialog();
                            mView.responseFailed(data);
                            CommonToast.toast(data.getMsg());
                        }
                    }
                });
    }

    @Override
    void TextWatch(final EditText amount) {
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    mView.ConvertSuccess("", "");
                } else if (Integer.valueOf(amount.getText().toString().trim()) < 1000) {
                    mView.ConvertSuccess("", "");
                } else {
                    convertMoney(amount.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    void playSound() {
        SoundPool soundPool;
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(1);
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        }
        //TODO 屏蔽支付成功语音
//        soundPool.load(mView.getCurContext(), R.raw.sc, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool1, int sampleId, int status) {
                soundPool1.play(1, 1, 1, 0, 0, 1);
            }
        });
    }

    @Override
    void dismiss() {
        dialog.dismiss();
    }

    @Override
    void Band(String clientId) {
        final BalanceRechargeContract.View view = weakReference.get() == null ? mView : weakReference.get();
        if (view == null) {
            return;
        }
        view.createAndShowDialog();
        RepositoryFactory.getRemoteAccountRepository().workBind(clientId)
                .compose(RxScheduler.toMain())
                .as(view.bindAutoDispose())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        view.destoryAndDismissDialog();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.hideLoading();
                    }
                });
    }

    private void convertMoney(String price) {//换算接口
        mView.ConvertSuccess(mView.getCurContext().getString(R.string.to_obtain), mView.getCurContext().getString(R.string.to_obtain));
        RepositoryFactory.getRemoteRepository().convert("5", price)
                .compose(RxScheduler.<ResponseModel<ConvertModel>>toMain())
                .as(mView.<ResponseModel<ConvertModel>>bindAutoDispose())
                .subscribe(new CommonObserver<ConvertModel>() {
                    @Override
                    public void onSuccess(ConvertModel data) {
                        String CNYPrice = data.getCNYPrice();
                        String servicePrice = data.getServicePrice();
                        String totalPrice = data.getTotalPrice();
                        String realPrice = data.getRealPrice();
                        mView.ConvertSuccess("-" + servicePrice + mView.getCurContext().getString(R.string.ar), realPrice + mView.getCurContext().getString(R.string.ar));
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getBitmap(final PaymentCodeModel data) {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
//                String uImg = UserCenter.getUserInfo().getUImg();
//                Bitmap logo = ImageUtils.getBitMBitmap(uImg);
//                int width = DimensionUtil.dip2px(500);
//                Bitmap bt = CacheManager.getInstance(mView.getCurContext()).getBitmap(uImg);
//                CacheManager.getInstance(mView.getCurContext()).putBitmap(uImg, logo);
//                Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(data.getPayCode(), width, Color.BLACK, bt != null ? bt : logo);
                int width = DimensionUtil.dip2px(500);
                Bitmap bitmap = ZXingUtils.createQRImage(data.getPayCode(), width, width);
                CacheManager.getInstance(mView.getCurContext()).putBitmap(data.getPayCode(), bitmap);
                e.onNext(bitmap);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        if (mView != null && bitmap != null) {
                            dialog = new UserWithDrawalQrCodeDialog(mView.getCurContext(), bitmap, data, R.style.Theme_Dialog_Scale);
                            dialog.show();
                            mView.responseSuccess();
                        }
                    }
                });
    }

    @Override
    void balancePackage() {

        mView.createAndShowDialog();
        RepositoryFactory.getRemoteAccountRepository().balancePackage()
                .compose(RxScheduler.<ResponseModel<List<RechargeModel>>>toMain())
                .as(mView.<ResponseModel<List<RechargeModel>>>bindAutoDispose())
                .subscribe(new CommonObserver<List<RechargeModel>>() {
                    @Override
                    public void onSuccess(List<RechargeModel> data) {
                        if (data == null) return;
                        data.get(0).setCheck(true);
                        mView.showDataList(data);
                        mView.destoryAndDismissDialog();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                    }
                });
    }

    @Override
    void getUserBalance() {
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteAccountRepository().getUserBalance()
                .compose(RxScheduler.<ResponseModel<BalanceModel>>toMain())
                .as(mView.<ResponseModel<BalanceModel>>bindAutoDispose())
                .subscribe(new CommonObserver<BalanceModel>() {
                    @Override
                    public void onSuccess(BalanceModel data) {
                        mView.getBalanceSuccess(data.getBalance());
                        mView.destoryAndDismissDialog();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                    }
                });
    }


}
