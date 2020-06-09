package com.fjx.mg.me.safe_center.check.question;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.me.safe_center.AuthType;
import com.fjx.mg.me.safe_center.bind.RebindMobileActivity;
import com.fjx.mg.setting.password.login.ModifyLoginPwdActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.callback.CActivityManager;
import com.library.common.utils.CommonToast;
import com.library.repository.data.UserCenter;
import com.library.repository.models.AuthQuestionModel;
import com.library.repository.models.UserInfoModel;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class QuestionAuthActivity extends BaseMvpActivity<QuestionAuthPresenter> implements QuestionAuthContract.View {



    @BindView(R.id.tvQuestion)
    TextView tvQuestion;
    @BindView(R.id.etQuestion)
    EditText etQuestion;
    private int mType;
    private String question;

    public static Intent newInstance(Context context, int type) {
        Intent intent = new Intent(context, QuestionAuthActivity.class);
        intent.putExtra("type", type);
        return intent;
    }


    @Override
    protected QuestionAuthPresenter createPresenter() {
        return new QuestionAuthPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_question_auth;
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
            case AuthType.LOGIN_PASSWORD:
            case AuthType.FORGET_PASSWORD:
                startActivity(ModifyLoginPwdActivity.newInstance(getCurContext(), true));
                finish();
                break;
            case AuthType.BIND_MOBILE:
                startActivity(RebindMobileActivity.newInstance(getCurContext()));
                finish();
                break;

            case AuthType.BIND_DEVICE:
                mPresenter.bindDevice();
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


    @OnClick(R.id.confirm)
    public void onViewClicked() {
        String answer = etQuestion.getText().toString();


        if (TextUtils.isEmpty(answer)) {
            CommonToast.toast(getString(R.string.hint_input_answer));
            return;
        }

        //name cardNum,phone,psw
        Map<String, Object> map = new HashMap<>();
        map.put("type", 10);
        map.put("answer", answer);
        map.put("question", question);

        mPresenter.check(map);
    }
}
