package com.library.repository.models;

import java.util.List;

public class RecAppListModel {

    private List<AccessListBean> accessList;

    public List<AccessListBean> getAccessList() {
        return accessList;
    }

    public void setAccessList(List<AccessListBean> accessList) {
        this.accessList = accessList;
    }

    public static class AccessListBean {
        private String appId;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }
    }
}
