package com.fjx.mg.login.bind;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.common.paylibrary.model.AliUserModel;
import com.fjx.mg.R;
import com.fjx.mg.login.areacode.AreaCodeActivity;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.me.safe_center.lock.GestureLockActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.StatusBarManager;
import com.library.repository.models.UserInfoModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * date       0n 2019年11月5日16:09:49
 * description: 绑定手机页
 */
public class BindMobileActivity extends BaseMvpActivity<BindMobilePresenter> implements BindMobileContract.View {

    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvAreaCode)
    TextView tvAreaCode;

    @BindView(R.id.tvGetSmsCode)
    TextView tvGetSmsCode;

    @BindView(R.id.tvPwdHint)
    TextView tvPwdHint;

    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.etConfirmPwd)
    EditText etConfirmPwd;

    @BindView(R.id.etSmsCode)
    EditText etSmsCode;
    @BindView(R.id.linePassword)
    View linePassword;
    @BindView(R.id.lineConfirmPassword)
    View lineConfirmPassword;
    @BindView(R.id.llLoginPassword)
    View llLoginPassword;
    @BindView(R.id.llConfirmPwd)
    View llConfirmPwd;


    private String openid, loginType, nickName, avatar, sex, password;
    private Boolean hasPass = false;

    public static Intent newInstance(Context context, String openid, String nickName, String loginType, String avatar, String sex) {
        Intent intent = new Intent(context, BindMobileActivity.class);
        intent.putExtra("openid", openid);
        intent.putExtra("nickName", nickName);
        intent.putExtra("loginType", loginType);
        intent.putExtra("avatar", avatar);
        intent.putExtra("sex", sex);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_bind;
    }

    @Override
    public void setMobile(String text) {
        etMobile.setText(text);
    }

    @Override
    protected void initView() {
        openid = getIntent().getStringExtra("openid");
        nickName = getIntent().getStringExtra("nickName");
        loginType = getIntent().getStringExtra("loginType");
        avatar = getIntent().getStringExtra("avatar");
        sex = getIntent().getStringExtra("sex");


        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        GradientDrawableHelper.whit(tvLogin).setColor(R.color.colorAccent).setCornerRadius(80);
//        if (TextUtils.equals(Constant.LoginType.ZFB, loginType)) {
//            mPresenter.getAliUserInfo(authCode);
//        }
    }


    @Override
    protected BindMobilePresenter createPresenter() {
        return new BindMobilePresenter(this);
    }


    @OnClick(R.id.tvAreaCode)
    public void clickAreaCode() {
        startActivityForResult(AreaCodeActivity.newInstance(getCurContext()), 111);

//        CommonDialogHelper.showAreaCodeDialog(this, new OnSelectListener() {
//            @Override
//            public void onSelect(int position, String text) {
//                tvAreaCode.setText(text);
//            }
//        });
    }

    @OnClick(R.id.tvLogin)
    public void clickBind() {
//        if (!hasPass) {
//            CommonToast.toast(R.string.failed_to_get_the_phone_information);
//            return;
//        }
        String phone = etMobile.getText().toString();
        String smsCode = etSmsCode.getText().toString();
        String areaCode = tvAreaCode.getText().toString().replace("+", "");
        String pwd = etPwd.getText().toString();
        String confirmPwd = etConfirmPwd.getText().toString();

        mPresenter.bind(nickName, areaCode, phone, smsCode, pwd, openid, sex, avatar, loginType, confirmPwd);

    }

    @OnClick(R.id.tvGetSmsCode)
    public void clickGetSmsCode() {
        String mobile = etMobile.getText().toString();
        String areaCode = tvAreaCode.getText().toString().replace("+", "");
        mPresenter.sendSmsCode(mobile, areaCode);
        mPresenter.getUser(mobile);
    }


    @Override
    public void bindSuccess(UserInfoModel data) {
        if (data.hasGesture()) {
            startActivity(MainActivity.newInstance(getCurContext()));
        } else {
            startActivity(GestureLockActivity.newInstance(getCurContext(), GestureLockActivity.TYPE_SETTING_GESTURE));
        }
    }

    @Override
    public void showTimeCount(String text) {
        tvGetSmsCode.setText(text);
    }

    @Override
    public void aliLoginSuccess(AliUserModel userModel) {
        openid = userModel.getUser_id();
        nickName = userModel.getNick_name();

        avatar = userModel.getAvatar();
        if (TextUtils.equals(userModel.getGender(), "m")) {
            sex = "1";
        } else {
            sex = "2";
        }
    }

    @Override
    public void showUser(String info) {
        Log.e("password", "" + info);
        this.password = info;
        //TODO 目前登录有默认密码不需要设置
//        llLoginPassword.setVisibility(TextUtils.isEmpty(info) ? View.VISIBLE : View.GONE);
//        linePassword.setVisibility(TextUtils.isEmpty(info) ? View.VISIBLE : View.GONE);
//        llConfirmPwd.setVisibility(TextUtils.isEmpty(info) ? View.VISIBLE : View.GONE);
//        lineConfirmPassword.setVisibility(TextUtils.isEmpty(info) ? View.VISIBLE : View.GONE);
//        tvPwdHint.setVisibility(TextUtils.isEmpty(info) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void sethasPass(Boolean canpass) {
        hasPass = canpass;
    }

    @Override
    public String getSignPassword() {
        return password;
    }

    @Override
    protected void onDestroy() {
        mPresenter.releaseTimer();
        super.onDestroy();
    }


    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == resultCode) {
            if (data == null) return;
            String areaCode = data.getStringExtra("areaCode");
            tvAreaCode.setText("+" + areaCode);
        }
    }
}
