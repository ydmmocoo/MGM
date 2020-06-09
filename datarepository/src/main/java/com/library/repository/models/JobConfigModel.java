package com.library.repository.models;

import java.util.List;


public class JobConfigModel {


    private List<CountryListBean> countryList;
    private List<JobTypesBean> jobTypes;
    private List<SexConfBean> sexConf;
    private List<EducationConfBean> educationConf;
    private List<ExpireTypeConfBean> expireTypeConf;
    private List<PayTypeBean> payType;
    private List<WorkYearTypeBean> workYearType;

    public List<CountryListBean> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<CountryListBean> countryList) {
        this.countryList = countryList;
    }

    public List<JobTypesBean> getJobTypes() {
        return jobTypes;
    }

    public void setJobTypes(List<JobTypesBean> jobTypes) {
        this.jobTypes = jobTypes;
    }

    public List<SexConfBean> getSexConf() {
        return sexConf;
    }

    public void setSexConf(List<SexConfBean> sexConf) {
        this.sexConf = sexConf;
    }

    public List<EducationConfBean> getEducationConf() {
        return educationConf;
    }

    public void setEducationConf(List<EducationConfBean> educationConf) {
        this.educationConf = educationConf;
    }

    public List<ExpireTypeConfBean> getExpireTypeConf() {
        return expireTypeConf;
    }

    public void setExpireTypeConf(List<ExpireTypeConfBean> expireTypeConf) {
        this.expireTypeConf = expireTypeConf;
    }

    public List<PayTypeBean> getPayType() {
        return payType;
    }

    public void setPayType(List<PayTypeBean> payType) {
        this.payType = payType;
    }

    public List<WorkYearTypeBean> getWorkYearType() {
        return workYearType;
    }

    public void setWorkYearType(List<WorkYearTypeBean> workYearType) {
        this.workYearType = workYearType;
    }

    public static class CountryListBean {
        /**
         * cId : 1
         * countryName : 马达加斯加
         * cityList : [{"cityId":"1","cityName":"塔那那利佛"},{"cityId":"2","cityName":"穆龙达瓦"}]
         */

        private String cId;
        private String countryName;
        private List<CityListBean> cityList;

        public String getCId() {
            return cId;
        }

        public void setCId(String cId) {
            this.cId = cId;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public List<CityListBean> getCityList() {
            return cityList;
        }

        public void setCityList(List<CityListBean> cityList) {
            this.cityList = cityList;
        }

        public static class CityListBean {
            /**
             * cityId : 1
             * cityName : 塔那那利佛
             */

            private String cityId;
            private String cityName;

            public String getCityId() {
                return cityId;
            }

            public void setCityId(String cityId) {
                this.cityId = cityId;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }
        }
    }

    public static class JobTypesBean {
        /**
         * typeId : 1
         * name : 行政
         */

        private String typeId;
        private String name;

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class SexConfBean {
        /**
         * sexId : 1
         * name : 男
         */

        private int sexId;
        private String name;

        public int getSexId() {
            return sexId;
        }

        public void setSexId(int sexId) {
            this.sexId = sexId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class EducationConfBean {
        /**
         * eId : 1
         * name : 小学
         */

        private String eId;
        private String name;

        public String getEId() {
            return eId;
        }

        public void setEId(String eId) {
            this.eId = eId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ExpireTypeConfBean {
        /**
         * eId : 1
         * expireType : ONE_WEEK
         */

        private String eId;
        private String expireType;

        public String getEId() {
            return eId;
        }

        public void setEId(String eId) {
            this.eId = eId;
        }

        public String getExpireType() {
            return expireType;
        }

        public void setExpireType(String expireType) {
            this.expireType = expireType;
        }
    }

    public static class PayTypeBean {
        /**
         * pId : 1
         * name : 1k~3k
         */

        private String pId;
        private String name;

        public String getPId() {
            return pId;
        }

        public void setPId(String pId) {
            this.pId = pId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class WorkYearTypeBean {
        /**
         * wId : 1
         * name : 1年~2年
         */

        private String wId;
        private String name;

        public String getWId() {
            return wId;
        }

        public void setWId(String wId) {
            this.wId = wId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
