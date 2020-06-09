package com.library.repository.models;

import java.util.List;

public class PhoneContactListModel {
    private String smsTemplate;
    private List<RemoteContactModel> phoneList;

    public String getSmsTemplate() {
        return smsTemplate;
    }

    public void setSmsTemplate(String smsTemplate) {
        this.smsTemplate = smsTemplate;
    }

    public List<RemoteContactModel> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<RemoteContactModel> phoneList) {
        this.phoneList = phoneList;
    }
}
