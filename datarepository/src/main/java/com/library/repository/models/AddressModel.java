package com.library.repository.models;

import java.util.List;

public class AddressModel {


    /**
     * hasNext : false
     * addressList : [{"addressId":"4","name":"青青子衿","sex":"男士","phone":"24649568956","address":"福建厦门明年"}]
     */

    private boolean hasNext;
    private List<AddressListBean> addressList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<AddressListBean> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressListBean> addressList) {
        this.addressList = addressList;
    }

    public static class AddressListBean {
        /**
         * addressId : 4
         * name : 青青子衿
         * sex : 男士
         * phone : 24649568956
         * address : 福建厦门明年
         * "addressId":"8",
         * "name":"丽丽",
         * "sex":"女士",
         * "phone":"6464646464",
         * "address":"福建厦门",
         * "roomNo":"中午",
         * "longitude":"1.0000000",
         * "latitude":"1.0000000"
         */

        private String addressId;
        private String name;
        private String sex;
        private String phone;
        private String address;
        private String roomNo;
        private String longitude;
        private String latitude;

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
