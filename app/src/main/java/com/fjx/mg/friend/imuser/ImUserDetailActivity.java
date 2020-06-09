package com.fjx.mg.friend.imuser;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.fjx.mg.friend.chat.ChatActivity;
import com.fjx.mg.friend.transfer.TransferMoneyActivity;
import com.fjx.mg.image.ImageActivity;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.setting.feedback.FeedBackActivity;
import com.fjx.mg.utils.RankPermissionHelper;
import com.library.common.base.BaseMvpActivity;
import com.library.common.callback.CActivityManager;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.utils.StringUtil;
import com.library.repository.data.UserCenter;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ImUserDetailActivity extends BaseMvpActivity<ImUserDetailPresenter> implements ImUserDetailContract.View {

    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvNickName)
    TextView tvNickName;
    @BindView(R.id.tvNickName2)
    TextView tvNickName2;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvGenderAddress)
    TextView tvGenderAddress;
    @BindView(R.id.tvTranMoney)
    TextView tvTranMoney;
    @BindView(R.id.tvChat)
    TextView tvChat;
    @BindView(R.id.tvRemark)
    TextView tvRemark;

    private TIMUserProfile mProfile;
    private String imUserId;
    private String remark;

    public static Intent newInstance(Context context, String imUserId) {
        Intent intent = new Intent(context, ImUserDetailActivity.class);
        intent.putExtra("imUserId", imUserId);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_im_user_detail;
    }

    @Override
    protected void initView() {
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        ToolBarManager.with(this).setTitle("").setBackgroundColor(R.color.colorAccent)
                .setNavigationIcon(R.drawable.iv_back)
                .setRightImage(R.drawable.ic_dot_more, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.showSettingDialog(imUserId, findViewById(R.id.toolbar_iv_right));
                    }
                });

        GradientDrawableHelper.whit(tvTranMoney).setColor(R.color.colorAccent).setCornerRadius(50);
        GradientDrawableHelper.whit(tvChat).setColor(R.color.parentBgColor).setStroke(1, R.color.colorAccent).setCornerRadius(50);


        imUserId = getIntent().getStringExtra("imUserId");

        TIMFriend friend = UserCenter.getFriend(imUserId);
        if (friend == null) {
            startActivity(AddFriendActivity.newInstance(getCurContext(), imUserId));
            finish();
            return;
        }
        mPresenter.getImUserInfo(imUserId);


//        HashMap<String, Object> profileMap = new HashMap<>();
//        profileMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_GENDER, TIMFriendGenderType.GENDER_MALE);
//        profileMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_FACEURL, "http://k.zol-img.com.cn/sjbbs/7692/a7691515_s.jpg");
//        TIMFriendshipManager.getInstance().modifySelfProfile(profileMap, new TIMCallBack() {
//            @Override
//            public void onError(int code, String desc) {
//                Log.e("modifySelfProfile", "modifySelfProfile failed: " + code + " desc" + desc);
//            }
//
//            @Override
//            public void onSuccess() {
//                Log.e("modifySelfProfile", "modifySelfProfile success");
//            }
//        });
    }

    @OnClick({R.id.tvTranMoney, R.id.tvChat, R.id.tvRemark, R.id.ivAvatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvTranMoney:
                if (RankPermissionHelper.NoPrivileges(2)) return;
                startActivity(TransferMoneyActivity.newInstance(getCurContext(), "", imUserId));
                break;
            case R.id.tvChat:
                if (mProfile == null) return;
                ChatActivity.startC2CChat(getCurContext(), mProfile.getIdentifier(), mProfile.getNickName());
                break;
            case R.id.tvRemark:
//                String remark=mProfile.
                mPresenter.showRemarkEditDialog(imUserId, remark);
                break;
            case R.id.ivAvatar:
                if (mProfile == null) return;
                String url = mProfile.getFaceUrl();
                if (TextUtils.isEmpty(url)) return;
                List<String> urlList = new ArrayList<>();
                urlList.add(url);
                String urls = JsonUtil.moderToString(urlList);
                startActivity(ImageActivity.newInstance(getCurContext(), urls, 0));

                break;
        }
    }

    @Override
    public void showImUserInfo(TIMUserProfile profile) {
        mProfile = profile;

        String phone = "";
        byte[] bytes = profile.getCustomInfo().get("phone");
        if (bytes != null)
            phone = new String(bytes);

        CommonImageLoader.load(profile.getFaceUrl()).placeholder(R.drawable.default_user_image)
                .round().into(ivAvatar);
        tvNickName.setText(profile.getNickName());
        tvPhone.setText(getString(R.string.phone_num).concat("：").concat(phone));
        tvNickName2.setText(profile.getNickName());
        tvNickName2.setVisibility(View.GONE);
        tvGenderAddress.setText(getGender(profile.getGender()).concat(" ").concat(profile.getLocation()));
        tvRemark.setVisibility(View.GONE);
    }

    @Override
    public void showImUserInfo(TIMFriend user) {
        TIMUserProfile profile = user.getTimUserProfile();
        remark = user.getRemark();
        mProfile = profile;
        CommonImageLoader.load(profile.getFaceUrl())
                .placeholder(R.drawable.default_user_image)
                .into(ivAvatar);

        String phone = "";
        byte[] bytes = profile.getCustomInfo().get("phone");
        if (bytes != null)
            phone = new String(bytes);

        //有备注优先显示备注，否则显示用户昵称
        if (TextUtils.isEmpty(user.getRemark())) {
            tvNickName.setText(user.getTimUserProfile().getNickName());
            tvNickName2.setVisibility(View.GONE);
        } else {
            tvNickName.setText(user.getRemark());
            tvNickName2.setVisibility(View.VISIBLE);
            tvNickName2.setText(getString(R.string.nickname).concat("：").concat(profile.getNickName()));
        }
        tvPhone.setText(getString(R.string.phone_num).concat("：").concat(StringUtil.phoneText(phone)));
        tvGenderAddress.setText(getGender(profile.getGender()).concat(" ").concat(profile.getLocation()));
    }

    @Override
    public void deleteFriendSuccess() {
        TIMManagerExt.getInstance().deleteConversation(TIMConversationType.C2C, imUserId);
        CommonToast.toast(getString(R.string.friend_delete_success));
        UserCenter.getAllFriend();
        CActivityManager.getAppManager().finishOthersActivity(MainActivity.class);
//        finish();
    }

    @Override
    public void blackFriendSuccess() {
        TIMManagerExt.getInstance().deleteConversation(TIMConversationType.C2C, imUserId);//删除对话
        CommonToast.toast(R.string.add_blacklist);
        UserCenter.getAllFriend();
        CActivityManager.getAppManager().finishOthersActivity(MainActivity.class);
//        finish();
    }

    @Override
    public void showRemarkDialog() {
        mPresenter.showRemarkEditDialog(imUserId, remark);
    }

    @Override
    public void complaintsUser(String imUid) {
        startActivity(FeedBackActivity.newInstance(getCurContext(), imUid,1));
    }

//    @Override
//    public void editUserRemarkSuccess(TIMUserProfile profile) {
//        showImUserInfo(profile);
//    }

    private String getGender(int g) {
        if (g == 1) {
            return getString(R.string.man);

        } else if (g == 2) {
            return getString(R.string.woman);
        } else {
            return getString(R.string.unknow);
        }
    }

    @Override
    protected ImUserDetailPresenter createPresenter() {
        return new ImUserDetailPresenter(this);
    }
}
