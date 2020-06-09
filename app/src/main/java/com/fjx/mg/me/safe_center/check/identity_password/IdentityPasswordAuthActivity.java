package com.fjx.mg.me.safe_center.check.identity_password;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.me.safe_center.AuthType;
import com.fjx.mg.me.safe_center.bind.RebindMobileActivity;
import com.fjx.mg.me.safe_center.fingerprint.FingerprintSetActivity;
import com.fjx.mg.me.safe_center.lock.GestureLockActivity;
import com.fjx.mg.me.safe_center.question.QuestionSetActivity;
import com.fjx.mg.setting.password.pay.ModifyPayPwdActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.data.UserCenter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class IdentityPasswordAuthActivity extends BaseMvpActivity<IdentityPasswordAuthPresenter> implements IdentityPasswordAuthContract.View {


    @BindView(R.id.etRealName)
    EditText etRealName;
    @BindView(R.id.etIdCard)
    EditText etIdCard;
    @BindView(R.id.etPassword)
    EditText etPassword;
    private int mType;

    public static Intent newInstance(Context context, int type) {
        Intent intent = new Intent(context, IdentityPasswordAuthActivity.class);
        intent.putExtra("type", type);
        return intent;
    }

    @Override
    protected IdentityPasswordAuthPresenter createPresenter() {
        return new IdentityPasswordAuthPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_identity_password;
    }

    @Override
    protected void initView() {
        mType = getIntent().getIntExtra("type", -1);
        ToolBarManager.with(this).setTitle(getString(R.string.verification));
    }

    @Override
    public void checkSuccess() {
        switch (mType) {
            case AuthType.SECURITY_ISSUES:
                startActivity(QuestionSetActivity.newInstance(getCurContext()));
                break;
            case AuthType.BIND_MOBILE:
                startActivity(RebindMobileActivity.newInstance(getCurContext()));
                break;
            case AuthType.GESTURE:
                startActivity(GestureLockActivity.newInstance(getCurContext(), GestureLockActivity.TYPE_SETTING_GESTURE));
                finish();
            case AuthType.PAY_PASSWORD:
                startActivityForResult(ModifyPayPwdActivity.newInstance(getCurContext(), true), 111);
                finish();
                break;
            case AuthType.FINGERPRINT:
                startActivity(FingerprintSetActivity.newInstance(getCurContext()));
                finish();
                break;
        }

        finish();
    }


    @OnClick(R.id.confirm)
    public void onViewClicked() {
        String name = etRealName.getText().toString();
        String idCard = etIdCard.getText().toString();
        String pwd = etPassword.getText().toString();
        String phone = UserCenter.getUserInfo().getPhone();

        if (TextUtils.isEmpty(name)) {
            CommonToast.toast(getString(R.string.hint_input_realname));
            return;
        }
        if (TextUtils.isEmpty(idCard)) {
            CommonToast.toast(getString(R.string.hint_input_idcode));
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            CommonToast.toast(getString(R.string.login_input_password));
            return;
        }

        //name cardNum,phone,psw
        Map<String, Object> map = new HashMap<>();
        map.put("type", 2);
        map.put("cardNum", idCard);
        map.put("psw", StringUtil.getPassword(pwd));
        map.put("phone", phone);
        map.put("name", name);
        mPresenter.check(map);
    }
}
