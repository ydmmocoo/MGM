package com.fjx.mg.me.safe_center.fingerprint;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fjx.mg.R;
import com.fjx.mg.me.safe_center.AuthType;
import com.fjx.mg.me.safe_center.check.identity_password.IdentityPasswordAuthActivity;
import com.fjx.mg.me.safe_center.check.mobile_password.MobilePasswordAuthActivity;
import com.fjx.mg.me.safe_center.check.password_question.PasswordQuestionActivity;
import com.library.repository.data.UserCenter;
import com.library.repository.models.UserInfoModel;

public class FingerprintPresenter extends FingerprintContract.Presenter {

    private MaterialDialog gestureDialog;

    FingerprintPresenter(FingerprintContract.View view) {
        super(view);
    }

    void showAuthDialog() {
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
                mView.getCurContext().startActivity(PasswordQuestionActivity.newInstance(mView.getCurContext(), AuthType.FINGERPRINT));
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestureDialog.dismiss();
                mView.getCurContext().startActivity(IdentityPasswordAuthActivity.newInstance(mView.getCurContext(), AuthType.FINGERPRINT));
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gestureDialog.dismiss();
                mView.getCurContext().startActivity(MobilePasswordAuthActivity.newInstance(mView.getCurContext(), AuthType.FINGERPRINT));
            }
        });
        textView1.setVisibility(setSecurityIssues ? View.VISIBLE : View.GONE);
        textView2.setVisibility(UserCenter.getUserInfo().isRealName() == 1 ? View.VISIBLE : View.GONE);
        textView3.setVisibility(View.VISIBLE);
        ivw.setVisibility(View.VISIBLE);
        ivw1.setVisibility(UserCenter.getUserInfo().isRealName() == 1 ? View.VISIBLE : View.GONE);
        textView1.setText(mView.getCurContext().getString(R.string.safe_question).concat("+").concat(mView.getCurContext().getString(R.string.login_password)));
        textView2.setText(mView.getCurContext().getString(R.string.identity_auth).concat("+").concat(mView.getCurContext().getString(R.string.login_password)));
        textView3.setText(mView.getCurContext().getString(R.string.mobile_code).concat("+").concat(mView.getCurContext().getString(R.string.login_password)));

        gestureDialog.show();
    }

//    void showAuthDialog() {
//        UserInfoModel data = UserCenter.getUserInfo();
//        final boolean setSecurityIssues = data.isSetSecurityIssues();//安全问题
//        gestureDialog = new MaterialDialog.Builder(mView.getCurActivity())
//                .customView(R.layout.dialog_auth, true)
//                .backgroundColor(ContextCompat.getColor(mView.getCurContext(), R.color.trans))
//                .build();
//        gestureDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        View view = gestureDialog.getCustomView();
//        if (view == null) return;
////        TextView textEmail = view.findViewById(R.id.textEmail);
//        TextView textView1 = view.findViewById(R.id.textView1);
//        TextView textView2 = view.findViewById(R.id.textView2);
//        textView1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gestureDialog.dismiss();
//
//                if (!setSecurityIssues && UserCenter.getUserInfo().isRealName() != 1) {//安全问题还有实名都不存在，通过验证码+登录密码
//                    mView.getCurContext().startActivity(MobilePasswordAuthActivity.newInstance(mView.getCurContext(), AuthType.FINGERPRINT));
//                } else {
//                    mView.getCurContext().startActivity(MobileQuestionAuthActivity.newInstance(mView.getCurContext(), AuthType.FINGERPRINT));
//                }
//
//
//            }
//        });
//        textView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gestureDialog.dismiss();
//                mView.getCurContext().startActivity(MobilePasswordAuthActivity.newInstance(mView.getCurContext(), AuthType.FINGERPRINT));
//            }
//        });
//
//        textView1.setVisibility(setSecurityIssues ? View.VISIBLE : View.GONE);
//        textView2.setVisibility(UserCenter.getUserInfo().isRealName() == 1 ? View.VISIBLE : View.GONE);
//        textView1.setText(mView.getCurContext().getString(R.string.mobile_code).concat("+").concat(mView.getCurContext().getString(R.string.safe_question)));
//        textView2.setText(mView.getCurContext().getString(R.string.mobile_code).concat("+").concat(mView.getCurContext().getString(R.string.login_password)));
//        if (!setSecurityIssues && UserCenter.getUserInfo().isRealName() != 1) {//安全问题还有实名都不存在，通过验证码更改手机
//            textView1.setVisibility(View.VISIBLE);
//            textView1.setText(mView.getCurContext().getString(R.string.mobile_code));
//        }
//        gestureDialog.show();
//    }
}
