package com.fjx.mg.me.safe_center.question;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.setting.certification.CertificationActivity;
import com.fjx.mg.setting.password.pay.ModifyPayPwdActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.repository.data.UserCenter;
import com.library.repository.models.UserInfoModel;

import butterknife.BindView;
import butterknife.OnClick;

public class QuestionSetActivity extends BaseMvpActivity<QuestionSetPresenter> implements QuestionSetContract.View {


    @BindView(R.id.etQuestion)
    EditText etQuestion;
    @BindView(R.id.etAnswer)
    EditText etAnswer;
    @BindView(R.id.confirm)
    TextView confirm;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, QuestionSetActivity.class);
        return intent;
    }

    @Override
    protected QuestionSetPresenter createPresenter() {
        return new QuestionSetPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_question_set;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.set_safe_question));
        GradientDrawableHelper.whit(confirm).setColor(R.color.colorAccent).setCornerRadius(50);
    }


    @OnClick(R.id.confirm)
    public void onViewClicked() {
        String question = etQuestion.getText().toString();
        String answer = etAnswer.getText().toString();
        mPresenter.setProblem(question, answer);


    }

    @Override
    public void setProblemSuccess() {
        UserInfoModel infoModel = UserCenter.getUserInfo();
//        if (infoModel.isRealName() == 0) {
//            startActivity(CertificationActivity.newInstance(getCurContext()));
//        } else
        //TODO 2019-10-29 11:17:49 目前版本不需要实名认证 先屏蔽
        if (infoModel.getIsSetPayPsw() != 1) {
            startActivity(ModifyPayPwdActivity.newInstance(getCurContext()));
            CommonToast.toast(getString(R.string.set_success));
            finish();
            return;
        }
        CommonToast.toast(getString(R.string.set_success));
        setResult(111);
        finish();
    }
}
