package com.fjx.mg.me.safe_center.check.mobile_question;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.me.safe_center.AuthType;
import com.fjx.mg.me.safe_center.fingerprint.FingerprintSetActivity;
import com.fjx.mg.me.safe_center.lock.GestureLockActivity;
import com.fjx.mg.setting.password.login.ModifyLoginPwdActivity;
import com.fjx.mg.setting.password.pay.ModifyPayPwdActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.callback.CActivityManager;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.AuthQuestionModel;
import com.library.repository.models.UserInfoModel;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MobileQuestionAuthActivity extends BaseMvpActivity<MobileQuestionAuthPresenter> implements MobileQuestionAuthContract.View {

    @BindView(R.id.etSmsCode)
    EditText etSmsCode;
    @BindView(R.id.tvGetSmsCode)
    TextView tvGetSmsCode;

    @BindView(R.id.tvQuestion)
    TextView tvQuestion;
    @BindView(R.id.tvHint)
    TextView tvHint;
    @BindView(R.id.etQuestion)
    EditText etQuestion;
    private int mType;
    private String question;

    public static Intent newInstance(Context context, int type) {
        Intent intent = new Intent(context, MobileQuestionAuthActivity.class);
        intent.putExtra("type", type);
        return intent;
    }

    @Override
    protected MobileQuestionAuthPresenter createPresenter() {
        return new MobileQuestionAuthPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_mobile_question;
    }

    @Override
    protected void initView() {
        mType = getIntent().getIntExtra("type", -1);
        ToolBarManager.with(this).setTitle(getString(R.string.verification));
        mPresenter.getSecurityIssue();

        String phone = UserCenter.getUserInfo().getPhone();
        tvHint.setText(getString(R.string.send_sms_to).concat(StringUtil.phoneText(phone)));
    }


    @Override
    public void showAuthQuestionModel(AuthQuestionModel model) {
        question = model.getQuestion();
        try {
            if (StringUtil.isNotEmpty(model.getQuestion())) {
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.question));
                sb.append("：");
                sb.append(model.getQuestion());
                tvQuestion.setText(sb.toString());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
//        tvQuestion.setText(getString(R.string.question).concat("：").concat(model.getQuestion()));

    }

    @Override
    public void showTimeCount(String s) {
        tvGetSmsCode.setText(s);
    }

    @Override
    public void checkSuccess() {
        switch (mType) {
            case AuthType.GESTURE:
                startActivity(GestureLockActivity.newInstance(getCurContext(), GestureLockActivity.TYPE_SETTING_GESTURE));
                finish();
                break;
            case AuthType.PAY_PASSWORD:
                startActivityForResult(ModifyPayPwdActivity.newInstance(getCurContext(), true), 111);
                finish();
                break;
            case AuthType.FINGERPRINT:
                startActivity(FingerprintSetActivity.newInstance(getCurContext()));
                finish();
                break;
            case AuthType.FORGET_PASSWORD://忘记密码
            case AuthType.LOGIN_PASSWORD:
                startActivity(ModifyLoginPwdActivity.newInstance(getCurContext(), true));
                finish();
                break;
            case AuthType.BIND_DEVICE:
                mPresenter.bindDevice();
                break;
            default:
                break;
        }

    }

    @Override
    public void bindDeviceSuccess() {
        UserInfoModel infoModel = UserCenter.getUserInfo();
        infoModel.setCheckDevice(false);
        UserCenter.saveUserInfo(infoModel);
        startActivity(MainActivity.newInstance(getCurContext()));
        CActivityManager.getAppManager().finishOthersActivity(MainActivity.class);
    }


    @OnClick({R.id.tvGetSmsCode, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvGetSmsCode:
                mPresenter.sendSmsCode();
                break;
            case R.id.confirm:
                String smsCode = etSmsCode.getText().toString();
                String answer = etQuestion.getText().toString();
                String phone = UserCenter.getUserInfo().getPhone();

                if (TextUtils.isEmpty(smsCode)) {
                    CommonToast.toast(getString(R.string.login_input_smscode));
                    return;
                }
                if (TextUtils.isEmpty(answer)) {
                    CommonToast.toast(getString(R.string.hint_input_answer));
                    return;
                }

                Map<String, Object> map = new HashMap<>();
                map.put("type", 4);
                map.put("smsCode", smsCode);
                map.put("answer", answer);
                map.put("phone", phone);
                map.put("question", question);
                mPresenter.check(map);

                break;
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.releaseTimer();
        super.onDestroy();
    }
}
