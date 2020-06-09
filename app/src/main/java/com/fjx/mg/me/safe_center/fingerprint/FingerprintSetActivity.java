package com.fjx.mg.me.safe_center.fingerprint;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.library.common.base.BaseActivity;
import com.library.common.fingerprint.FingerprintIdentify;
import com.library.common.fingerprint.base.BaseFingerprint;
import com.library.common.utils.CommonToast;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.model.FingerprintModel;
import com.library.repository.models.UserInfoModel;

import butterknife.BindView;
import butterknife.OnClick;

public class FingerprintSetActivity extends BaseActivity {


    @BindView(R.id.ivFinger)
    ImageView ivFinger;

    private FingerprintIdentify mFingerprintIdentify;
    private int MAX_AVAILABLE_TIMES = 5;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, FingerprintSetActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_finger_auth;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.finger_auth));
        mFingerprintIdentify = new FingerprintIdentify(getApplicationContext());
        mFingerprintIdentify.setSupportAndroidL(true);
        mFingerprintIdentify.setExceptionListener(new BaseFingerprint.ExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                Log.d("FingerprintIdentify", exception.getLocalizedMessage());
            }
        });
        mFingerprintIdentify.init();
        if (!mFingerprintIdentify.isFingerprintEnable()) {
            CommonToast.toast(getString(R.string.no_support));
            finish();
        }

        mFingerprintIdentify.startIdentify(MAX_AVAILABLE_TIMES, new BaseFingerprint.IdentifyListener() {
            @Override
            public void onSucceed() {
                // 验证成功，自动结束指纹识别
                CommonToast.toast(getString(R.string.auth_access));
//                boolean isFingerEnable = UserCenter.isFingerEnable();
//                UserCenter.saveFingerEnable(!isFingerEnable);
                toggleFingerEnabl();
                finish();
            }

            @Override
            public void onNotMatch(int availableTimes) {
                // 指纹不匹配，并返回可用剩余次数并自动继续验证
                CommonToast.toast(getString(R.string.auth_no_access));
            }

            @Override
            public void onFailed(boolean isDeviceLocked) {
                // 错误次数达到上限或者API报错停止了验证，自动结束指纹识别
                CommonToast.toast(getString(R.string.busy_try_later));
            }

            @Override
            public void onStartFailedByDeviceLocked() {
                // 第一次调用startIdentify失败，因为设备被暂时锁定
                CommonToast.toast(getString(R.string.busy_try_later));
            }
        });
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

    private void toggleFingerEnabl() {
        UserInfoModel infoModel = UserCenter.getUserInfo();
        if (infoModel == null) return;
        String uid = infoModel.getUId();
        FingerprintModel model = DBDaoFactory.getFingerprintDao().queryModel(uid);
        if (model == null) {
            model = new FingerprintModel();
            model.setUserId(uid);
        }
        if (isFingerEnable()) {
            model.setFingerEnable(false);
        } else {
            model.setFingerEnable(true);
        }
        DBDaoFactory.getFingerprintDao().insertModel(model);
    }


}
