package com.tencent.qcloud.uikit.event;

/**
 * Author    by hanlz
 * Date      on 2020/3/3.
 * Description：
 */
public class ModifyEvent {
    private String nicename;

    public ModifyEvent(String nicename) {
        this.nicename = nicename;
    }

    public String getNicename() {
        return nicename;
    }

    public void setNicename(String nicename) {
        this.nicename = nicename;
    }
}
