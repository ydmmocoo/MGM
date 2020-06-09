package com.common.sharesdk;

/**
 * Author    by hanlz
 * Date      on 2020/3/17.
 * Description：
 */
public class ShareOpenActEvent {
    private String extraMap;

    public ShareOpenActEvent(String extraMap) {
        this.extraMap = extraMap;
    }

    public String getExtraMap() {
        return extraMap;
    }

    public void setExtraMap(String extraMap) {
        this.extraMap = extraMap;
    }
}
