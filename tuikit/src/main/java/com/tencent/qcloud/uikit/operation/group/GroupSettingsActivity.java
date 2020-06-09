package com.tencent.qcloud.uikit.operation.group;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.library.common.base.BaseMvpActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonToast;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.ext.group.TIMGroupSelfInfo;
import com.tencent.qcloud.uikit.R;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupChatInfo;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupChatManager;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupMemberInfo;
import com.tencent.qcloud.uikit.business.chat.group.view.widget.GroupMemberControlAdapter;
import com.tencent.qcloud.uikit.business.chat.group.view.widget.GroupMemberControler;
import com.tencent.qcloud.uikit.common.UIKitConstants;
import com.tencent.qcloud.uikit.common.component.titlebar.PageTitleBar;
import com.tencent.qcloud.uikit.common.utils.PopWindowUtil;
import com.tencent.qcloud.uikit.common.utils.UIUtils;
import com.tencent.qcloud.uikit.common.widget.GroupModificationDialog;
import com.tencent.qcloud.uikit.common.widget.GroupNicknameModificationDialog;
import com.tencent.qcloud.uikit.operation.group.event.GroupShowNicknameEvent;
import com.tencent.qcloud.uikit.operation.group.mvp.GrouChatSettingsPresenter;
import com.tencent.qcloud.uikit.operation.group.mvp.GroupChatSettingsContact;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/12/5.
 * Description：群聊功能设置(项目留下的坑 tuikit module无法导入datarepositiory module 废弃)
 * <p>
 * 详见 com.fjx.mg.friend.chat.GroupSettingsActivity
 */
@Deprecated
public class GroupSettingsActivity extends BaseMvpActivity<GrouChatSettingsPresenter> implements GroupChatSettingsContact.View, View.OnClickListener {

    private TextView mDissolveBtn;
    private PageTitleBar mTitleBar;

    private GridView mMembersGrid;
    private GroupChatInfo mGroupInfo;
    private GroupMemberControlAdapter mMemberAdapter;
    private LinearLayout mLlMore;
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
    private String mGroupId;//群组id

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


        mLlMore = findViewById(R.id.llMore);//查看更多群成员
        mLlMore.setOnClickListener(this);

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
        mTitleBar = findViewById(R.id.group_info_title_bar);
        mTitleBar.getRightGroup().setVisibility(View.GONE);
        mTitleBar.setTitle(getResources().getString(R.string.group_detail), PageTitleBar.POSITION.CENTER);
        mTitleBar.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initMemberList();
        mTvGroupName = findViewById(R.id.tvGroupName);
        mTvGroupName.setText(mGroupInfo.getGroupName());

        mRlGroupChatFaceIcon = findViewById(R.id.rlGroupChatFaceIcon);
        mRlGroupChatFaceIcon.setOnClickListener(this);
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
        mCbMemberNickname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                EventBus.getDefault().post(new GroupShowNicknameEvent(b));
            }
        });

        mTvChatClear = findViewById(R.id.tvChatClear);
        mTvChatClear.setOnClickListener(this);
    }

    private void initMemberListData(List<GroupMemberInfo> memberDetails) throws NullPointerException {
        for (int i = 0; i < memberDetails.size(); i++) {
            TIMUserProfile timUserProfile = TIMFriendshipManager.getInstance().queryUserProfile(memberDetails.get(i).getAccount());
            if (timUserProfile != null) {
                String name = "";
                String nickName = timUserProfile.getNickName();
                if (!TextUtils.isEmpty(nickName)) {
                    name = nickName;
                } else {
                    name = timUserProfile.getIdentifier();
                }
                memberDetails.get(i).setAccount(name);
                memberDetails.get(i).setIconUrl(timUserProfile.getFaceUrl());
            }
        }
        mMemberAdapter.setDataSource(memberDetails);
    }

    @Override
    public void setGroupName(String name) {
        UIUtils.toastLongMessage(getString(R.string.modify_group_name_success));
        mTvGroupName.setText(name);
    }

    @Override
    public void setNickname(String name) {
        UIUtils.toastLongMessage(getResources().getString(R.string.modify_nickname_success));
        mTvGroupNickname.setText(name);
    }

    @Override
    public void setNameCard(TIMGroupSelfInfo timGroupSelfInfo) {
        mTvGroupNickname.setText(timGroupSelfInfo.getNameCard());
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

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rlContainer) {//修改公告
            Intent intent = new Intent(getCurContext(), GroupNoticeActivity.class);
            intent.putExtra(IntentConstants.GROUP_ID, mGroupInfo.getPeer());
            startActivityForResult(intent, 233);
        } else if (view.getId() == R.id.llGroupChatName) {
            GroupModificationDialog dialog = new GroupModificationDialog();
            dialog.show(getSupportFragmentManager(), "groupName");
            dialog.setOnMonModificationListener(new GroupModificationDialog.onModificationListener() {
                @Override
                public void onSure(String name) {
                    mPresenter.modifyGroupName(name);
                }
            });
        } else if (view.getId() == R.id.llNickname) {
            GroupNicknameModificationDialog instance = new GroupNicknameModificationDialog();
            instance.show(getSupportFragmentManager(), "nicname");
            instance.setOnMonModificationListener(new GroupNicknameModificationDialog.onModificationListener() {
                @Override
                public void onSure(String name) {
                    mPresenter.modifyGroupNickname(name);
                }
            });
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

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 233 && resultCode == -1) {
            String notice = data.getStringExtra(IntentConstants.EXT);
            if (!TextUtils.isEmpty(notice))
                mTvGroupBroadcastContent.setText(notice);
        } else if (requestCode == 666) {
            mPresenter.refreshMemberList(mGroupId);
        }
    }

}
