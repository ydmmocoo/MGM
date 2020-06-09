package com.library.repository.models;

import java.util.List;

public class QuestionInfo {
    private QuestionInfoBean questionInfo;

    public QuestionInfoBean getQuestionInfo() {
        return questionInfo;
    }

    public void setQuestionInfo(QuestionInfoBean questionInfo) {
        this.questionInfo = questionInfo;
    }

    public static class QuestionInfoBean {
        private String qId;//	id
        private String uid;
        private String question;//问题✔
        private String desc;//描述✔
        private String price;//赏金
        private String reply_count;//回复数量✔
        private String status;//状态,1:进行中,2:有采纳答案结束,3:无采纳答案结束'
        private List<String> images;//附带图片列表

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }



        public String getQId() {
            return qId;
        }

        public void setQId(String qId) {
            this.qId = qId;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getReply_count() {
            return reply_count;
        }

        public void setReply_count(String reply_count) {
            this.reply_count = reply_count;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
