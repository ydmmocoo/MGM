package com.library.repository.models;

import java.util.List;

public class CountryListModel {

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
