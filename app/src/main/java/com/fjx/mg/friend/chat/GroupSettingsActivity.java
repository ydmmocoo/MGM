package com.fjx.mg.friend.chat;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fjx.mg.dialog.PickImageDialog;
import com.fjx.mg.dialog.TipsDialog;
import com.fjx.mg.image.ImageActivity;
import com.fjx.mg.utils.MessageSateSpUtil;
import com.fjx.mg.utils.NicknameSateSpUtil;
import com.google.gson.JsonObject;
import com.library.common.base.BaseMvpActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.MultipartBodyHBuilder;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.library.repository.util.LogUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfoResult;
import com.tencent.imsdk.ext.group.TIMGroupSelfInfo;
import com.tencent.qcloud.uikit.R;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupChatInfo;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupChatManager;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupMemberInfo;
import com.tencent.qcloud.uikit.business.chat.group.view.widget.GroupMemberControlAdapter;
import com.tencent.qcloud.uikit.business.chat.group.view.widget.GroupMemberControler;
import com.tencent.qcloud.uikit.common.UIKitConstants;
import com.tencent.qcloud.uikit.common.utils.PopWindowUtil;
import com.tencent.qcloud.uikit.common.utils.UIUtils;
import com.tencent.qcloud.uikit.operation.group.GroupDeleteMemberActivity;
import com.tencent.qcloud.uikit.operation.group.GroupInvitelMemberActivity;
import com.tencent.qcloud.uikit.operation.group.GroupMemberActivity;
import com.tencent.qcloud.uikit.operation.group.GroupNicknameActivity;
import com.tencent.qcloud.uikit.operation.group.GroupNoticeActivity;
import com.tencent.qcloud.uikit.operation.group.mvp.GrouChatSettingsPresenter;
import com.tencent.qcloud.uikit.operation.group.mvp.GroupChatSettingsContact;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Author    by hanlz
 * Date      on 2019/12/5.
 * Description：群聊功能设置
 */
public class GroupSettingsActivity extends BaseMvpActivity<GrouChatSettingsPresenter> implements GroupChatSettingsContact.View, View.OnClickListener {

    private final int BRAODCAST_REQUESTCODE = 12;
    private final int NICKNAME_REQUESTCODE = 13;
    private final int NAME_REQUESTCODE = 14;

    private TextView mDissolveBtn;
//    private PageTitleBar mTitleBar;

    private GridView mMembersGrid;
    private GroupChatInfo mGroupInfo;
    private GroupMemberControlAdapter mMemberAdapter;
    private LinearLayout mLlNickname;
    private LinearLayout mLlGroupChatName;
    private TextView mTvGroupBroadcastContent;
    private RelativeLayout mRlContainer;
    private TextView mTvGroupName;
    private TextView mTvGroupNickname;
    private CheckBox mCbChatTop;
    private CheckBox mCbMemberNickname;
    private TextView mTvChatClear;
    private RelativeLayout mRlGroupChatFaceIcon;
    private TextView mTvMore;
    private String mGroupId;//群组id
    private BasePopupView popupView;
    private List<GroupMemberInfo> mMemberDetails;
    private ImageView mIvAvatar;
    private CheckBox mCbNoDisturb;
    private MessageSateSpUtil util = new MessageSateSpUtil();
    private LinearLayout mLlMoreContanier;
    //    private ImageView mIvMore;
    private String faceUrl;

    @Override
    protected GrouChatSettingsPresenter createPresenter() {
        return new GrouChatSettingsPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.act_group_settings;
    }

