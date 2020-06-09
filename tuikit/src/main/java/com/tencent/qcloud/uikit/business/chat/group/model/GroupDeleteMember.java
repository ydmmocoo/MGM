package com.tencent.qcloud.uikit.business.chat.group.model;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/12/5.
 * Description：
 */
public class GroupDeleteMember {
    private List<GroupMemberInfo> list;

    public List<GroupMemberInfo> getList() {
        return list;
    }

    public void setList(List<GroupMemberInfo> list) {
        this.list = list;
    }
}
