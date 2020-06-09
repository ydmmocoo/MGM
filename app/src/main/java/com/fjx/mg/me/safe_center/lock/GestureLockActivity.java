package com.fjx.mg.me.safe_center.lock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.login.login.LoginActivity;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.view.lock.GestureLockLayout;
import com.fjx.mg.view.lock.ILockView;
import com.fjx.mg.view.lock.JDLockView;
import com.fjx.mg.view.lock.LockViewFactory;
import com.library.common.base.BaseMvpActivity;
import com.library.common.callback.CActivityManager;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonToast;
import com.library.common.utils.LogTUtil;
import com.library.repository.core.net.NetCode;
import com.library.repository.data.UserCenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 手势密码界面
 */
public class GestureLockActivity extends BaseMvpActivity<GestureLockPresenter> implements GestureLockContract.View {

    public static final int TYPE_SETTING_GESTURE = 1;//设置手势
    public static final int TYPE_AUTH = 2;//验证
    public static final int TYPE_REFRESHTOKEN = 3;//刷新token
    public static final int TYPE_CHANGE = 4;//修改手势

    private Handler mHandler = new Handler();

    @BindView(R.id.l_lock_view)
    GestureLockLayout lockView;
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.tvForgetGesture)
    TextView tvForgetGesture;
    private int mType;//设置手势密码
    private boolean isCreated;

    /**
     * @param context
     * @return
     */
    public static Intent newInstance(Context context, int type) {
        Intent intent = new Intent(context, GestureLockActivity.class);
        intent.putExtra(IntentConstants.TYPE, type);
        return intent;
    }

    @Override
    protected GestureLockPresenter createPresenter() {
        return new GestureLockPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isCreated) {
            isCreated = true;
        } else {
            return;
        }

    }

    @Override
    protected int layoutId() {
        return R.layout.activity_gesture_lock;
    }

    @Override
    protected void initView() {
        mType = getIntent().getIntExtra(IntentConstants.TYPE, TYPE_SETTING_GESTURE);
        initHintText();

        ToolBarManager.with(this).setTitle(getString(R.string.gesture_pwd));
        //设置手势解锁模式为验证模式
        initGestureSet();
    }

    private void initGestureSet() {
        lockView.setMode(mType == TYPE_SETTING_GESTURE ? GestureLockLayout.RESET_MODE : GestureLockLayout.VERIFY_MODE);
        //设置手势解锁每行每列点的个数
        lockView.setDotCount(3);
        //设置手势解锁最大尝试次数 默认 5
        lockView.setTryTimes(3);
        lockView.setLockView(new LockViewFactory() {
            @Override
            public ILockView newLockView() {
                return new JDLockView(getCurContext());
            }
        });
        if (mType != TYPE_SETTING_GESTURE) {
            if (UserCenter.getUserInfo() == null) {
                UserCenter.logout();
                finish();
                return;
            }
            //设置手势解锁正确答案
            lockView.setAnswerSting(UserCenter.getUserInfo().getGestureCode());
        }

        if (mType == TYPE_REFRESHTOKEN)
            tvForgetGesture.setVisibility(View.VISIBLE);
        initEvents();
    }

    private void initHintText() {
        switch (mType) {
            case TYPE_SETTING_GESTURE:
                tv_hint.setText(getString(R.string.hint_new_gesture));
                break;
            case TYPE_CHANGE:
                tv_hint.setText(getString(R.string.hint_input_old_gesture));
                break;
        }
    }


    private void initEvents() {
        if (mType == TYPE_SETTING_GESTURE) {
            mPresenter.setOnLockResetListener(lockView);

        } else {
            mPresenter.setOnLockVerifyListener(lockView);
        }

    }

    private void resetGesture() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lockView.resetGesture();
            }
        }, 200);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onFirstPasswordFinished(List<Integer> answerList) {
        tv_hint.setText(getString(R.string.hint_confirm_gesture));
        resetGesture();
    }

    @Override
    public void onSetPasswordFinished(boolean isMatched, String answerList) {
        if (isMatched) {
            mPresenter.setGestureCode(answerList);
            return;
        }
        CommonToast.toast(R.string.gesture_failed_reset_guessture);
        tv_hint.setText(getString(R.string.hint_new_gesture));
        resetGesture();
        initGestureSet();
    }

    @Override
    public void onGestureFinished(boolean isMatched) {
        if (isMatched) {
            if (mType == TYPE_REFRESHTOKEN) {
                mPresenter.refreshToken(UserCenter.getUserInfo().getGestureCode());
            } else if (mType == TYPE_CHANGE) {
                tv_hint.setText(getString(R.string.hint_new_gesture));
                lockView.setMode(GestureLockLayout.RESET_MODE);
                mPresenter.setOnLockResetListener(lockView);
                lockView.setAnswerSting("");
            }
            CommonToast.toast(R.string.gesture_check_success);
            return;
        }
        resetGesture();
        CommonToast.toast(R.string.gesture_check_failed);

    }

    @Override
    public void onGestureTryTimesBoundary() {
        if (mType == TYPE_REFRESHTOKEN) {
            UserCenter.logout();
            finish();
        }
    }

    @Override
    public void setGestureCodeSuccess() {
        CommonToast.toast(getString(R.string.set_success));
        if (mType == TYPE_SETTING_GESTURE) {
            startActivity(MainActivity.newInstance(getCurContext()));
            CActivityManager.getAppManager().finishOthersActivity(MainActivity.class);
        } else {
            finish();
        }
    }

    @Override
    public void refreshTokenSuccess() {
        //刷新token成功跳转LoginActivity
        startActivity(LoginActivity.newInstance(this, true));
    }


    @Override
    public void onBackPressed() {
//        if (mType == TYPE_SETTING_GESTURE || mType == TYPE_REFRESHTOKEN) {
//            UserCenter.logout();
//        } else {
//            setResult(222);
//            finish();
//        }
        finish();
        UserCenter.logout2();
    }


    @OnClick(R.id.tvForgetGesture)
    public void onViewClicked() {
//        startActivity(LoginActivity.newInstance(getCurContext()));
//        UserCenter.logout();
//        setResult(222);
        UserCenter.logout2();
        finish();
        LogTUtil.i("netLog", "tvForgetGesture" + NetCode.isShowGestureLockActivity);
    }
}
