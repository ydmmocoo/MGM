package com.library.repository.models;

import com.library.repository.db.model.CompanyTypeModel;

import java.util.List;

public class CompanyTypeListModel {

    List<CompanyTypeModel> companyTypeList;

    public List<CompanyTypeModel> getCompanyTypeList() {
        return companyTypeList;
    }

    public void setCompanyTypeList(List<CompanyTypeModel> companyTypeList) {
        this.companyTypeList = companyTypeList;
    }
}
