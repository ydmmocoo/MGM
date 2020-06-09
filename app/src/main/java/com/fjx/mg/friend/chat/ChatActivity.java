package com.fjx.mg.friend.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fjx.mg.R;
import com.library.common.base.BaseActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StatusBarManager;
import com.tencent.qcloud.uikit.business.chat.c2c.model.C2CChatManager;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupChatManager;
import com.tencent.qcloud.uikit.common.BaseFragment;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by valxehuang on 2018/7/18.
 */

public class ChatActivity extends BaseActivity {

    public static final String IS_GROUP = "IS_GROUP";
    public static final String INTENT_DATA = "INTENT_DATA";
    BaseFragment mCurrentFragment = null;

    @Override
    protected void initView() {
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        Bundle bundle = getIntent().getExtras();
        boolean isGroup = bundle.getBoolean(IS_GROUP);
        if (isGroup) {
            mCurrentFragment = new GroupChatFragment();
        } else {
            mCurrentFragment = new PersonalChatFragment();
        }
        if (mCurrentFragment != null) {
            mCurrentFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, mCurrentFragment).commitAllowingStateLoss();
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_chat;
    }

    /**
     * 跳转到C2C聊天
     *
     * @param context  跳转容器的Context
     * @param chatInfo 会话ID(对方identify)
     */
    public static void startC2CChat(Context context, String chatInfo, String nickName) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(IS_GROUP, false);
        intent.putExtra(INTENT_DATA, chatInfo);
        intent.putExtra("nickName", nickName);
        context.startActivity(intent);
    }

    public static void startC2CChat(Context context, String chatInfo, String nickName, String elem) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(IS_GROUP, false);
        intent.putExtra(INTENT_DATA, chatInfo);
        intent.putExtra("nickName", nickName);
        intent.putExtra("message", elem);
        context.startActivity(intent);
    }

    public static void startC2CChat(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(IS_GROUP, false);
        intent.putExtra("transfer_money",true);
        context.startActivity(intent);
    }

    /**
     * 分享
     *
     * @param context
     * @param chatInfo
     * @param nickName
     * @param elem
     * @param isShare
     */
    public static void startC2CChat(Context context, String chatInfo, String nickName, String elem, String say, boolean isShare) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(IS_GROUP, false);
        intent.putExtra(INTENT_DATA, chatInfo);
        intent.putExtra("isShare", isShare);
        intent.putExtra("nickName", nickName);
        intent.putExtra("say", say);
        intent.putExtra("message", elem);
        context.startActivity(intent);
    }

    public static void startC2CChat(Context context, String chatInfo, String nickName, boolean addNewFriend) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(IS_GROUP, false);
        intent.putExtra(INTENT_DATA, chatInfo);
        intent.putExtra("nickName", nickName);
        intent.putExtra("addNewFriend", addNewFriend);
        context.startActivity(intent);
    }


    /**
     * 跳转到群聊
     *
     * @param context  跳转容器的Context
     * @param chatInfo 会话ID（群ID）
     */
    public static void startGroupChat(Context context, String chatInfo) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(IS_GROUP, true);
        intent.putExtra(IntentConstants.CHAT_INFO, chatInfo);
        context.startActivity(intent);
    }


    @Override
    public void finish() {
        if (mCurrentFragment instanceof GroupChatFragment) {
            //退出Activity时释放群聊相关资源
            GroupChatManager.getInstance().destroyGroupChat();
        } else if (mCurrentFragment instanceof PersonalChatFragment) {
            //退出Activity时释放单聊相关资源
            C2CChatManager.getInstance().destroyC2CChat();
        }
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //弹窗拒绝是调用
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) ;
        new AppSettingsDialog.Builder(this).build().show();

    }

    @Override
    public void onBackPressed() {
        SoftInputUtil.hideSoftInput(this);
        super.onBackPressed();
    }
}
