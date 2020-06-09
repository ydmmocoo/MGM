package com.library.repository.models;

import com.chad.library.adapter.base.entity.JSectionEntity;
import com.chad.library.adapter.base.entity.SectionEntity;

public class AreaCodeSectionModel extends JSectionEntity {

    private boolean isHeader;
    private String header;
    private AreaCodeModel data;

    public AreaCodeSectionModel(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
    }

    public AreaCodeSectionModel(boolean isHeader, String header, AreaCodeModel data) {
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

    public AreaCodeModel getData() {
        return data;
    }

    public void setData(AreaCodeModel data) {
        this.data = data;
    }

    @Override
    public boolean isHeader() {
        return isHeader;
    }
}