package com.fjx.mg.mgmpay;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fjx.mg.R;
import com.fjx.mg.setting.password.pay.ModifyPayPwdActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.fingerprint.FingerprintIdentify;
import com.library.common.fingerprint.base.BaseFingerprint;
import com.library.common.utils.CommonToast;
import com.library.common.view.editdialog.PayFragment;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.model.FingerprintModel;
import com.library.repository.models.PayByBalanceModel;
import com.library.repository.models.PayCheckOrderModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.Map;

/**
 * Author    by hanlz
 * Date      on 2020/3/3.
 * Description：
 */
public class MGMpayPresenter extends MGMPayContact.Presenter {

    public MGMpayPresenter(MGMPayContact.View view) {
        super(view);
    }


    @Override
    void requestPayByBalance(String appId, String prepayId, String appKey) {
        RepositoryFactory.getRemoteMGMPayApi().payByBalance(appId, prepayId, appKey)
                .compose(RxScheduler.<ResponseModel<PayByBalanceModel>>toMain())
                .as(mView.<ResponseModel<PayByBalanceModel>>bindAutoDispose())
                .subscribe(new CommonObserver<PayByBalanceModel>() {
                    @Override
                    public void onSuccess(PayByBalanceModel data) {
                        if (mView != null) {
                            mView.responsePayByBalanceSuc(data);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.responsePayByBalanceFail(data);
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (mView != null) {
                            mView.noLogin();
                        }
                    }
                });
    }

    @Override
    void requestCheckOrder(String orderString) {
        if (!UserCenter.hasLogin()) {
            Log.d("netLog", "未登录");
            if (mView != null) {
                mView.noLogin();
            }
        }
        RepositoryFactory.getRemoteMGMPayApi().checkOrder(orderString)
                .compose(RxScheduler.<ResponseModel<PayCheckOrderModel>>toMain())
                .as(mView.<ResponseModel<PayCheckOrderModel>>bindAutoDispose())
                .subscribe(new CommonObserver<PayCheckOrderModel>() {
                    @Override
                    public void onSuccess(PayCheckOrderModel data) {
                        if (mView != null) {
                            mView.showView(data);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null) {
                            mView.responsePayByBalanceFail(data);
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (mView != null) {
                            mView.noLogin();
                        }
                    }
                });
    }

    public void selectPswDialog(Map<String, String> map) {
        boolean isFingerEnable = isFingerEnable();
        if (isFingerEnable) {
            showFingerprintDialog(map);
        } else {
            showPayPasswordDiaog();
        }
    }

    private void showPayPasswordDiaog() {
        int isSet = UserCenter.getUserInfo().getIsSetPayPsw();
        if (isSet != 1) {
            showSetPayPwdDialog();
            return;
        }
        if (mView != null) {
            mView.showPswDialog();
        }
    }

    MaterialDialog fingerDialog;

    /**
     * 指纹识别认证
     */
    private void showFingerprintDialog(Map<String, String> map) {
        fingerDialog = new MaterialDialog.Builder(mView.getCurActivity())
                .customView(R.layout.dialog_finger, true)
                .backgroundColor(ContextCompat.getColor(mView.getCurContext(), R.color.trans))
                .build();
        fingerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = fingerDialog.getCustomView();
        if (view == null) return;
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        TextView tvPassword = view.findViewById(R.id.tvPassword);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        String price = map.get(IntentConstants.PRICE);
        tvPrice.setText(price);
        tvPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerDialog.dismiss();
                showPayPasswordDiaog();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerDialog.dismiss();
            }
        });

        initFingerpring(map);
        fingerDialog.show();
    }

    private void initFingerpring(final Map<String, String> map) {
        FingerprintIdentify mFingerprintIdentify = new FingerprintIdentify(mView.getCurContext().getApplicationContext());
        mFingerprintIdentify.setSupportAndroidL(true);
        mFingerprintIdentify.setExceptionListener(new BaseFingerprint.ExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                Log.d("FingerprintIdentify", exception.getLocalizedMessage());
            }
        });
        mFingerprintIdentify.init();
        mFingerprintIdentify.startIdentify(10, new BaseFingerprint.IdentifyListener() {
            @Override
            public void onSucceed() {
                // 验证成功，自动结束指纹识别
                CommonToast.toast(mView.getCurContext().getString(R.string.auth_access));
                if (fingerDialog != null) fingerDialog.dismiss();
                fingerDialog.dismiss();
                String appId = map.get(IntentConstants.APPID);
                String appKey = map.get(IntentConstants.APPKEY);
                String prepayId = map.get(IntentConstants.PREPAYID);
                requestPayByBalance(appId, prepayId, appKey);

            }

            @Override
            public void onNotMatch(int availableTimes) {
                // 指纹不匹配，并返回可用剩余次数并自动继续验证
                CommonToast.toast(mView.getCurContext().getString(R.string.auth_no_access));
            }

            @Override
            public void onFailed(boolean isDeviceLocked) {
                // 错误次数达到上限或者API报错停止了验证，自动结束指纹识别
                CommonToast.toast(mView.getCurContext().getString(R.string.busy_try_later));
                showPayPasswordDiaog();
            }

            @Override
            public void onStartFailedByDeviceLocked() {
                // 第一次调用startIdentify失败，因为设备被暂时锁定
                CommonToast.toast(mView.getCurContext().getString(R.string.busy_try_later));
                showPayPasswordDiaog();
            }
        });
    }

    private void showSetPayPwdDialog() {

        MaterialDialog dialog = new MaterialDialog.Builder(mView.getCurActivity())
                .title(R.string.attention)
                .content(R.string.hint_pay_password)
                .cancelable(false)
                .positiveText(R.string.go_set)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mView.getCurContext().startActivity(ModifyPayPwdActivity.newInstance(mView.getCurContext()));
                        dialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .negativeText(R.string.cancel).build();

        dialog.show();
    }

    private boolean isFingerEnable() {
        UserInfoModel infoModel = UserCenter.getUserInfo();
        if (infoModel == null) return false;
        String uid = infoModel.getUId();
        FingerprintModel model = DBDaoFactory.getFingerprintDao().queryModel(uid);
        if (model == null) return false;
        return model.getFingerEnable();
    }

    public void checkPayPasswoed(final String password, final PayFragment fragment, final Map<String, String> map) {
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
                        fragment.dismiss();
                        String appId = map.get(IntentConstants.APPID);
                        String appKey = map.get(IntentConstants.APPKEY);
                        String prepayId = map.get(IntentConstants.PREPAYID);
                        requestPayByBalance(appId, prepayId, appKey);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null)
                            mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }

                    @Override
                    public void onUserFailed(ResponseModel data) {
                        super.onUserFailed(data);
                        //9990用户登录过期
                        CommonToast.toast(data.getMsg());
                    }
                });

    }

    public void getUserProfile() {
        if (!UserCenter.hasLogin()) {
            mView.noLogin();
            return;
        }
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteAccountRepository().getUserProfile()
                .compose(RxScheduler.<ResponseModel<UserInfoModel>>toMain())
                .as(mView.<ResponseModel<UserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<UserInfoModel>() {
                    @Override
                    public void onSuccess(UserInfoModel data) {
                        UserInfoModel model = UserCenter.getUserInfo();
                        data.setToken(model.getToken());
                        data.setUseRig(model.getUseRig());
                        UserCenter.saveUserInfo(data);
                        mView.showUserInfo(data);
                        mView.destoryAndDismissDialog();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.showUserInfoFail(data);
                            mView.destoryAndDismissDialog();
                        }
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (mView != null) {
                            mView.noLogin();
                        }
                    }
                });

    }
}
