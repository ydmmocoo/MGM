package com.fjx.mg.main.scan.paymentcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.web.CommonWebActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.LogTUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.utils.StringUtil;
import com.library.common.view.editdialog.PayPwdView;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.models.PayCodeModel;
import com.library.repository.repository.RepositoryFactory;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Author    by hanlz
 * Date      on 2020/4/2.
 * Description：付款码页面--包含条形码和二维码
 */
public class PaymentCodeActivity extends BaseMvpActivity<PaymentCodePresenter> implements PaymentCodeConstract.View {

    @BindView(R.id.tvPaymentNum)
    TextView mTvPaymentNum;
    @BindView(R.id.ivBarCode)
    ImageView mIvBarCode;
    @BindView(R.id.ivQrCode)
    ImageView mIvQrCode;
    @BindView(R.id.clOpen)
    ConstraintLayout mClOpen;
    @BindView(R.id.clClose)
    ConstraintLayout mClClose;
    private TextView mTvPayCodeSwitch;
    ImageView mIvSwitch;
    private String mContents;//条形码数字
    private String isUsePayCode;
    private volatile PaymentCodeHandler mHandler = new PaymentCodeHandler();
    private PaymentCodeRunnable mRunnable;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, PaymentCodeActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_payment;
    }

    @Override
    protected PaymentCodePresenter createPresenter() {
        return new PaymentCodePresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        ToolBarManager with = ToolBarManager.with(this);

        mIvSwitch = with.setRightImage(R.drawable.group_chat_settings, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayCodeStatusDialog();
            }
        });
        with.setNavigationIcon(R.drawable.iv_back)
                .setTitle(getString(R.string.payment_code), R.color.white)
                .setBackgroundColor(R.color.colorAccent);
        setStatus();
    }

    private void setStatus() {
        isUsePayCode = UserCenter.getUserInfo().getIsUsePayCode();
        if (StringUtil.isNotEmpty(isUsePayCode)) {
            if (StringUtil.equals("1", isUsePayCode)) {
                //开启
                mClOpen.setVisibility(View.VISIBLE);
                mClClose.setVisibility(View.GONE);
                mIvSwitch.setVisibility(View.VISIBLE);
                mPresenter.getPayCode();
            } else {
                //关闭
                mClOpen.setVisibility(View.GONE);
                mClClose.setVisibility(View.VISIBLE);
                mIvSwitch.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 付款码控制dialog
     */
    private void showPayCodeStatusDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_pay_code_status, null);
        final DialogUtil.Builder builder = new DialogUtil.Builder(this).create(R.style.ActionSheetDialogStyle)
                .setGravity(DialogUtil.BOTTOM);
        view.findViewById(R.id.tvInstruction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dimiss();
                //跳转收款码使用说明 http://139.9.38.218/invite/paycodepro
                StringBuilder sb = new StringBuilder();
                sb.append(Constant.HOST);
                sb.append("invite/paycodepro?l=");
                sb.append(RepositoryFactory.getLocalRepository().getLangugeType());
                CommonWebActivity.Options options = new CommonWebActivity.Options();
//                options.setTitle(getString(R.string.payment_code_instructions));
                options.setLoadUrl(sb.toString());
                startActivity(CommonWebActivity.newInstance(getCurActivity(), JsonUtil.moderToString(options)));
            }
        });
        view.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dimiss();
            }
        });
        mTvPayCodeSwitch = view.findViewById(R.id.tvPayCodeSwitch);
//        try {
//            String isUsePayCode = UserCenter.getUserInfo().getIsUsePayCode();
//            if (StringUtil.isNotEmpty(isUsePayCode)) {
//                if (StringUtil.equals("1", isUsePayCode)) {
//                    //开启
//                    mTvPayCodeSwitch.setText(getString(R.string.temporarily_closed));
//                } else {
//                    //关闭
//                    mTvPayCodeSwitch.setText("开启使用");
//                }
//            }
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
        mTvPayCodeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dimiss();
                try {
                    LogTUtil.d("", JsonUtil.moderToString(UserCenter.getUserInfo()));
                    String isUsePayCode = UserCenter.getUserInfo().getIsUsePayCode();
                    if (StringUtil.isNotEmpty(isUsePayCode)) {
                        if (StringUtil.equals("1", isUsePayCode)) {
                            //开启
                            mPresenter.setPayCode("2");
                        } else {
                            //关闭
                            mPresenter.setPayCode("1");
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }


            }
        });
        builder.setContentView(view);
        builder.show();
    }

    @OnClick({R.id.ivBarCode, R.id.tvPaymentNum, R.id.tvStartPaymentCode})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivBarCode:
            case R.id.tvPaymentNum://展示全屏条形码
                startActivity(BarCodeActivity.newIntent(getCurContext(), mContents));
                break;
            case R.id.tvStartPaymentCode://开启付款码
                final PaymentCodeFragment fragment = new PaymentCodeFragment();
                fragment.setPaySuccessCallBack(new PayPwdView.InputCallBack() {
                    @Override
                    public void onInputFinish(String result) {
                        String payPwd = StringUtil.getPassword(result);
                        mPresenter.checkPayPasswoed(payPwd, fragment);
                    }
                });
                fragment.show(getSupportFragmentManager(), "PaymentCodeFragment");
                break;
            default:
                break;
        }
    }

    @Override
    public void showBarCodeBitmap(Bitmap bitmap) {
        mIvBarCode.setImageBitmap(bitmap);
    }

    @Override
    public void showQRCodeBitmap(Bitmap bitmap) {
        mIvQrCode.setImageBitmap(bitmap);
    }

    @Override
    public void setPayCodeStutasSuc() {
        setStatus();
    }

    @Override
    public void getPayCodeSuc(PayCodeModel data) {
        mPresenter.createBarCode(this, data.getBarCode());
        mPresenter.createQRCode(data.getQrCode());
        mContents = data.getBarCode();
        String isUsePayCode = UserCenter.getUserInfo().getIsUsePayCode();
        if (StringUtil.isNotEmpty(isUsePayCode)) {
            if (StringUtil.equals("1", isUsePayCode)) {
                //二维码更新频率，单位秒
                String expTime = data.getExpTime();
                if (mRunnable == null) {
                    LogTUtil.d("", "mRunnable");
                    mRunnable = new PaymentCodeRunnable(Integer.valueOf(expTime));
                    mHandler.post(mRunnable);
                }
            } else {
                if (mHandler != null) {
                    mHandler.removeCallbacks(mRunnable);
                }
            }
        }

    }

    private class PaymentCodeRunnable implements Runnable {
        int interval;

        public PaymentCodeRunnable(int interval) {
            this.interval = interval;

        }

        @Override
        public void run() {
            mHandler.postDelayed(mRunnable, interval * 1000);
            mPresenter.getPayCode2();
        }
    }

    private class PaymentCodeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacks(null);
        }
        super.onDestroy();
    }
}
