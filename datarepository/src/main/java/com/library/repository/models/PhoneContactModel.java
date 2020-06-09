package com.library.repository.models;

public class PhoneContactModel {


    /**
     * display_name : 春花姑妈
     * firstName : 春花姑妈
     * mobile : 18759545066
     */

    private String display_name;
    private String firstName;
    private String mobile;

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
}
