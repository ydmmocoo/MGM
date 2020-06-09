package com.library.repository.models;

/**
 * Author    by Administrator
 * Date      on 2019/10/17.
 * Description：
 */
public class NearbyCityCompanyListModel {

    private String cId;//公司记录id
    private String title;//名称
    private String img;//封面图片

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
