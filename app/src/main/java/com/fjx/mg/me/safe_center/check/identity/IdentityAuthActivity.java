package com.fjx.mg.me.safe_center.check.identity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

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

public class IdentityAuthActivity extends BaseMvpActivity<IdentityAuthPresenter> implements IdentityAuthContract.View {


    @BindView(R.id.etRealName)
    EditText etRealName;
    @BindView(R.id.etIdCard)
    EditText etIdCard;

    private int mType;

    public static Intent newInstance(Context context, int type) {
        Intent intent = new Intent(context, IdentityAuthActivity.class);
        intent.putExtra("type", type);
        return intent;
    }


    @Override
    protected IdentityAuthPresenter createPresenter() {
        return new IdentityAuthPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_identity_auth;
    }

    @Override
    protected void initView() {
        mType = getIntent().getIntExtra("type", -1);
        ToolBarManager.with(this).setTitle(getString(R.string.verification));

        mPresenter.getSecurityIssue();
    }

    @Override
    public void showAuthQuestionModel(AuthQuestionModel model) {
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
        String name = etRealName.getText().toString();
        String idCard = etIdCard.getText().toString();

        if (TextUtils.isEmpty(name)) {
            CommonToast.toast(getString(R.string.hint_input_realname));
            return;
        }
        if (TextUtils.isEmpty(idCard)) {
            CommonToast.toast(getString(R.string.hint_input_idcode));
            return;
        }


        //name cardNum,phone,psw
        Map<String, Object> map = new HashMap<>();
        map.put("type", 9);
        map.put("cardNum", idCard);
        map.put("name", name);

        mPresenter.check(map);
    }
}
