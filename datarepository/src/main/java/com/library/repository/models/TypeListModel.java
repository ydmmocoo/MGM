package com.library.repository.models;

import java.util.List;

public class TypeListModel {

    private List<TypeListBean> typeList;

    public List<TypeListBean> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<TypeListBean> typeList) {
        this.typeList = typeList;
    }

    public static class TypeListBean {
        private String tId;
        private String img;
        private String typeName;
        private Boolean cliclk = false;

        public Boolean getCliclk() {
            return cliclk;
        }

        public void setCliclk(Boolean cliclk) {
            this.cliclk = cliclk;
        }

        public String gettId() {
            return tId;
        }

        public void settId(String tId) {
            this.tId = tId;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }
}
