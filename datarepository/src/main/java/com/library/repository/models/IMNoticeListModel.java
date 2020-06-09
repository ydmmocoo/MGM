package com.library.repository.models;

import java.util.List;

public class IMNoticeListModel {

    private boolean hasNext;
    List<IMNoticeModel> recordList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<IMNoticeModel> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<IMNoticeModel> recordList) {
        this.recordList = recordList;
    }
}
