package com.fjx.mg.me.safe_center.check.identity_mobile;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.me.safe_center.AuthType;
import com.fjx.mg.me.safe_center.question.QuestionSetActivity;
import com.fjx.mg.setting.password.login.ModifyLoginPwdActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.data.UserCenter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class IdentityMobileAuthActivity extends BaseMvpActivity<IdentityMobileAuthPresenter> implements IdentityMobileAuthContract.View {


    @BindView(R.id.etRealName)
    EditText etRealName;
    @BindView(R.id.etIdCard)
    EditText etIdCard;
    @BindView(R.id.tvHint)
    TextView tvHint;
    @BindView(R.id.etSmsCode)
    EditText etSmsCode;
    @BindView(R.id.tvGetSmsCode)
    TextView tvGetSmsCode;
    private int mType;

    public static Intent newInstance(Context context, int type) {
        Intent intent = new Intent(context, IdentityMobileAuthActivity.class);
        intent.putExtra("type", type);
        return intent;
    }

    @Override
    protected IdentityMobileAuthPresenter createPresenter() {
        return new IdentityMobileAuthPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_identity_mobile;
    }

    @Override
    protected void initView() {
        mType = getIntent().getIntExtra("type", -1);
        ToolBarManager.with(this).setTitle(getString(R.string.verification));

        String phone = UserCenter.getUserInfo().getPhone();
        tvHint.setText(getString(R.string.send_sms_to).concat(StringUtil.phoneText(phone)));
    }

    @Override
    public void checkSuccess() {
        switch (mType) {
            case AuthType.SECURITY_ISSUES:
                startActivity(QuestionSetActivity.newInstance(getCurContext()));
                break;
            case AuthType.LOGIN_PASSWORD:
            case AuthType.FORGET_PASSWORD:
                startActivity(ModifyLoginPwdActivity.newInstance(getCurContext(), true));
                break;
        }

        finish();
    }

    @Override
    public void showTimeCount(String s) {
        tvGetSmsCode.setText(s);
    }


    private void onClickConfirm() {
        String name = etRealName.getText().toString();
        String idCard = etIdCard.getText().toString();
        String smsCode = etSmsCode.getText().toString();
        String phone = UserCenter.getUserInfo().getPhone();

        if (TextUtils.isEmpty(name)) {
            CommonToast.toast(getString(R.string.hint_input_realname));
            return;
        }
        if (TextUtils.isEmpty(idCard)) {
            CommonToast.toast(getString(R.string.hint_input_idcode));
            return;
        }

        if (TextUtils.isEmpty(smsCode)) {
            CommonToast.toast(getString(R.string.login_input_smscode));
            return;
        }

        //name cardNum,phone,psw
        Map<String, Object> map = new HashMap<>();
        map.put("type", 1);
        map.put("cardNum", idCard);
        map.put("smsCode", smsCode);
        map.put("phone", phone);
        map.put("name", name);
        mPresenter.check(map);
    }


    @OnClick({R.id.tvGetSmsCode, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvGetSmsCode:
                mPresenter.sendSmsCode();
                break;
            case R.id.confirm:
                onClickConfirm();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.releaseTimer();
        super.onDestroy();
    }
}
