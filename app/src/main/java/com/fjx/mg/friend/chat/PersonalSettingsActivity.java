package com.fjx.mg.friend.chat;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.fjx.mg.friend.imuser.NewImUserDetailActivity;
import com.fjx.mg.setting.feedback.FeedBackActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.repository.models.TimGroupInfoModel;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.qcloud.uikit.business.chat.model.CacheUtil;
import com.tencent.qcloud.uikit.common.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.uikit.common.utils.PopWindowUtil;
import com.tencent.qcloud.uikit.common.widget.ShadeImageView;
import com.tencent.qcloud.uikit.operation.c2c.mvp.PersonalChatSettingsContact;
import com.tencent.qcloud.uikit.operation.c2c.mvp.PersonalChatSettingsPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author    by hanlz
 * Date      on 2020/3/10.
 * Description：个人设置页
 */
public class PersonalSettingsActivity extends BaseMvpActivity<PersonalChatSettingsPresenter> implements PersonalChatSettingsContact.View {

    @BindView(R.id.personalIcon)
    ShadeImageView mPersonalIcon;
    @BindView(R.id.tvNickname)
    TextView mTvNickname;
    @BindView(R.id.cbChatTop)
    CheckBox mCbChatTop;

    private String mUId;
    private String mIconUrl;
    private String mNickname;

    public static Intent newIntent(Context context, String userId, String nickname) {
        Intent intent = new Intent(context, PersonalSettingsActivity.class);
        intent.putExtra(IntentConstants.UID, userId);
        intent.putExtra(IntentConstants.NICKNAME, nickname);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_personal_settings;
    }

    @Override
    protected void initView() {
        super.initView();
        if (getIntent() == null) {
            return;
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ToolBarManager.with(this).setTitle(getString(R.string.chat_details));
        mUId = getIntent().getStringExtra(IntentConstants.UID);
        mIconUrl = CacheUtil.getInstance().userAvatar(mUId);
        mNickname = getIntent().getStringExtra(IntentConstants.NICKNAME);
        if (!TextUtils.isEmpty(mIconUrl))
            GlideEngine.loadImage(mPersonalIcon, mIconUrl, null);
        mTvNickname.setText(mNickname);

        mCbChatTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mPresenter.setTopSession(b, mUId);
            }
        });
        mPresenter.setChecked(mUId, mCbChatTop);
    }

    @OnClick({R.id.ivStartGroup, R.id.tvChatClear, R.id.tvComplaint, R.id.personalIcon})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.personalIcon:
                startActivity(NewImUserDetailActivity.newInstance(getCurContext(), mUId));
                break;
            case R.id.ivStartGroup:
                TimGroupInfoModel model = new TimGroupInfoModel();
                model.setIdentifier(mUId);
                model.setNickName(mNickname);
                model.setFaceUrl(mIconUrl);
                model.setSelected(true);
                startActivity(StartGroupChatActivity.newIntent(getCurContext(), JsonUtil.moderToString(model)));
                break;
            case R.id.tvChatClear:
                PopWindowUtil.buildEnsureDialog(getCurContext(), getString(com.tencent.qcloud.uikit.R.string.confirm_clear_chat_history), "", getString(com.tencent.qcloud.uikit.R.string.cancel), getString(com.tencent.qcloud.uikit.R.string.sure), new PopWindowUtil.EnsureListener() {
                    @Override
                    public void sure(Object obj) {
                        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, mUId);
                        conversation.deleteLocalMessage(new TIMCallBack() {
                            @Override
                            public void onError(int i, String s) {

                            }

                            @Override
                            public void onSuccess() {
                                CommonToast.toast(com.tencent.qcloud.uikit.R.string.clear_success);
                                finish();
                                SettingsEvent settingsEvent = new SettingsEvent();
                                settingsEvent.setType("2");
                                EventBus.getDefault().post(settingsEvent);
                            }
                        });
                    }

                    @Override
                    public void cancel() {

                    }
                });
                break;
            case R.id.tvComplaint:
                startActivity(FeedBackActivity.newInstance(getCurContext()));
                break;
            default:
                break;
        }
    }

    @Override
    protected PersonalChatSettingsPresenter createPresenter() {
        return new PersonalChatSettingsPresenter(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SettingsEvent event) {
        if (TextUtils.equals("1", event.getType())) {
            SettingsEvent settingsEvent = new SettingsEvent();
            settingsEvent.setType("2");
            EventBus.getDefault().post(settingsEvent);
            finish();
        }
    }

    @Override
    public void queryUserProfileSuc(TIMUserProfile profile) {
    }
}
