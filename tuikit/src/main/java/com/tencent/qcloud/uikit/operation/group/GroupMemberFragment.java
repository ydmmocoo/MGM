package com.tencent.qcloud.uikit.operation.group;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.qcloud.uikit.R;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupChatManager;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupMemberInfo;
import com.tencent.qcloud.uikit.business.chat.group.view.GroupMemberPanel;
import com.tencent.qcloud.uikit.business.chat.group.view.widget.GroupMemberCallback;
import com.tencent.qcloud.uikit.common.BaseFragment;
import com.tencent.qcloud.uikit.common.IUIKitCallBack;
import com.tencent.qcloud.uikit.common.UIKitConstants;
import com.tencent.qcloud.uikit.common.component.titlebar.PageTitleBar;
import com.tencent.qcloud.uikit.common.utils.UIUtils;

import java.util.List;


public class GroupMemberFragment extends BaseFragment {
    private GroupMemberPanel mMemberPanel;
    private View mBaseView;
    private String groupId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.group_fragment_members, container, false);
        mMemberPanel = mBaseView.findViewById(R.id.group_member_grid_panel);
        mMemberPanel.setGroupMemberCallback(new GroupMemberCallback() {
            @Override
            public void onMemberRemove(GroupMemberInfo memberInfo, List<GroupMemberInfo> members) {
//                mMemberPanel.getTitleBar().setTitle("群成员(" + members.size() + ")", PageTitleBar.POSITION.LEFT);
            }
        });
        groupId = getArguments().getString(UIKitConstants.GROUP_ID);
        init();
        return mBaseView;
    }

    private void init() {

        GroupChatManager.getInstance().loadGroupMembersRemote(groupId, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                List<GroupMemberInfo> members = (List<GroupMemberInfo>) data;
                for (int i = 0; i < members.size(); i++) {
                    TIMUserProfile timUserProfile = TIMFriendshipManager.getInstance().queryUserProfile(members.get(i).getAccount());
                    if (timUserProfile == null) {
                        continue;
                    }
                    if (!TextUtils.isEmpty(timUserProfile.getNickName())) {
                        members.get(i).setAccount(timUserProfile.getNickName());
                    }
                    if (!TextUtils.isEmpty(timUserProfile.getFaceUrl())){
                        members.get(i).setIconUrl(timUserProfile.getFaceUrl());
                    }
                }
                mMemberPanel.setMembers(members);
//                mMemberPanel.getTitleBar().setTitle("群成员(" + members.size() + ")", PageTitleBar.POSITION.CENTER);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                UIUtils.toastLongMessage(errCode + ":" + errMsg);
            }
        });

        mMemberPanel.setMemberPanelEvent(new GroupMemberPanel.GroupMemberPanelEvent() {
            @Override
            public void backBtnClick() {
               getActivity().finish();
            }

            @Override
            public void addMemberBtnClick() {
                forward(new GroupInvitelMemberFragment(), false);
            }

            @Override
            public void delMemberBtnClick() {
                forward(new GroupDelMemberFragment(), false);
            }
        });

    }
}
