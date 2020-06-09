package com.fjx.mg.me.safe_center;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.me.safe_center.auth.AuthCenterActivity;
import com.fjx.mg.me.safe_center.fingerprint.FingerprintActivity;
import com.fjx.mg.me.safe_center.question.QuestionSetActivity;
import com.fjx.mg.setting.certification.CertificationActivity;
import com.fjx.mg.setting.certification.CertificationInfoActivity;
import com.fjx.mg.setting.password.login.ModifyLoginPwdActivity;
import com.fjx.mg.setting.password.pay.ModifyPayPwdActivity;
import com.library.common.base.BaseActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.StatusBarManager;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.model.FingerprintModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 安全中心
 */
public class SafeCenterActivity extends BaseActivity {

    @BindView(R.id.tvCertPerson)
    TextView tvCertPerson;
    @BindView(R.id.tvAuthProblem)
    TextView tvAuthProblem;
    @BindView(R.id.tvPayPassword)
    TextView tvPayPassword;
    @BindView(R.id.tvBindMobile)
    TextView tvBindMobile;
    @BindView(R.id.tvLoginPassword)
    TextView tvLoginPassword;
    @BindView(R.id.tvFingerprint)
    TextView tvFingerprint;
    @BindView(R.id.tvGesture)
    TextView tvGesture;
    @BindView(R.id.tvSafeScore)
    TextView tvSafeScore;
    @BindView(R.id.tvScoreStatus)
    TextView tvScoreStatus;
    @BindView(R.id.llCertification)
    LinearLayout llCertification;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, SafeCenterActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        mCommonStatusBarEnable = false;
        return R.layout.ac_save_center;

    }

    @Override
    protected void initView() {
        Log.d("initView", StringUtil.getDeviceId());
        StatusBarManager.transparentNavigationBar(this);
        GradientDrawableHelper.whit(tvScoreStatus).setColor(R.color.white).setCornerRadius(50);
        ToolBarManager.with(this).setBackgroundColor(R.color.trans).setTitle(getString(R.string.safe_center), R.color.white)
                .setNavigationIcon(R.drawable.iv_back);


    }

    private void showUserInfo() {
        UserInfoModel data = UserCenter.getUserInfo();

        int colorGray = ContextCompat.getColor(getCurContext(), R.color.textColorGray1);
        int textColor = ContextCompat.getColor(getCurContext(), R.color.textColor);

        if (data.isRealName() == 0) {
            tvCertPerson.setText(getString(R.string.unCertified));
            tvCertPerson.setTextColor(textColor);
        } else if (data.isRealName() == 1) {
            tvCertPerson.setText(getString(R.string.Certified));
            tvCertPerson.setTextColor(colorGray);
        } else {
            tvCertPerson.setText(getString(R.string.certifiying));
            llCertification.setClickable(false);
            tvCertPerson.setTextColor(textColor);
        }
        tvAuthProblem.setText(data.isSetSecurityIssues() ? getString(R.string.had_setting) : getString(R.string.unsetting));
        tvAuthProblem.setTextColor(data.isSetSecurityIssues() ? colorGray : textColor);

        tvPayPassword.setText(data.hasSetPayPwd() ? getString(R.string.had_setting) : getString(R.string.unsetting));
        tvPayPassword.setTextColor(data.hasSetPayPwd() ? colorGray : textColor);

        tvBindMobile.setText(getString(R.string.had_setting));
        tvBindMobile.setTextColor(colorGray);

        tvLoginPassword.setText(data.isSetLoginPsw() ? getString(R.string.had_setting) : getString(R.string.unsetting));
        tvLoginPassword.setTextColor(data.isSetLoginPsw() ? colorGray : textColor);


        tvGesture.setText(data.hasGesture() ? getString(R.string.had_setting) : getString(R.string.unsetting));
        tvGesture.setTextColor(data.hasGesture() ? colorGray : textColor);


        tvFingerprint.setText(isFingerEnable() ? getString(R.string.had_setting) : getString(R.string.unsetting));
        tvFingerprint.setTextColor(isFingerEnable() ? colorGray : textColor);

        String scoreStr = data.getSecurityLevelScore();
        int score = 0;
        if (!TextUtils.isEmpty(scoreStr)) {
            score = Integer.parseInt(scoreStr);
            if (isFingerEnable()) score += 5;

        }
//        90 <= 高 <＝100
//        70 <= 中 < 90
//        低 < 70
        tvSafeScore.setText(String.valueOf(score));
        if (score >= 90) {
            tvScoreStatus.setText(getString(R.string.safe_high));
            tvScoreStatus.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorGreen));
            tvSafeScore.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorGreen));
        } else if (score >= 70) {
            tvScoreStatus.setText(getString(R.string.safe_midle));
            tvScoreStatus.setTextColor(ContextCompat.getColor(getCurContext(), R.color.textColorYellow));
            tvSafeScore.setTextColor(ContextCompat.getColor(getCurContext(), R.color.textColorYellow));
        } else {
            tvScoreStatus.setText(getString(R.string.safe_low));
            tvScoreStatus.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
            tvSafeScore.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));

        }
    }


    @OnClick({R.id.llCertification, R.id.llAuthProblem, R.id.llPayPassword, R.id.llBindMobile, R.id.llLoginPassword, R.id.llFingerprint, R.id.llGesture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llCertification:
                if (UserCenter.getUserInfo().isRealName() == 1) {
                    startActivity(CertificationInfoActivity.newInstance(getCurContext()));
                } else {
                    startActivityForResult(CertificationActivity.newInstance(getCurContext()), 111);
                }
                break;
            case R.id.llAuthProblem://安全问题
                if (UserCenter.getUserInfo().isSetSecurityIssues()) {
                    startActivity(AuthCenterActivity.newInstance(getCurContext(), AuthType.SECURITY_ISSUES));
                } else {
                    startActivityForResult(QuestionSetActivity.newInstance(getCurContext()), 111);
                }
                break;
            case R.id.llPayPassword://支付密码
                if (UserCenter.getUserInfo().getIsSetPayPsw() == 1) {
                    startActivity(AuthCenterActivity.newInstance(getCurContext(), AuthType.PAY_PASSWORD));
                } else {
                    startActivityForResult(ModifyPayPwdActivity.newInstance(getCurContext()), 111);
                }
                break;
            case R.id.llBindMobile://手机绑定
                startActivity(AuthCenterActivity.newInstance(getCurContext(), AuthType.BIND_MOBILE));
                break;
            case R.id.llLoginPassword://登陆密码
                if (UserCenter.getUserInfo().isSetLoginPsw()) {
                    startActivity(AuthCenterActivity.newInstance(getCurContext(), AuthType.LOGIN_PASSWORD));
                } else {
                    startActivityForResult(ModifyLoginPwdActivity.newInstance(getCurContext()), 111);
                }
                break;
            case R.id.llFingerprint://指纹密码
                startActivity(FingerprintActivity.newInstance(getCurContext()));
                break;
            case R.id.llGesture://手势密码
                startActivity(AuthCenterActivity.newInstance(getCurContext(), AuthType.GESTURE));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showUserInfo();
        getUserProfile();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == resultCode) {
//            getUserProfile();
//        }
//    }


    void getUserProfile() {
        if (!UserCenter.hasLogin()) return;
        showLoading();
        RepositoryFactory.getRemoteAccountRepository().getUserProfile()
                .compose(RxScheduler.<ResponseModel<UserInfoModel>>toMain())
                .as(this.<ResponseModel<UserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<UserInfoModel>() {
                    @Override
                    public void onSuccess(UserInfoModel data) {
                        hideLoading();
                        UserInfoModel model = UserCenter.getUserInfo();
                        data.setToken(model.getToken());
                        data.setUseRig(model.getUseRig());
                        UserCenter.saveUserInfo(data);
                        showUserInfo();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    private boolean isFingerEnable() {
        UserInfoModel infoModel = UserCenter.getUserInfo();
        if (infoModel == null) return false;
        String uid = infoModel.getUId();
        FingerprintModel model = DBDaoFactory.getFingerprintDao().queryModel(uid);
        if (model == null) return false;
        return model.getFingerEnable();
    }
}
