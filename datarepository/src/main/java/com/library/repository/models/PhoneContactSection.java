package com.library.repository.models;

import com.chad.library.adapter.base.entity.JSectionEntity;
import com.chad.library.adapter.base.entity.SectionEntity;

public class PhoneContactSection extends JSectionEntity {

    private boolean isHeader;
    private String header;
    private String display_name;
    private String firstName;
    private String mobile;

    public PhoneContactSection(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
    }

    public PhoneContactSection(boolean isHeader, String header, String display_name, String firstName, String mobile) {
        this.isHeader = isHeader;
        this.header = header;
        this.display_name = display_name;
        this.firstName = firstName;
        this.mobile = mobile;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    private int isExits;//0：不存在,1:存在，3：已经是好友
    public int getIsExits() {
        return isExits;
    }

    public void setIsExits(int isExits) {
        this.isExits = isExits;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public boolean isHeader() {
        return isHeader;
    }
}
