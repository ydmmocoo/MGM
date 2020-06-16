package com.library.repository.data;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.library.common.callback.CActivityManager;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.ContextManager;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.NetCode;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendAllowType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.receiver.ImLoginReceiver;

import java.util.HashMap;
import java.util.List;

import static com.tencent.imsdk.TIMUserProfile.TIM_PROFILE_TYPE_KEY_ALLOWTYPE;
import static com.tencent.imsdk.TIMUserProfile.TIM_PROFILE_TYPE_KEY_FACEURL;
import static com.tencent.imsdk.TIMUserProfile.TIM_PROFILE_TYPE_KEY_GENDER;
import static com.tencent.imsdk.TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK;

public class UserCenter {

    public static void saveUserInfo(UserInfoModel userInfo) {
        RepositoryFactory.getLocalRepository().saveUserInfo(userInfo);
    }

    public static UserInfoModel getUserInfo() {
        return RepositoryFactory.getLocalRepository().getLocalUserInfo();
    }


    public static boolean getOfflineStatus() {
        return RepositoryFactory.getLocalRepository().getOffLineStatus();
    }

    public static void savatOfflineStatus(boolean flag) {
        RepositoryFactory.getLocalRepository().saveOffLineStatus(flag);
    }


    public static void savaTimUser(TIMUserProfile userProfile) {
        RepositoryFactory.getLocalRepository().saveTIMUser(userProfile);
    }


    public static TIMUserProfile getTimUser() {
        return RepositoryFactory.getLocalRepository().getTIMUser();
    }


    public static String getToken() {
        if (getUserInfo() == null) return "";
        return getUserInfo().getToken();
    }


    public static boolean hasLogin() {
        return !TextUtils.isEmpty(getToken());
    }


    public static boolean tipsLogin() {
        if (hasLogin()) {

            return true;
        } else {


            return false;
        }
    }

    public static TIMFriend getFriend(String uid) {
        List<TIMFriend> list = RepositoryFactory.getLocalRepository().getAllFriend();
        for (TIMFriend friend : list) {
            if (TextUtils.equals(friend.getIdentifier(), uid)) return friend;
        }
        return null;
    }

    public static boolean needLogin() {
        if (hasLogin()) {//已经登录--有token的情况视为登录 不作其它操作
            return false;
        } else {
            goLoginActivity();
            return true;
        }
    }

    public static void goLoginActivity() {
        Context context = CActivityManager.getAppManager().currentActivity();
        Intent intent = new Intent("mg_LoginActivity");
        context.startActivity(intent);
    }

    public static void goMainActivity() {
        Context context = CActivityManager.getAppManager().currentActivity();
        Intent intent = new Intent("com.fjx.mg.MainActivity");
        context.startActivity(intent);
    }

    public static void logout() {
        UserInfoModel model = UserCenter.getUserInfo();
        if (model != null)
            model.setToken("");
        saveUserInfo(model);
        needLogin();
        RepositoryFactory.getChatRepository().logout();
        RepositoryFactory.getLocalRepository().logoutClear();
        NetCode.isShowGestureLockActivity = false;
    }

    public static void logout2() {
        UserInfoModel model = UserCenter.getUserInfo();
        if (model != null)
            model.setToken("");
        saveUserInfo(model);
        RepositoryFactory.getChatRepository().logout();
        RepositoryFactory.getLocalRepository().logoutClear();
        NetCode.isShowGestureLockActivity = false;
    }

    public static void logout2Settings() {
        UserInfoModel model = UserCenter.getUserInfo();
        if (model != null)
            model.setToken("");
        saveUserInfo(model);
        SharedPreferencesUtil.name("user_data").put("UserInfoModel", "").apply();
        goMainActivity();
        RepositoryFactory.getChatRepository().logout();
        RepositoryFactory.getLocalRepository().logoutClear();
        NetCode.isShowGestureLockActivity = false;
    }


    public static boolean hasImLogin() {
        String loginUser = TIMManager.getInstance().getLoginUser();
        return !TextUtils.isEmpty(loginUser);
    }


