package com.fjx.mg.me.safe_center.auth;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fjx.mg.R;
import com.fjx.mg.me.safe_center.AuthType;
import com.fjx.mg.me.safe_center.check.identity_mobile.IdentityMobileAuthActivity;
import com.fjx.mg.me.safe_center.check.identity_password.IdentityPasswordAuthActivity;
import com.fjx.mg.me.safe_center.check.mobile_password.MobilePasswordAuthActivity;
import com.fjx.mg.me.safe_center.check.mobile_question.MobileQuestionAuthActivity;
import com.fjx.mg.me.safe_center.check.password_question.PasswordQuestionActivity;
import com.fjx.mg.me.safe_center.lock.GestureLockActivity;
import com.fjx.mg.me.safe_center.question.QuestionSetActivity;
import com.fjx.mg.setting.certification.CertificationActivity;
import com.fjx.mg.setting.password.login.ModifyLoginPwdActivity;
import com.fjx.mg.setting.password.pay.ModifyPayPwdActivity;
import com.library.common.utils.CommonToast;
import com.library.repository.data.UserCenter;
import com.library.repository.models.UserInfoModel;

/**
 * 安全问题/验证码验证
 */
public class AuthCenterPresenter extends AuthCenterContract.Presenter {

    private MaterialDialog gestureDialog;

    AuthCenterPresenter(AuthCenterContract.View view) {
        super(view);
    }

    @Override
    boolean canEdit() {
        UserInfoModel model = UserCenter.getUserInfo();
        if (!isRealName()) return false;
        if (!isSetSecurityIssues()) return false;
        return true;
    }

    @Override
    void change(int type) {
        switch (type) {
            case AuthType.GESTURE:
                mView.getCurContext().startActivity(GestureLockActivity.newInstance(mView.getCurContext(), GestureLockActivity.TYPE_CHANGE));
                break;
            case AuthType.SECURITY_ISSUES:
                mView.getCurContext().startActivity(QuestionSetActivity.newInstance(mView.getCurContext()));
                break;
            case AuthType.LOGIN_PASSWORD:
                mView.getCurContext().startActivity(ModifyLoginPwdActivity.newInstance(mView.getCurContext()));
                break;
            case AuthType.PAY_PASSWORD:
                mView.getCurContext().startActivity(ModifyPayPwdActivity.newInstance(mView.getCurContext()));
                break;
        }

//        showAuthDialog(type, false);
    }

    @Override
    void reset(int type) {
        showAuthDialog(type, true);
    }

