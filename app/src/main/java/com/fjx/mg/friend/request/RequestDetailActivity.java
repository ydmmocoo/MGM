package com.fjx.mg.friend.request;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.chat.ChatActivity;
import com.fjx.mg.friend.transfer.TransferMoneyActivity;
import com.fjx.mg.main.MainActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.callback.CActivityManager;
import com.library.common.utils.CacheManager;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.StatusBarManager;
import com.library.common.utils.StringUtil;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.business.chat.c2c.model.C2CChatInfo;
import com.tencent.qcloud.uikit.business.chat.c2c.model.C2CChatManager;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class RequestDetailActivity extends BaseMvpActivity<RequestDetailPresenter> implements RequestDetailContract.View {

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
    @BindView(R.id.tvAgree)
    TextView tvAgree;
    @BindView(R.id.tvRemark)
    TextView tvRemark;

    @BindView(R.id.etReply)
    EditText etReply;

    private String imUserId;
    private String addWorld;

    public static Intent newInstance(Context context, String imUserId, String addWorld) {
        Intent intent = new Intent(context, RequestDetailActivity.class);
        intent.putExtra("imUserId", imUserId);
        intent.putExtra("addWorld", addWorld);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_im_request_detail;
    }

    @Override
    protected void initView() {
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        ToolBarManager.with(this).setTitle("").setBackgroundColor(R.color.colorAccent)
                .setNavigationIcon(R.drawable.iv_back);

        GradientDrawableHelper.whit(tvAgree).setColor(R.color.colorAccent).setCornerRadius(50);
        imUserId = getIntent().getStringExtra("imUserId");
        addWorld = getIntent().getStringExtra("addWorld");
        mPresenter.getImUserInfo(imUserId);

    }

    @OnClick({R.id.tvAgree, R.id.tvReject})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvAgree:
                String remark = etReply.getText().toString();
                mPresenter.doResponse(imUserId, remark);
                break;
            case R.id.tvReject:
                remark = etReply.getText().toString();
                mPresenter.doResponseReject(imUserId, remark);
                break;
        }
    }

    @Override
    public void showImUserInfo(TIMUserProfile profile) {

        CommonImageLoader.load(profile.getFaceUrl()).placeholder(R.drawable.default_user_image)
                .round().into(ivAvatar);
        tvNickName.setText(profile.getNickName());
        tvPhone.setText(getString(R.string.phone_num).concat("：").concat(profile.getIdentifier()));
        tvGenderAddress.setText(getGender(profile.getGender()).concat(" ").concat(profile.getLocation()));
        tvRemark.setText(getString(R.string.additional_information).concat(addWorld));
    }

    @Override
    public void doResponseResult(boolean isAgree) {
        CommonToast.toast(isAgree ? getString(R.string.had_agree) : getString(R.string.had_refuse));
        if (isAgree) {
//            C2CChatInfo chatInfo = C2CChatManager.getInstance().getC2CChatInfo(imUserId);
//            C2CChatManager.getInstance().setCurrentChatInfo(chatInfo);
//            MessageInfo messageInfo = MessageInfoUtil.buildAddFriendCustomMessage(imUserId);
//            C2CChatManager.getInstance().sendC2CMessage(messageInfo, false, null);
            String nickname = tvNickName.getText().toString();
            ChatActivity.startC2CChat(getCurContext(), imUserId, nickname,true);
            setResult(-1);
        }
        finish();
    }


    private String getGender(int g) {
        if (g == 0) {
            return getString(R.string.unknow);
        } else if (g == 1) {
            return getString(R.string.woman);
        } else {
            return getString(R.string.man);
        }
    }

    @Override
    protected RequestDetailPresenter createPresenter() {
        return new RequestDetailPresenter(this);
    }
}
