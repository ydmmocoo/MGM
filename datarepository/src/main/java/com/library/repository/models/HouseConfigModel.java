package com.library.repository.models;

import java.util.List;

public class HouseConfigModel {


    private List<LaguageConfBean> laguageConf;
    private List<LayoutConfBean> layoutConf;
    private List<SexConfBean> sexConf;
    private List<JobTypeBean> jobTypes;
    private List<CountryListBean> countryList;
    private List<EducationListBean> educationConf;
    private List<ExpireTypeBean> expireTypeConf;


    public List<LaguageConfBean> getLaguageConf() {
        return laguageConf;
    }

    public void setLaguageConf(List<LaguageConfBean> laguageConf) {
        this.laguageConf = laguageConf;
    }

    public List<EducationListBean> getEducationConf() {
        return educationConf;
    }

    public void setEducationConf(List<EducationListBean> educationConf) {
        this.educationConf = educationConf;
    }

    public List<LayoutConfBean> getLayoutConf() {
        return layoutConf;
    }

    public List<JobTypeBean> getJobTypes() {
        return jobTypes;
    }

    public void setJobTypes(List<JobTypeBean> jobTypes) {
        this.jobTypes = jobTypes;
    }

    public void setLayoutConf(List<LayoutConfBean> layoutConf) {
        this.layoutConf = layoutConf;
    }

    public List<SexConfBean> getSexConf() {
        return sexConf;
    }

    public void setSexConf(List<SexConfBean> sexConf) {
        this.sexConf = sexConf;
    }

    public List<CountryListBean> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<CountryListBean> countryList) {
        this.countryList = countryList;
    }

    public List<ExpireTypeBean> getExpireTypeConf() {
        return expireTypeConf;
    }

    public void setExpireTypeConf(List<ExpireTypeBean> expireTypeConf) {
        this.expireTypeConf = expireTypeConf;
    }

    public static class LaguageConfBean {
        /**
         * laguageId : 1
         * name : 中文
         */

        private String laguageId;
        private String name;

        public String getLaguageId() {
            return laguageId;
        }

        public void setLaguageId(String laguageId) {
            this.laguageId = laguageId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class LayoutConfBean {
        /**
         * layoutId : 1
         * name : Studio
         */

        private String layoutId;
        private String name;

        public String getLayoutId() {
            return layoutId;
        }

        public void setLayoutId(String layoutId) {
            this.layoutId = layoutId;
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

        private String sexId;
        private String name;

        public String getSexId() {
            return sexId;
        }

        public void setSexId(String sexId) {
            this.sexId = sexId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class JobTypeBean {
        /**
         * sexId : 1
         * name : 男
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


    public static class CountryListBean {
        /**
         * cId : 1
         * countryName : 中国
         * cityList : [{"cityId":"1","cityName":"北京"},{"cityId":"2","cityName":"上海"},{"cityId":"3","cityName":"广州"},{"cityId":"4","cityName":"厦门"}]
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
             * cityName : 北京
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

    public static class EducationListBean {
        /**
         * cityId : 1
         * cityName : 北京
         */

        private String eId;
        private String name;


        public String geteId() {
            return eId;
        }

        public void seteId(String eId) {
            this.eId = eId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    public static class ExpireTypeBean {
        /**
         * cityId : 1
         * cityName : 北京
         */

        private String eId;
        private String expireType;


        public String geteId() {
            return eId;
        }

        public void seteId(String eId) {
            this.eId = eId;
        }

        public String getExpireType() {
            return expireType;
        }

        public void setExpireType(String expireType) {
            this.expireType = expireType;
        }
    }

}
