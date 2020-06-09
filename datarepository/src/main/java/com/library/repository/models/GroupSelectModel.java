package com.library.repository.models;

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
    private TimGroupInfoModel data;

    public GroupSelectModel(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
    }

    public GroupSelectModel(boolean isHeader, String header, TimGroupInfoModel data) {
        this.isHeader = isHeader;
        this.header = header;
        this.data = data;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public TimGroupInfoModel getData() {
        return data;
    }

    public void setData(TimGroupInfoModel data) {
        this.data = data;
    }

    @Override
    public boolean isHeader() {
        return isHeader;
    }
}
