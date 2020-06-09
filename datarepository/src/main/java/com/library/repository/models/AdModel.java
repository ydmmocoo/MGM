package com.library.repository.models;

public class AdModel {



    /**
     * aId : 1
     * img : http://47.97.159.184/Uploads/ad/2019-07-22/5d3568a72122e.jpg
     * jumpType : 2
     * path :
     * url : https://www.messageglobal-online.com/invite/index
     * title :
     */

    private String aId;
    private String img;
    private String jumpType;//点击广告跳转类型,1：app内页，2：外链跳转，3：跳转详情'
    private String path;
    private String url;
    private String title;
    private String androidPath;

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getAndroidPath() {
        return androidPath;
    }

    public void setAndroidPath(String androidPath) {
        this.androidPath = androidPath;
    }

    public String getAId() {
        return aId;
    }

    public void setAId(String aId) {
        this.aId = aId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getJumpType() {
        return jumpType;
    }

    public void setJumpType(String jumpType) {
        this.jumpType = jumpType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
