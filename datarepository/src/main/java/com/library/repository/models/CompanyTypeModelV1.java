package com.library.repository.models;


import java.util.List;

public class CompanyTypeModelV1 {


    /**
     * cId : 1
     * name : 机械
     * secondList : [{"secondId":"5","secondName":"拖拉机"},{"secondId":"6","secondName":"摩托车"}]
     */

    private String cId;
    private String name;
    private List<SecondListBean> secondList;

    public String getCId() {
        return cId;
    }

    public void setCId(String cId) {
        this.cId = cId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SecondListBean> getSecondList() {
        return secondList;
    }

    public void setSecondList(List<SecondListBean> secondList) {
        this.secondList = secondList;
    }

    public static class SecondListBean {
        /**
         * secondId : 5
         * secondName : 拖拉机
         */

        private String secondId;
        private String secondName;

        public String getSecondId() {
            return secondId;
        }

        public void setSecondId(String secondId) {
            this.secondId = secondId;
        }

        public String getSecondName() {
            return secondName;
        }

        public void setSecondName(String secondName) {
            this.secondName = secondName;
        }
    }
}
