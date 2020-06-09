package com.library.repository.models;

import java.util.List;

public class RemoteAreaCode {
    private List<AreaCodeModel> hotList;
    private List<AreaCodeModel> nationList;

    public List<AreaCodeModel> getHotList() {
        return hotList;
    }

    public void setHotList(List<AreaCodeModel> hotList) {
        this.hotList = hotList;
    }

    public List<AreaCodeModel> getNationList() {
        return nationList;
    }

    public void setNationList(List<AreaCodeModel> nationList) {
        this.nationList = nationList;
    }
}