    @Override
    protected void initView() {
        super.initView();
        mGroupId = getIntent().getStringExtra(UIKitConstants.GROUP_ID);
        mGroupInfo = GroupChatManager.getInstance().getCurrentChatInfo();
        mPresenter.getSelfInfo(mGroupId);


        mTvMore = findViewById(R.id.tvMore);//查看更多群成员
        mTvMore.setOnClickListener(this);

        mTvGroupBroadcastContent = findViewById(R.id.tvGroupBroadcastContent);
        mTvGroupBroadcastContent.setText(mGroupInfo.getNotice());

        mRlContainer = findViewById(R.id.rlContainer);
        mRlContainer.setOnClickListener(this);


        mLlNickname = findViewById(R.id.llNickname);
        mLlNickname.setOnClickListener(this);

        mTvGroupNickname = findViewById(R.id.tvGroupNickname);

        mLlGroupChatName = findViewById(R.id.llGroupChatName);
        mLlGroupChatName.setOnClickListener(this);

        mDissolveBtn = findViewById(R.id.tvExitChat);
        mDissolveBtn.setOnClickListener(this);
        mIvAvatar = findViewById(R.id.ivAvatar);
        initGroupIcon();
        initMemberList();
        mTvGroupName = findViewById(R.id.tvGroupName);
        mTvGroupName.setText(mGroupInfo.getGroupName());

        mRlGroupChatFaceIcon = findViewById(R.id.rlGroupChatFaceIcon);
        mRlGroupChatFaceIcon.setOnClickListener(this);
        findViewById(R.id.page_title_left_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.group.state");
                intent.putExtra("state", mCbMemberNickname.isChecked());
                sendBroadcast(intent);
                NicknameSateSpUtil util = new NicknameSateSpUtil();
                util.put(mGroupId, mCbMemberNickname.isChecked());
                finish();
            }
        });
        mCbNoDisturb = findViewById(R.id.cbNoDisturb);//消息免打扰
        mCbNoDisturb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mPresenter.setReceiverMessagePpt(mGroupId, TIMManager.getInstance().getLoginUser(), b);
            }
        });
        mCbNoDisturb.setChecked(util.get(mGroupId + TIMManager.getInstance().getLoginUser()));