    @Override
    void showAuthDialog(final int type, final boolean isReset) {
        UserInfoModel data = UserCenter.getUserInfo();
        final boolean setSecurityIssues = data.isSetSecurityIssues();//安全问题
        gestureDialog = new MaterialDialog.Builder(mView.getCurActivity())
                .customView(R.layout.dialog_auth, true)
                .backgroundColor(ContextCompat.getColor(mView.getCurContext(), R.color.trans))
                .build();
        gestureDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = gestureDialog.getCustomView();
        if (view == null) return;
//        TextView textEmail = view.findViewById(R.id.textEmail);
        TextView textView1 = view.findViewById(R.id.textView1);
        TextView textView2 = view.findViewById(R.id.textView2);
        TextView textView3 = view.findViewById(R.id.textView3);//多加选项
        View ivw = view.findViewById(R.id.view);//多加选项下面的线
        View ivw1 = view.findViewById(R.id.view1);//textView1下面的线
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestureDialog.dismiss();

                switch (type) {
                    case AuthType.GESTURE:
                    case AuthType.PAY_PASSWORD:
                    case AuthType.BIND_MOBILE:
                        mView.getCurContext().startActivity(PasswordQuestionActivity.newInstance(mView.getCurContext(), type));
                        break;
                    case AuthType.SECURITY_ISSUES:
                        mView.getCurContext().startActivity(MobilePasswordAuthActivity.newInstance(mView.getCurContext(), type));
                        break;
                    case AuthType.LOGIN_PASSWORD:
                        mView.getCurContext().startActivity(IdentityMobileAuthActivity.newInstance(mView.getCurContext(), type));
                        break;

                }
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestureDialog.dismiss();
                switch (type) {
                    case AuthType.GESTURE:
                    case AuthType.PAY_PASSWORD:
                    case AuthType.BIND_MOBILE:
                    case AuthType.SECURITY_ISSUES:
                        mView.getCurContext().startActivity(IdentityPasswordAuthActivity.newInstance(mView.getCurContext(), type));
                        break;
                    case AuthType.LOGIN_PASSWORD:
                        mView.getCurContext().startActivity(MobileQuestionAuthActivity.newInstance(mView.getCurContext(), type));
                        break;
                }
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestureDialog.dismiss();
                switch (type) {
                    case AuthType.GESTURE:
                    case AuthType.PAY_PASSWORD:
                    case AuthType.SECURITY_ISSUES:
                        mView.getCurContext().startActivity(MobilePasswordAuthActivity.newInstance(mView.getCurContext(), type));
                        break;
                    case AuthType.BIND_MOBILE:
                    case AuthType.LOGIN_PASSWORD:
                        mView.getCurContext().startActivity(MobilePasswordAuthActivity.newInstance(mView.getCurContext(), type, true));
                        break;
                }
            }
        });

        switch (type) {
            case AuthType.GESTURE://手势密码
            case AuthType.PAY_PASSWORD://支付密码
                textView1.setVisibility(setSecurityIssues ? View.VISIBLE : View.GONE);
                textView2.setVisibility(UserCenter.getUserInfo().isRealName() == 1 ? View.VISIBLE : View.GONE);
                textView3.setVisibility(View.VISIBLE);
                ivw.setVisibility(View.VISIBLE);
                ivw1.setVisibility(UserCenter.getUserInfo().isRealName() == 1 ? View.VISIBLE : View.GONE);
                textView1.setText(mView.getCurContext().getString(R.string.safe_question).concat("+").concat(mView.getCurContext().getString(R.string.login_password)));
                textView2.setText(mView.getCurContext().getString(R.string.identity_auth).concat("+").concat(mView.getCurContext().getString(R.string.login_password)));
                textView3.setText(mView.getCurContext().getString(R.string.mobile_code).concat("+").concat(mView.getCurContext().getString(R.string.login_password)));
                break;
            case AuthType.BIND_MOBILE://换绑手机
                textView1.setVisibility(setSecurityIssues ? View.VISIBLE : View.GONE);
                textView2.setVisibility(UserCenter.getUserInfo().isRealName() == 1 ? View.VISIBLE : View.GONE);
                textView3.setVisibility(UserCenter.getUserInfo().isRealName() != 1 && !setSecurityIssues ? View.VISIBLE : View.GONE);
                ivw.setVisibility(UserCenter.getUserInfo().isRealName() != 1 && !setSecurityIssues ? View.VISIBLE : View.GONE);

                ivw1.setVisibility(UserCenter.getUserInfo().isRealName() == 1 ? View.VISIBLE : View.GONE);
                textView1.setText(mView.getCurContext().getString(R.string.safe_question).concat("+").concat(mView.getCurContext().getString(R.string.login_password)));
                textView2.setText(mView.getCurContext().getString(R.string.identity_auth).concat("+").concat(mView.getCurContext().getString(R.string.login_password)));
                textView3.setText(mView.getCurContext().getString(R.string.login_password));
                break;
            case AuthType.SECURITY_ISSUES://设置安全问题
                textView1.setText(mView.getCurContext().getString(R.string.mobile_code).concat("+").concat(mView.getCurContext().getString(R.string.login_password)));
                textView2.setText(mView.getCurContext().getString(R.string.identity_auth).concat("+").concat(mView.getCurContext().getString(R.string.login_password)));
                textView2.setVisibility(UserCenter.getUserInfo().isRealName() == 1 ? View.VISIBLE : View.GONE);//没有实名隐藏通过身份证设置安全问题
                ivw1.setVisibility(UserCenter.getUserInfo().isRealName() == 1 ? View.VISIBLE : View.GONE);
                break;

            case AuthType.LOGIN_PASSWORD://密码修改或者重置
                textView1.setVisibility(UserCenter.getUserInfo().isRealName() == 1 ? View.VISIBLE : View.GONE);
                ivw1.setVisibility(UserCenter.getUserInfo().isRealName() == 1 ? View.VISIBLE : View.GONE);
                textView2.setVisibility(setSecurityIssues ? View.VISIBLE : View.GONE);
                if (UserCenter.getUserInfo().isRealName() != 1 && !setSecurityIssues) {
                    textView3.setVisibility(View.VISIBLE);
                    ivw.setVisibility(View.VISIBLE);
                }
                textView1.setText(mView.getCurContext().getString(R.string.identity_auth).concat("+").concat(mView.getCurContext().getString(R.string.mobile_code)));
                textView2.setText(mView.getCurContext().getString(R.string.mobile_code).concat("+").concat(mView.getCurContext().getString(R.string.safe_question)));
                textView3.setText(mView.getCurContext().getString(R.string.mobile_code));
                break;
        }

        gestureDialog.show();
    }


    private boolean isRealName() {
        if (UserCenter.getUserInfo().isRealName() != 1) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_cer_first));
            mView.getCurActivity().startActivity(CertificationActivity.newInstance(mView.getCurContext()));
            return false;
        }
        return true;
    }


    private boolean isSetSecurityIssues() {
        if (!UserCenter.getUserInfo().isSetSecurityIssues()) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_quester_first));
            mView.getCurActivity().startActivity(QuestionSetActivity.newInstance(mView.getCurContext()));
            return false;
        }
        return true;
    }
}
