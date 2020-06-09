package com.fjx.mg.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;

import com.fjx.mg.R;
import com.fjx.mg.me.safe_center.SafeCenterActivity;
import com.fjx.mg.me.safe_center.question.QuestionSetActivity;
import com.fjx.mg.setting.password.pay.ModifyPayPwdActivity;
import com.library.common.callback.CActivityManager;
import com.library.common.receiver.ForbiddenReceiver;
import com.library.common.utils.ContextManager;
import com.library.repository.data.UserCenter;
import com.library.repository.models.UserInfoModel;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.ConfirmPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;

public class RankPermissionHelper {

    /**
     * 是否有使用即将操作的功能的权限
     * <p>
     * 这边传入的参数是级别，可以查看我的会员等级界面，功能的使用需要达到对应的等级
     *
     * @param needRank 需要达到的等级
     * @return
     */
    public static boolean NoPrivileges(int needRank) {
        if (UserCenter.needLogin()) return true;
        if (isForbidden()) return true;

        if (RankPermissionHelper.showSafeCenterDialog()) return true;
//        if (!UserCenter.getUserInfo().hasNecessaryConditions()) {
//            Activity activity = CActivityManager.getAppManager().currentActivity();
//            activity.startActivity(SafeCenterActivity.newInstance(activity));
//            return true;
//        }

        return false;


        // TODO: 2019/7/26 第一版先不用会员相关
//        UserInfoModel infoModel = UserCenter.getUserInfo();
//        String myRankStr = infoModel.getRank();
//        if (TextUtils.isEmpty(myRankStr)) myRankStr = "1";
//        int myRank = Integer.parseInt(myRankStr);
//        if (myRank < needRank) {
//            //级别不够
//            Intent intent = new Intent();
//            intent.setAction(RankPermissionReceiver.MG_RANK_ACTION);
//            ContextManager.getContext().sendBroadcast(intent);
//            return true;
//        }
//        return false;
    }

    public static boolean NoPrivileges() {
        if (UserCenter.needLogin()) {
            return true;
        }
        if (isForbidden()) {
            return true;
        }

        if (RankPermissionHelper.setPayPsw()) {
            return true;
        }
        return false;
    }


    /**
     * 会员账号是否被禁用
     *
     * @return
     */
    public static boolean isForbidden() {
        UserInfoModel infoModel = UserCenter.getUserInfo();
        if (infoModel.isForbidden()) {
            Intent intent = new Intent();
            intent.setAction(ForbiddenReceiver.MG_FORBIDDEN_ACTION);
            ContextManager.getContext().sendBroadcast(intent);
            return true;
        }
        return false;
    }

    public static boolean showSafeCenterDialog() {
        if (UserCenter.getUserInfo().hasNecessaryConditions()) return false;

        Context context = CActivityManager.getAppManager().currentActivity();
        XPopup.setPrimaryColor(ContextCompat.getColor(context, R.color.colorAccent));
        ConfirmPopupView popupView = new XPopup.Builder(context)
                .asConfirm("提示", context.getString(R.string.hint_safe_lower),
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                Activity activity = CActivityManager.getAppManager().currentActivity();
                                activity.startActivity(SafeCenterActivity.newInstance(activity));
                            }
                        });
        if (!popupView.isShow())
            popupView.show();

        return true;
    }

    public static boolean setPayPsw() {
        if (UserCenter.getUserInfo().hasNecessaryConditions()) return false;

        Context context = CActivityManager.getAppManager().currentActivity();
        XPopup.setPrimaryColor(ContextCompat.getColor(context, R.color.colorAccent));
        ConfirmPopupView popupView = new XPopup.Builder(context)
                .asConfirm(context.getString(R.string.tips), context.getString(R.string.hint_safe_lower),
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                Activity activity = CActivityManager.getAppManager().currentActivity();
                                if (UserCenter.getUserInfo().getIsSetPayPsw() == 0) {
                                    activity.startActivityForResult(ModifyPayPwdActivity.newInstance(activity), 113);
                                } else if (!UserCenter.getUserInfo().isSetSecurityIssues()) {
                                    activity.startActivityForResult(QuestionSetActivity.newInstance(activity), 112);
                                }
                                //TODO 2019-10-29 11:17:49 目前版本不需要实名认证 先屏蔽
//                                else if (UserCenter.getUserInfo().isRealName() == 0){
//                                    activity.startActivityForResult(CertificationActivity.newInstance(activity), 111);
//                                }
                            }
                        });
        if (!popupView.isShow())
            popupView.show();

        return true;
    }

}
