package com.library.repository.core.net;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.library.common.base.BaseActivity;
import com.library.common.base.BaseApp;
import com.library.common.base.BaseView;
import com.library.common.callback.CActivityManager;
import com.library.common.constant.IntentConstants;
import com.library.common.receiver.ForbiddenReceiver;
import com.library.common.receiver.RankPermissionReceiver;
import com.library.common.utils.CommonToast;
import com.library.common.utils.ContextManager;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.R;
import com.library.repository.data.UserCenter;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.util.LogUtil;
import com.tencent.qcloud.uikit.TimConfig;


import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public abstract class CommonObserver<T> implements Observer<ResponseModel<T>> {

    private BaseView mView;

    public CommonObserver() {
    }

    public CommonObserver(BaseView view) {
        mView = view;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (mView != null) mView.createAndShowDialog();
    }

    @Override
    public void onNext(ResponseModel<T> response) {
        LogUtil.printJson("netLog", JsonUtil.moderToString(response), "" + response.getCode());
        if (mView != null) mView.destoryAndDismissDialog();
        if (response.getCode() == NetCode.SUCCESS) {
            onSuccess(response.getData());
        } else if (response.getCode() == NetCode.LOGIN_OVERDUE) {
            try {
                BaseActivity activity = (BaseActivity) CActivityManager.getAppManager().currentActivity();
                if (activity == null) return;
                activity.destoryAndDismissDialog();
                if (!NetCode.isShowGestureLockActivity) {
                    NetCode.isShowGestureLockActivity = true;
                    Intent intent = new Intent("mg_GestureLockActivity");
                    intent.putExtra("type", 3);
                    activity.startActivityForResult(intent, 222);
                }
                onFinish();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(response.getCode() == NetCode.LOGIN_OTHER_DEVICE_TIP){
            try {
                UserCenter.logout();
                BaseActivity activity = (BaseActivity) CActivityManager.getAppManager().currentActivity();
                if (activity == null) return;
                activity.destoryAndDismissDialog();
                NetCode.isShowGestureLockActivity = false;
                onFinish();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (response.getCode() == NetCode.PERMISSION_DENIED) {
            //等级不足
            Intent intent = new Intent();
            intent.setAction(RankPermissionReceiver.MG_RANK_ACTION);
            ContextManager.getContext().sendBroadcast(intent);
        } else if (response.getCode() == NetCode.PERMISSION_FORBIDDEN) {
            //会员禁用
            UserInfoModel infoModel = UserCenter.getUserInfo();
            infoModel.setStatus("3");
            UserCenter.saveUserInfo(infoModel);
            Intent intent = new Intent();
            intent.setAction(ForbiddenReceiver.MG_FORBIDDEN_ACTION);
            ContextManager.getContext().sendBroadcast(intent);
        } else if (response.getCode() == NetCode.USER_FAILED) {
            onSetPassword();
            onUserFailed(response);
        } else if (response.getCode() == NetCode.SENSITIVITY_CONTENT) {
            //敏感内容
            onError(response);
        } else {
            try {
                onError(response);
                Log.e("CommonObserver", response.getMsg() + "");
            } catch (NullPointerException e) {
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        if (mView != null) mView.destoryAndDismissDialog();
        if (e == null || TextUtils.isEmpty(e.getMessage())) {
            onError(new ResponseModel(-1, ContextManager.getContext().getString(R.string.error_unknow)));
            return;
        }
        if (e instanceof SocketTimeoutException) {
            Log.d("AppLog", e.getMessage().concat("    SocketTimeoutException"));
            onNetError(new ResponseModel(-2, ContextManager.getContext().getString(R.string.error_socket_fail)));
        } else if (e instanceof ConnectException) {
            onNetError(new ResponseModel(-2, ContextManager.getContext().getString(R.string.error_socket_fail)));
        }
        Log.e("CommonObserver", e.getMessage());

        String message = e.getMessage();
        if (message.contains("Failed to connect")
                || message.contains("Unable to resolve host")) {
            message = ContextManager.getContext().getString(R.string.error_net);
            onError(new ResponseModel(-1, message));
            Log.d("AppLog", "CommonObserver:" + message);
        } else {
            onError(new ResponseModel(-1, null));
        }
    }

    @Override
    public void onComplete() {
        if (mView != null) mView.destoryAndDismissDialog();
    }


    public abstract void onSuccess(T data);

    public abstract void onError(ResponseModel data);

    public abstract void onNetError(ResponseModel data);


    public void onFinish() {
    }

    public void onSetPassword() {

    }

    public void onUserFailed(ResponseModel data) {
    }


}
