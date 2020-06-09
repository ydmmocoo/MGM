package com.library.repository.models;

/**
 * Author    by hanlz
 * Date      on 2020/3/9.
 * Description：用户发布的黄页列表
 */
public class MomentsCompanyImagesModel {
    private String cId;//记录id
    private String imgUrl;//图片地址

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
