package com.fjx.mg.login.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.fjx.mg.R;
import com.fjx.mg.login.areacode.AreaCodeActivity;
import com.fjx.mg.login.bind.BindMobileActivity;
import com.fjx.mg.login.register.RegisterActivity;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.me.safe_center.lock.GestureLockActivity;
import com.fjx.mg.utils.SharedPreferencesUtils;
import com.fjx.mg.utils.ThirdUtils;
import com.fjx.mg.view.PhoneTextWatcher;
import com.library.common.base.BaseMvpActivity;
import com.library.common.callback.CActivityManager;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.AppUtil;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.NetCode;
import com.library.repository.data.UserCenter;
import com.library.repository.models.UserInfoModel;

import java.util.Stack;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页
 *
 * </p>
 * 密码登录
 */
public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.tvLogin)
    TextView mTvLogin;
    @BindView(R.id.etMobile)
    EditText mEtMobile;
    @BindView(R.id.iv_clear)
    ImageView mIvClear;
    @BindView(R.id.etPwd)
    EditText mEtPwd;
    @BindView(R.id.tvAreaCode)
    TextView mTvAreaCode;
    @BindView(R.id.ivLoginWx)
    ImageView mIvLoginWx;
    @BindView(R.id.ivAliLogin)
    ImageView mIvAliLogin;
    @BindView(R.id.tvVersion)
    TextView mTvVersion;
    @BindView(R.id.rlLoginHint)
    RelativeLayout mRlLoginHint;
    private boolean newMain;
    private boolean flag, isMgmPay;
    private PhoneTextWatcher mPhoneTextWatcher;
    Dialog dialog2;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    public static Intent mgnPay2Login(Context context, boolean isMGMPay) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(IntentConstants.MGMPAY, isMGMPay);
        return intent;
    }

    public static Intent newInstance(Context context, boolean newMain) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(IntentConstants.NEW_MAIN, newMain);
        return intent;
    }


    public static Intent newInstance(Context context, boolean newMain, boolean flag) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(IntentConstants.NEW_MAIN, newMain);
        intent.putExtra(IntentConstants.FLAG, flag);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_login;
    }

    @Override
    protected void initView() {
        String versionName = AppUtil.getVersionName();
        if (StringUtil.isNotEmpty(versionName)) {
            mTvVersion.setText("v".concat(versionName));
        }
        isMgmPay = getIntent().getBooleanExtra(IntentConstants.MGMPAY, false);
        mPhoneTextWatcher = new PhoneTextWatcher();
        mEtMobile.addTextChangedListener(mPhoneTextWatcher);
        UserInfoModel infoModel = UserCenter.getUserInfo();
        if (infoModel != null) {
            infoModel.setToken("");
            UserCenter.saveUserInfo(infoModel);
        }
        newMain = getIntent().getBooleanExtra(IntentConstants.NEW_MAIN, false);
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        GradientDrawableHelper.whit(mTvLogin).setColor(R.color.colorAccent).setCornerRadius(80);

        if (newMain) {
            CActivityManager.getAppManager().finishOthersActivity(LoginActivity.class);
        }
        flag = UserCenter.getOfflineStatus();
        if (flag) {
            UserCenter.savatOfflineStatus(false);
            AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this).setMessage(R.string.force_logout).setTitle(R.string.attention)
                    .setPositiveButton(R.string.relogin, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            NetCode.isShowGestureLockActivity = false;
                        }
                    }).create();
            dialog.setCancelable(false);
            dialog.show();
        }

        UserInfoModel model = UserCenter.getUserInfo();
        if (model != null) {
            mEtMobile.setText(model.getPhone());
            mTvAreaCode.setText("+" + model.getSn());
            SharedPreferencesUtil.name("sp_code").put("code", model.getSn());
            mPhoneTextWatcher.setAreaCode(model.getSn());
        }else {
            String phone=SharedPreferencesUtils.getString(getCurContext(),"login_phone","");
            mEtMobile.setText(phone);
            if (!TextUtils.isEmpty(phone)){
                mEtMobile.setSelection(phone.length());
                mIvClear.setVisibility(View.VISIBLE);
            }
        }

        //监听电话号码输入
        mEtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>0){
                    mIvClear.setVisibility(View.VISIBLE);
                }else {
                    mIvClear.setVisibility(View.GONE);
                }
            }
        });
        //清除输入号码
        mIvClear.setOnClickListener(v -> {
            mEtMobile.setText("");
            mIvClear.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        newMain = getIntent().getBooleanExtra("newMain", false);
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @OnClick(R.id.tvLogin)
    public void clickLogin() {
        String mobile = mEtMobile.getText().toString().replace(" ", "");
        String password = mEtPwd.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            CommonToast.toast(getString(R.string.login_input_phone));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            CommonToast.toast(getString(R.string.login_input_password));
            return;
        }
        createAndShowDialog2();
        mPresenter.loginPwd(mobile, password);
    }

    private void createAndShowDialog2() {
        if (dialog2 == null) {
            dialog2 = new Dialog(getCurActivity());
        }
        dialog2.setContentView(R.layout.dialog_qr_layout);
        dialog2.getWindow().setGravity(Gravity.CENTER);
        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog2.setCanceledOnTouchOutside(false);
        if (!dialog2.isShowing() && dialog2 != null) {
            dialog2.show();
        }
    }

    @OnClick(R.id.tvRegister)
    public void clickRegister() {
        startActivity(RegisterActivity.newInstance(getCurContext()));
    }

    @OnClick(R.id.tvIdentifyingCode)
    public void clickIdentifyingCode() {
        startActivity(IdentifyLoginActivity.newInstance(getCurContext()));
    }

    @OnClick(R.id.ivLoginFacebook)
    public void clickLoginFacebook() {
        createAndShowDialog2();
        mPresenter.loginFacebook();
    }

    @OnClick(R.id.ivLoginWx)
    public void clickLoginWx() {
        if (ThirdUtils.isWeixinAvilible(this)) {
            createAndShowDialog2();
            mPresenter.loginWx();
        } else {
            //未安装微信
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.tips))
                    .setMessage(getString(R.string.uninstalled_wx))
                    .setPositiveButton(getString(R.string.confirm1), null)
                    .create()
                    .show();
        }
    }

    @OnClick(R.id.ivAliLogin)
    public void clickAliLogin() {
        createAndShowDialog2();
        mPresenter.loginAli();
    }

    @OnClick(R.id.ivClose)
    public void clickBack() {
//        onBackPressed();
        finish();
    }

    @OnClick(R.id.tvAreaCode)
    public void clickAreaCode() {
        startActivityForResult(AreaCodeActivity.newInstance(getCurContext()), 111);
    }


    @OnClick(R.id.tvForgetPwd)
    public void onViewClicked() {
        String phone = mEtMobile.getText().toString().replace(" ", "");
        if (TextUtils.isEmpty(phone)) {
            CommonToast.toast(getString(R.string.login_input_phone));
            return;
        }

        String code = mTvAreaCode.getText().toString().trim();//获取sn号
        if (code.contains("261")) {
            if (!StringUtil.phoneLegal(phone)) {
                CommonToast.toast(getString(R.string.error_format_phone));
                return;
            }
        }
        mPresenter.getUser(phone);
    }

    @Override
    public void loginSuccess(UserInfoModel data) {
        SharedPreferencesUtils.setString(getCurContext(),"login_phone",data.getPhone());
        if (isMgmPay) {
            finish();
        } else {
            if (TextUtils.isEmpty(data.getGestureCode())) {
                startActivity(GestureLockActivity.newInstance(getCurContext(), GestureLockActivity.TYPE_SETTING_GESTURE));
            } else {
                Stack<Activity> activityStack = CActivityManager.getAppManager().getActivityStack();
                if (activityStack==null||activityStack.size()<=1) {
                    startActivity(MainActivity.newInstance(getCurContext()));
                }
                finish();
                NetCode.isShowGestureLockActivity = false;
            }
        }
    }

    @Override
    public void loginFalied() {
        if (dialog2.isShowing() && dialog2 != null) {
            dialog2.dismiss();
        }
    }

    @Override
    public void setMobile(String text) {

    }

    @Override
    public void showTimeCount(String text) {

    }

    @Override
    public void userNoRegister(String openId, String nickName, String loginType, String avatar, String sex) {
        startActivity(BindMobileActivity.newInstance(getCurContext(), openId, nickName, loginType, avatar, sex));
    }


//    @Override
//    public void onBackPressed() {
//        Activity activity = CActivityManager.getAppManager().findActivity(MainActivity.class);
//        if (newMain || activity == null) {
//            startActivity(MainActivity.newInstance(getCurContext()));
//        }
//        finish();
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == resultCode) {
            if (data == null) return;
            String areaCode = data.getStringExtra("areaCode");
            mTvAreaCode.setText("+" + areaCode);
            Log.e("phoneTextWatcher", "" + mPhoneTextWatcher);
            Log.e("areaCode", "" + areaCode);
            SharedPreferencesUtil.name("sp_code").put("code", areaCode);
            mPhoneTextWatcher.setAreaCode(areaCode);
        }
    }

    @Override
    protected void onDestroy() {
//        NetCode.isShowGestureLockActivity = true;
        try {
            if (dialog2 != null) {
                if (dialog2.isShowing()) {
                    dialog2.dismiss();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }
}
