package com.library.repository.models;

import com.library.repository.db.model.CompanyTypeModel;

import java.util.List;

public class CompanyTypeListModelV1 {

    List<CompanyTypeModelV1> companyTypeList;
    List<CountryListModel> countryList;

    public List<CompanyTypeModelV1> getCompanyTypeList() {
        return companyTypeList;
    }

    public void setCompanyTypeList(List<CompanyTypeModelV1> companyTypeList) {
        this.companyTypeList = companyTypeList;
    }

    public List<CountryListModel> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<CountryListModel> countryList) {
        this.countryList = countryList;
    }
}
