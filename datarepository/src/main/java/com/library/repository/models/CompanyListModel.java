package com.library.repository.models;

import java.util.List;

public class CompanyListModel {

    private boolean hasNext;
    private List<CmpanydetaisModel> companyList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<CmpanydetaisModel> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<CmpanydetaisModel> companyList) {
        this.companyList = companyList;
    }
}
