package com.fjx.mg.me.safe_center.check.password_question;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

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
import com.library.repository.models.AuthQuestionModel;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PasswordQuestionActivity extends BaseMvpActivity<PwdQuPresenter> implements PwdQuContract.View {

    @BindView(R.id.tvQuestion)
    TextView tvQuestion;
    @BindView(R.id.etQuestion)
    EditText etQuestion;
    @BindView(R.id.etPassword)
    EditText etPassword;
    private int mType;
    private String question;

    public static Intent newInstance(Context context, int type) {
        Intent intent = new Intent(context, PasswordQuestionActivity.class);
        intent.putExtra("type", type);
        return intent;
    }

    @Override
    protected PwdQuPresenter createPresenter() {
        return new PwdQuPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_password_question;
    }

    @Override
    protected void initView() {
        mType = getIntent().getIntExtra("type", -1);
        ToolBarManager.with(this).setTitle(getString(R.string.verification));

        mPresenter.getSecurityIssue();
    }

    @Override
    public void showAuthQuestionModel(AuthQuestionModel model) {
        question = model.getQuestion();
        tvQuestion.setText(getString(R.string.question).concat("：").concat(model.getQuestion()));

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
            case AuthType.SECURITY_ISSUES:
                startActivity(QuestionSetActivity.newInstance(getCurContext()));
                finish();
                break;
            case AuthType.BIND_MOBILE:
                startActivity(RebindMobileActivity.newInstance(getCurContext()));
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
        String pwd = etPassword.getText().toString();
        String answer = etQuestion.getText().toString();

        if (TextUtils.isEmpty(pwd)) {
            CommonToast.toast(getString(R.string.login_input_password));
            return;
        }
        if (TextUtils.isEmpty(answer)) {
            CommonToast.toast(getString(R.string.hint_input_answer));
            return;
        }
        //name cardNum,phone,psw
        Map<String, Object> map = new HashMap<>();
        map.put("type", 8);
        map.put("psw", StringUtil.getPassword(pwd));
        map.put("question", question);
        map.put("phone", UserCenter.getUserInfo().getPhone());
        map.put("answer", answer);
        mPresenter.check(map);
    }
}
