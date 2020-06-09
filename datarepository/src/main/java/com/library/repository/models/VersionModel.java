package com.library.repository.models;

public class VersionModel {


    /**
     * downUrl : http://www.mg.com/apppackage/app-release.apk
     * tip : 您的版本不是最新版本，请升级到最新版本！
     * content:更新内容
     */

    private String downUrl;
    private String tip;
    private String versionName;
    private int versionCode;
    private String content;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
