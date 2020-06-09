package com.library.repository.models;

public class CompanyCerModel {


    /**
     * companyName : 楼下
     * businessLicense : 5869566
     * businessImg : http://47.97.159.184/Uploads/image/2019-07-03/5d1c5d9d64b04.png
     * employImg : http://47.97.159.184/Uploads/image/2019-07-03/5d1c5d9d6fdd8.png
     */

    private String companyName;
    private String businessLicense;
    private String businessImg;
    private String employImg;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getBusinessImg() {
        return businessImg;
    }

    public void setBusinessImg(String businessImg) {
        this.businessImg = businessImg;
    }

    public String getEmployImg() {
        return employImg;
    }

    public void setEmployImg(String employImg) {
        this.employImg = employImg;
    }
}
