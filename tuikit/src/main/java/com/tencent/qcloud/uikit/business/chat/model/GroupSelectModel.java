package com.tencent.qcloud.uikit.business.chat.model;

import com.chad.library.adapter.base.entity.JSectionEntity;
import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Author    by hanlz
 * Date      on 2019/12/2.
 * Description：
 */
public class GroupSelectModel extends JSectionEntity {

    private boolean isHeader;
    private String header;
    private TimGroupInfoModel item;

    public GroupSelectModel(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
    }

    public GroupSelectModel(boolean isHeader, String header, TimGroupInfoModel item) {
        this.isHeader = isHeader;
        this.header = header;
        this.item=item;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public boolean isHeader() {
        return isHeader;
    }

    public TimGroupInfoModel getItem() {
        return item;
    }

    public void setItem(TimGroupInfoModel item) {
        this.item = item;
    }
}
