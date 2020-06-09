package com.fjx.mg.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.utils.ImageUtils;
import com.library.common.utils.CacheManager;
import com.library.common.utils.CommonToast;
import com.library.common.utils.ContextManager;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.ZXingUtils;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.PaymentCodeModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UserWithDrawalQrCodeDialog extends Dialog {

    private Bitmap bitmap;
    private PaymentCodeModel model;
    private Context mContext;
    private ImageView qrCode;//二维码展示图
    private TextView tvReset;//重新加载
    private ProgressBar mPbResetLoading;

    public UserWithDrawalQrCodeDialog(Context context, Bitmap bitmap, PaymentCodeModel model, int theme) {
        super(context, theme);
        this.bitmap = bitmap;
        this.model = model;
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_whitdrawal_qr_code);
        initUI();
    }


    @SuppressLint("SetTextI18n")
    private void initUI() {
        qrCode = findViewById(R.id.imgQrCodeWithdrawal);
        TextView tvAmount = findViewById(R.id.tvQrCodeAmount);//实付金额
        TextView withdrawalAmount = findViewById(R.id.tvQrCodeWithdrawalAmount);//提现金额
        TextView poundage = findViewById(R.id.tvQrCodePoundage);//手续费
        mPbResetLoading = findViewById(R.id.pbReset);
        tvReset = findViewById(R.id.tvReset);
        if (bitmap == null) {
            tvReset.setVisibility(View.VISIBLE);
        } else {
            tvReset.setVisibility(View.GONE);
        }
        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetBitmap();
            }
        });
        qrCode.setImageBitmap(bitmap);
        tvAmount.setText(model.getRealPrice() + ContextManager.getContext().getString(R.string.ar));
        withdrawalAmount.setText(ContextManager.getContext().getString(R.string.pay_money) + " " + model.getPrice() + ContextManager.getContext().getString(R.string.ar));
        poundage.setText(ContextManager.getContext().getString(R.string.order_poundage) + " -" + model.getServicePrice() + ContextManager.getContext().getString(R.string.ar));
    }

    private void resetBitmap() {
        mPbResetLoading.setVisibility(View.VISIBLE);
        RepositoryFactory.getRemoteAccountRepository().getQRCollectionCode(model.getPrice(), "1")//	1:表示代理点充值
                .compose(RxScheduler.<ResponseModel<PaymentCodeModel>>toMain())
                .subscribe(new CommonObserver<PaymentCodeModel>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onSuccess(final PaymentCodeModel data) {
                        tvReset.setVisibility(View.VISIBLE);
                        mPbResetLoading.setVisibility(View.GONE);
                        Observable.create(new ObservableOnSubscribe<Bitmap>() {
                            @Override
                            public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
//                                String uImg = UserCenter.getUserInfo().getUImg();
//                                Bitmap logo = ImageUtils.getBitMBitmap(uImg);
//                                Bitmap bt = CacheManager.getInstance(mContext).getBitmap(uImg);
//                                CacheManager.getInstance(mContext).putBitmap(uImg, logo);
                                int width = DimensionUtil.dip2px(500);
                                Bitmap bitmap = ZXingUtils.createQRImage(data.getPayCode(), width, width);
//                                Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(data.getPayCode(), width, Color.BLACK, bt != null ? bt : logo);
                                CacheManager.getInstance(mContext).putBitmap(data.getPayCode(), UserWithDrawalQrCodeDialog.this.bitmap);
                                e.onNext(bitmap);
                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Consumer<Bitmap>() {
                                    @Override
                                    public void accept(Bitmap bitmap) throws Exception {
                                        qrCode.setImageBitmap(bitmap);
                                    }
                                });
                    }


                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        tvReset.setVisibility(View.VISIBLE);
                        mPbResetLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        tvReset.setVisibility(View.VISIBLE);
                        mPbResetLoading.setVisibility(View.GONE);
                    }
                });
    }
}
