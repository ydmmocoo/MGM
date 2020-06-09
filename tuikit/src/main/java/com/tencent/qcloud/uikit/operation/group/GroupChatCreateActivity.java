package com.tencent.qcloud.uikit.operation.group;

import com.library.common.base.BaseActivity;
import com.library.common.utils.StatusBarManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.qcloud.uikit.R;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupMemberInfo;
import com.tencent.qcloud.uikit.business.contact.model.ContactInfoBean;
import com.tencent.qcloud.uikit.business.contact.view.ContactList;

import java.util.ArrayList;

import butterknife.BindView;

public class GroupChatCreateActivity extends BaseActivity {


    ContactList mContactList;
    private ArrayList<GroupMemberInfo> mMembers = new ArrayList<>();

    @Override
    protected int layoutId() {
        return R.layout.group_create_activity;
    }

    @Override
    protected void initView() {
        StatusBarManager.setLightFontColor(this, com.library.common.R.color.colorAccent);
        GroupMemberInfo memberInfo = new GroupMemberInfo();
        memberInfo.setAccount(TIMManager.getInstance().getLoginUser());
        mMembers.add(0, memberInfo);

        mContactList = findViewById(R.id.group_create_member_list);
        mContactList.setSelectChangeListener(new ContactList.ContactSelectChangedListener() {
            @Override
            public void onSelectChanged(ContactInfoBean contact, boolean selected) {
                if (selected) {
                    GroupMemberInfo memberInfo = new GroupMemberInfo();
                    memberInfo.setAccount(contact.getIdentifier());
                    mMembers.add(memberInfo);
                } else {
                    for (int i = 0; i < mMembers.size(); i++) {
                        if (mMembers.get(i).getAccount().equals(contact.getIdentifier()))
                            mMembers.remove(i);
                    }

                }

            }
        });
    }
}
