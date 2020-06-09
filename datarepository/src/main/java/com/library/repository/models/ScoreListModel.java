package com.library.repository.models;

import java.util.List;

public class ScoreListModel {


    /**
     * hasNext : false
     * scoreList : [{"scoreId":"2","type":"邀请注册","score":"10","createTime":"2019-07-04 15:05:09"}]
     */

    private boolean hasNext;
    private List<ScoreBean> scoreList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<ScoreBean> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<ScoreBean> scoreList) {
        this.scoreList = scoreList;
    }

    public static class ScoreBean {
        /**
         * scoreId : 2
         * type : 邀请注册
         * score : 10
         * createTime : 2019-07-04 15:05:09
         */

        private String scoreId;
        private String type;
        private String score;
        private String createTime;

        public String getScoreId() {
            return scoreId;
        }

        public void setScoreId(String scoreId) {
            this.scoreId = scoreId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
