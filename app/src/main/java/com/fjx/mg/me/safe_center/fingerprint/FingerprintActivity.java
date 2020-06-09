package com.fjx.mg.me.safe_center.fingerprint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.library.common.base.BaseMvpActivity;
import com.library.common.fingerprint.FingerprintIdentify;
import com.library.common.fingerprint.base.BaseFingerprint;
import com.library.common.utils.CommonToast;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.model.FingerprintModel;
import com.library.repository.models.UserInfoModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @date 2019年11月4日10:24:14
 * description:指纹密码
 */
public class FingerprintActivity extends BaseMvpActivity<FingerprintPresenter> implements FingerprintContract.View {


    @BindView(R.id.ivSwitch)
    ImageView ivSwitch;

    private FingerprintIdentify mFingerprintIdentify;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, FingerprintActivity.class);
        return intent;
    }

    @Override
    protected FingerprintPresenter createPresenter() {
        return new FingerprintPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_fingerprint;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.finger_pwd));

        mFingerprintIdentify = new FingerprintIdentify(getApplicationContext());
        mFingerprintIdentify.setSupportAndroidL(true);
        mFingerprintIdentify.setExceptionListener(new BaseFingerprint.ExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                Log.d("FingerprintIdentify", exception.getLocalizedMessage());
            }
        });
        mFingerprintIdentify.init();
    }


    @OnClick(R.id.ivSwitch)
    public void onViewClicked() {
        if (!mFingerprintIdentify.isFingerprintEnable()) {
            CommonToast.toast(getString(R.string.no_support));
            return;
        }

        if (isFingerEnable()) {
            startActivity(FingerprintSetActivity.newInstance(getCurContext()));
        } else {
            mPresenter.showAuthDialog();
//            startActivity(FingerprintSetActivity.newInstance(getCurContext()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        ivSwitch.setImageResource(isFingerEnable() ? R.drawable.switch_on : R.drawable.switch_off);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFingerprintIdentify.cancelIdentify();
    }

    private boolean isFingerEnable() {
        UserInfoModel infoModel = UserCenter.getUserInfo();
        if (infoModel == null) return false;
        String uid = infoModel.getUId();
        FingerprintModel model = DBDaoFactory.getFingerprintDao().queryModel(uid);
        if (model == null) return false;
        return model.getFingerEnable();
    }

}
