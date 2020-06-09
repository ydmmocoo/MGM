package com.library.repository.models;

import java.util.List;

public class JobListModel {

    /**
     * hasNext : false
     * jobsList : [{"jobId":"5","title":"产品","countryName":"中国","cityName":"厦门","sex":"男","pay":"100cny","contactPhone":"15860750234","contactWeixin":"wx20089","createTime":"2019-05-17 17:24:33","jobType":"行政,销售","status":"2","statusTip":""},{"jobId":"4","title":"会计","countryName":"中国","cityName":"厦门","sex":"男","pay":"100cny","contactPhone":"15860750234","contactWeixin":"wx20089","createTime":"2019-05-17 17:24:27","jobType":"行政,销售","status":"2","statusTip":""},{"jobId":"3","title":"厨师","countryName":"中国","cityName":"厦门","sex":"男","pay":"100cny","contactPhone":"15860750234","contactWeixin":"wx20089","createTime":"2019-05-17 17:24:20","jobType":"行政,销售","status":"2","statusTip":""}]
     */

    private boolean hasNext;
    private List<JobModel> jobsList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<JobModel> getJobsList() {
        return jobsList;
    }

    public void setJobsList(List<JobModel> jobsList) {
        this.jobsList = jobsList;
    }
}
