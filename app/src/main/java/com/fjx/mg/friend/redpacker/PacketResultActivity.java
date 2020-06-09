package com.fjx.mg.friend.redpacker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.fjx.mg.R;
import com.library.common.base.BaseActivity;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StatusBarManager;
import com.library.repository.data.UserCenter;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.business.chat.model.ElemExtModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PacketResultActivity extends BaseActivity {

    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvNickName)
    TextView tvNickName;
    @BindView(R.id.tvRemark)
    TextView tvRemark;
    @BindView(R.id.tvMoney)
    TextView tvMoney;

    public static Intent newInstance(Context context, String uid, String messageInfo) {
        Intent intent = new Intent(context, PacketResultActivity.class);
        intent.putExtra("ElemExtModel", messageInfo);
        intent.putExtra("uid", uid);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_packet_result;
    }

    @Override
    protected void initView() {

        StatusBarManager.setLightFontColor(this, R.color.colorRed);
        String uid = getIntent().getStringExtra("uid");
        String json = getIntent().getStringExtra("ElemExtModel");
        ElemExtModel info = JsonUtil.strToModel(json, ElemExtModel.class);
        TIMFriend friend = UserCenter.getFriend(uid);
        if (friend == null) {
            finish();
            return;
        }

        CommonImageLoader.load(friend.getTimUserProfile().getFaceUrl());
        if (!TextUtils.isEmpty(friend.getRemark())) {
            tvNickName.setText(friend.getRemark());
        } else if (!TextUtils.isEmpty(friend.getTimUserProfile().getNickName())) {
            tvNickName.setText(friend.getTimUserProfile().getNickName());
        } else {
            tvNickName.setText(friend.getIdentifier());
        }
        tvMoney.setText(info.getMoney());
        tvRemark.setText(info.getRemark());

    }


    @OnClick(R.id.ivClose)
    public void onViewClicked() {
        finish();
    }
}