//        mIvMore = findViewById(R.id.ivmore
//        if (mGroupInfo.isOwner()) {
//            mIvMore.setVisibility(View.VISIBLE);
//        } else {
//            mIvMore.setVisibility(View.GONE);
//        }
//        mIvMore.setOnClickListener(this);
        mIvAvatar.setOnClickListener(this);
    }

    private void initGroupIcon() {
        List<String> infos = new ArrayList<>();
        infos.add(mGroupId);
        TIMGroupManager.getInstance().getGroupInfo(infos, new TIMValueCallBack<List<TIMGroupDetailInfoResult>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMGroupDetailInfoResult> timGroupDetailInfoResults) {

                faceUrl = timGroupDetailInfoResults.get(0).getFaceUrl();
                CommonImageLoader.load(faceUrl).placeholder(R.drawable.group_chat_header_ic).into(mIvAvatar);
            }
        });
    }

    private void initMemberList() {
        mMembersGrid = findViewById(R.id.group_members);
        mMemberAdapter = new GroupMemberControlAdapter();
        mMembersGrid.setAdapter(mMemberAdapter);
        List<GroupMemberInfo> memberDetails = mGroupInfo.getMemberDetails();
        initMemberListData(memberDetails);
        mMemberAdapter.setControler(new GroupMemberControler() {
            @Override
            public void detailMemberControl() {

            }

            @Override
            public void addMemberControl() {
                startActivityForResult(GroupInvitelMemberActivity.newIntent(getCurContext(), mGroupInfo.getPeer()), 666);
            }

            @Override
            public void delMemberControl() {
                startActivityForResult(GroupDeleteMemberActivity.newIntent(getCurActivity(), mGroupInfo.getPeer()), 666);
            }
        });
        mDissolveBtn.setText(R.string.exit_group);

        mCbChatTop = findViewById(R.id.cbChatTop);
        mCbChatTop.setChecked(mGroupInfo.isTopChat());
        mCbChatTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mPresenter.setTopSession(b);
            }
        });

        //显示成员昵称
        mCbMemberNickname = findViewById(R.id.cbMemberNickname);
        NicknameSateSpUtil util = new NicknameSateSpUtil();
        mCbMemberNickname.setChecked(util.get(mGroupId));

        mTvChatClear = findViewById(R.id.tvChatClear);
        mTvChatClear.setOnClickListener(this);
    }

    private void initMemberListData(final List<GroupMemberInfo> memberDetails) throws NullPointerException {
        for (int i = 0; i < memberDetails.size(); i++) {
            final GroupMemberInfo groupMemberInfo = memberDetails.get(i);
            TIMUserProfile timUserProfile = TIMFriendshipManager.getInstance().queryUserProfile(memberDetails.get(i).getAccount());
            if (timUserProfile != null) {
                String name = "";
                String nickName = timUserProfile.getNickName();
                if (!TextUtils.isEmpty(nickName)) {
                    name = nickName;
                } else {
                    name = timUserProfile.getIdentifier();
                }
                groupMemberInfo.setId(memberDetails.get(i).getAccount());
                groupMemberInfo.setAccount(name);
                groupMemberInfo.setIconUrl(timUserProfile.getFaceUrl());
            } else {
                final ArrayList<String> ids = new ArrayList<>();
                ids.add(memberDetails.get(i).getAccount());
                TIMFriendshipManager.getInstance().getUsersProfile(ids, false, new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                        String name = "";
                        String nickName = timUserProfiles.get(0).getNickName();
                        if (!TextUtils.isEmpty(nickName)) {
                            name = nickName;
                        } else {
                            name = timUserProfiles.get(0).getIdentifier();
                        }
                        groupMemberInfo.setAccount(name);
                        groupMemberInfo.setIconUrl(timUserProfiles.get(0).getFaceUrl());
                    }
                });
            }
        }
        for (int i = 0; i < memberDetails.size(); i++) {
            if (mGroupInfo.getOwner().equals(memberDetails.get(i).getId())) {
                //群主
                GroupMemberInfo groupMemberInfo = memberDetails.get(i);
                memberDetails.remove(i);
                memberDetails.add(0, groupMemberInfo);
            }
        }
        mMemberAdapter.setDataSource(memberDetails, mGroupInfo.isOwner());
        mMemberDetails = memberDetails;
        if (memberDetails.size() > 8) {
            mTvMore.setVisibility(View.VISIBLE);
        } else {
            mTvMore.setVisibility(View.GONE);
        }

    }

    @Override
    public void setGroupName(String name) {

    }

    @Override
    public void setNickname(String name) {

    }

    @Override
    public void setNameCard(TIMGroupSelfInfo timGroupSelfInfo) {
        mTvGroupNickname.setText(timGroupSelfInfo.getNameCard());
        mTvGroupNickname.setVisibility(View.VISIBLE);
    }

    @Override
    public void getGroupChatInfo(GroupChatInfo groupChatInfo) {
        initMemberListData(groupChatInfo.getMemberDetails());
    }

    @Override
    public void getTimUserProfile(List<TIMUserProfile> datas) {

    }

    @Override
    public void messageSate(String groupId, String userId, boolean open) {
        util.put(groupId + userId, open);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent("com.group.state");
        intent.putExtra("state", mCbMemberNickname.isChecked());
        sendBroadcast(intent);
        NicknameSateSpUtil util = new NicknameSateSpUtil();
        util.put(mGroupId, mCbMemberNickname.isChecked());
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rlContainer) {//修改公告
            Intent intent = new Intent(getCurContext(), GroupNoticeActivity.class);
            intent.putExtra(IntentConstants.GROUP_ID, mGroupInfo.getPeer());
            intent.putExtra(IntentConstants.CONTENT, mTvGroupBroadcastContent.getText().toString());
            intent.putExtra(IntentConstants.IS_OWNER, mGroupInfo.isOwner());
            GroupSettingsActivity.this.startActivityForResult(intent, BRAODCAST_REQUESTCODE);
        } else if (view.getId() == R.id.llGroupChatName) {//群名称
            if (mGroupInfo.isOwner()) {
                GroupSettingsActivity.this.startActivityForResult(GroupNicknameActivity.newIntent(getCurContext(), mGroupInfo.getPeer(),
                        getString(R.string.please_modification_group_name), 2, mTvGroupName.getText().toString()), NAME_REQUESTCODE);
            } else {
                //非群主不能修改群名称
                TipsDialog dialog = new TipsDialog(this, getString(com.fjx.mg.R.string.only_master_modify_group_name));
                dialog.show();
            }
        } else if (view.getId() == R.id.llNickname) {//群昵称
            GroupSettingsActivity.this.startActivityForResult(GroupNicknameActivity.newIntent(getCurContext(), mGroupInfo.getPeer(),
                    getString(R.string.please_modification_group_nickname), 1, mTvGroupNickname.getText().toString()), NICKNAME_REQUESTCODE);
        } else if (view.getId() == R.id.llMore) {//查看更多成员
            startActivity(GroupMemberActivity.newIntent(getCurContext(), mGroupInfo.getPeer()));
        } else if (view.getId() == R.id.tvExitChat) {
            if (mGroupInfo == null)
                return;
            PopWindowUtil.buildEnsureDialog(getCurContext(), getString(R.string.confirm_exit_group_chat), "", getString(R.string.cancel), getString(R.string.sure), new PopWindowUtil.EnsureListener() {
                @Override
                public void sure(Object obj) {
                    mPresenter.quiteGroup(mGroupInfo.getPeer());
                }

                @Override
                public void cancel() {

                }
            });
        } else if (view.getId() == R.id.tvChatClear) {
            PopWindowUtil.buildEnsureDialog(getCurContext(), getString(R.string.confirm_clear_chat_history), "", getString(R.string.cancel), getString(R.string.sure), new PopWindowUtil.EnsureListener() {
                @Override
                public void sure(Object obj) {
                    TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, mGroupInfo.getPeer());
                    conversation.deleteLocalMessage(new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {

                        }

                        @Override
                        public void onSuccess() {
                            CommonToast.toast(R.string.clear_success);
                            finish();
                        }
                    });
                }

                @Override
                public void cancel() {

                }
            });

        } else if (view.getId() == R.id.rlGroupChatFaceIcon) {
            if (mGroupInfo.isOwner()) {
                if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showPickPhotoDialog();
                } else {
                    EasyPermissions.requestPermissions(this, getString(com.fjx.mg.R.string.permission_camata_sd),
                            1, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            } else {
                TipsDialog dialog = new TipsDialog(this, getString(com.fjx.mg.R.string.only_master_modify_group_header));
                dialog.show();
            }
        } else if (view.getId() == R.id.ivAvatar) {
            List<String> faceUrls = new ArrayList<>();
            faceUrls.add(faceUrl);
            if (!TextUtils.isEmpty(faceUrl)) {
                startActivity(ImageActivity.newInstance(getCurActivity(), JsonUtil.moderToString(faceUrls), 0));
            } else {
                startActivity(ImageActivity.newInstance(getCurActivity(), JsonUtil.moderToString(faceUrls), 0, true));
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BRAODCAST_REQUESTCODE && resultCode == 1) {
            if (data != null) {
                String notice = data.getStringExtra("message");
                if (!TextUtils.isEmpty(notice))
                    mTvGroupBroadcastContent.setText(notice);
            }
        }
        if (requestCode == NICKNAME_REQUESTCODE && resultCode == 1) {
            if (data != null) {
                String name = data.getStringExtra("message");
                if (!TextUtils.isEmpty(name)) {
                    UIUtils.toastLongMessage(getResources().getString(R.string.modify_nickname_success));
                    mTvGroupNickname.setText(name);
                    mTvGroupNickname.setVisibility(View.VISIBLE);
                }
            }
        }

        if (requestCode == NAME_REQUESTCODE && resultCode == 1) {
            if (data != null) {
                String name = data.getStringExtra("message");
                if (!TextUtils.isEmpty(name)) {
                    mTvGroupName.setText(name);
                    UIUtils.toastLongMessage(getString(R.string.modify_group_name_success));
                }
            }
        }

        if (requestCode == 666) {
            mPresenter.refreshMemberList(mGroupId);
        }

        if (requestCode == PictureConfig.CHOOSE_REQUEST) {

            // 图片、视频、音频选择结果回调
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList.size() > 0) {
                String path = selectList.get(0).getCompressPath();

                updateImage(path);
            }
        }
    }

    public void showPickPhotoDialog() {
        PickImageDialog dialog = new PickImageDialog(getCurContext());
        dialog.withAspectRatio(1, 1);


        popupView = new XPopup.Builder(getCurContext())
                .asCustom(dialog)
                .show();
    }

    void updateImage(String filePath) {
        List<MultipartBody.Part> requestBody
                = MultipartBodyHBuilder.Builder()
                .addParams("k", "avatar")
                .addParams("avatar", new File(filePath))
                .builder();
        showLoading();
        RepositoryFactory.getRemoteRepository().uploadFile(requestBody)
                .compose(RxScheduler.<ResponseModel<JsonObject>>toMain())
                .subscribe(new CommonObserver<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject data) {
                        hideLoading();
                        Map<String, Object> map = JsonUtil.jsonToMap(data.toString());
                        String avatar = (String) map.get("avatar");
                        faceUrl = Constant.getHost() + avatar.substring(1, avatar.length());
                        CommonImageLoader.load(Constant.getHost() + avatar.substring(1, avatar.length())).placeholder(R.drawable.group_chat_header_ic).into(mIvAvatar);
                        mPresenter.modifyGroupFaceIcon(mGroupInfo.getPeer(), faceUrl);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }
}