    public static void imLogin() {
        if (!hasLogin()) return;
        final String timUserId = getUserInfo().getIdentifier();
        String sign = getUserInfo().getUseRig();
        RepositoryFactory.getChatRepository().login(timUserId, sign, new TIMCallBack() {

            @Override
            public void onError(int i, String s) {
                Log.e(Constant.TIM_LOG, "code" + i + "  error" + s);
                initStorage(timUserId);
                sendLoginReceiver(false);
                SharedPreferencesUtil.name("error_code").put("code", i).apply();
                SharedPreferencesUtil.name("offline_name").put("offline", 1).apply();
                if (i == 6208) {
                    //离线被踢
                    UserInfoModel userInfo = getUserInfo();
                    userInfo.setToken("");
                    saveUserInfo(userInfo);
                    NetCode.isShowGestureLockActivity = true;
                    UserCenter.savatOfflineStatus(true);
                    Intent intent = new Intent("mg_LoginActivity");
                    intent.putExtra(IntentConstants.NEW_MAIN, false);
                    intent.putExtra(IntentConstants.FLAG, true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ContextManager.getContext().startActivity(intent);

                }
                Log.e(Constant.TIM_LOG, s.concat("  code:").concat(String.valueOf(i)));
            }

            @Override
            public void onSuccess() {
                Log.e(Constant.TIM_LOG, "Success");
                getUserProFile();
                sendLoginReceiver(true);
            }
        });
    }

    public static void sendLoginReceiver(boolean isSuccess) {
        Intent intent = new Intent();
        if (isSuccess) {
            intent.setAction(ImLoginReceiver.MG_LOGIN_SUCCESS);
        } else {
            intent.setAction(ImLoginReceiver.MG_LOGIN_FAILED);
        }
        ContextManager.getContext().sendBroadcast(intent);
    }


    private static void getUserProFile() {
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {
                Log.d(Constant.TIM_LOG, s);
            }

            @Override
            public void onSuccess(TIMUserProfile timUserProfile) {
                savaTimUser(timUserProfile);
                Log.d(Constant.TIM_LOG, JsonUtil.moderToString(timUserProfile));
                getAllFriend();
            }
        });
    }

    public static void bindLoginIm(final String avatar, final String nickName, final String sex) {
        if (!hasLogin()) return;
        final String timUserId = getUserInfo().getIdentifier();
        String sign = getUserInfo().getUseRig();

        RepositoryFactory.getChatRepository().login(timUserId, sign, new TIMCallBack() {

            @Override
            public void onError(int i, String s) {
                initStorage(timUserId);
                Log.d(Constant.TIM_LOG, s.concat("  code:").concat(String.valueOf(i)));

            }

            @Override
            public void onSuccess() {
                Log.d(Constant.TIM_LOG, "Success");
                updateImUserImage(avatar, nickName, sex);
                getUserProFile();
            }
        });
    }

    private static void updateImUserImage(String imageUrl, String nickName, String sex) {
        HashMap<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(imageUrl))
            map.put(TIM_PROFILE_TYPE_KEY_FACEURL, imageUrl);
        if (!TextUtils.isEmpty(nickName))
            map.put(TIM_PROFILE_TYPE_KEY_NICK, nickName);
        if (!TextUtils.isEmpty(sex)) {
            try {
                map.put(TIM_PROFILE_TYPE_KEY_GENDER, Integer.parseInt(sex));
            } catch (NumberFormatException e) {
                map.put(TIM_PROFILE_TYPE_KEY_GENDER, "2");
            }
        }
        RepositoryFactory.getChatRepository().modifySelfProfile(map, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("updateImUserImage", s);
            }

            @Override
            public void onSuccess() {
                Log.d("updateImUserImage", "onSuccess: ");
                getUserProFile();
            }
        });
    }

    private static void initStorage(String timUserId) {
        TIMManagerExt.getInstance().initStorage(timUserId, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                Log.e(Constant.TIM_LOG, "initStorage failed, code: " + code + "|descr: " + desc);
            }

            @Override
            public void onSuccess() {
                Log.i(Constant.TIM_LOG, "initStorage succ");
            }
        });
    }

    public static void getAllFriend() {
        RepositoryFactory.getChatRepository().getAllFriend(new TIMValueCallBack<List<TIMFriend>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMFriend> list) {
                RepositoryFactory.getLocalRepository().saveUserAvatar(list);
                RepositoryFactory.getLocalRepository().saveAllFriend(list);
                SharedPreferencesUtil sp = SharedPreferencesUtil.name("FriendCheckStateSpUtil");
                boolean aBoolean = sp.getBoolean(TIMManager.getInstance().getLoginUser(), false);
                if (aBoolean) {
                    modifySelf();
                } else {
                    modifySelf2();
                }
            }
        });
    }


    //test
    private static void modifySelf() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(TIM_PROFILE_TYPE_KEY_ALLOWTYPE, TIMFriendAllowType.TIM_FRIEND_NEED_CONFIRM);
        RepositoryFactory.getChatRepository().modifySelfProfile(map, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

    private static void modifySelf2() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(TIM_PROFILE_TYPE_KEY_ALLOWTYPE, TIMFriendAllowType.TIM_FRIEND_ALLOW_ANY);
        RepositoryFactory.getChatRepository().modifySelfProfile(map, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("modifySelf", "desc = ".concat(s.concat("code " + i)));
            }

            @Override
            public void onSuccess() {
                Log.d("modifySelf", "onSuccess");
            }
        });
    }
}
