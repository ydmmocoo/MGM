package com.tencent.qcloud.uikit.operation.group.event;

/**
 * Author    by hanlz
 * Date      on 2019/12/5.
 * Description：
 */
public class GroupShowNicknameEvent {
    private boolean state;

    public GroupShowNicknameEvent(boolean b) {
        this.state = b;
    }

    public boolean isState() {
        return state;
    }
}
