package com.library.repository.models;

import java.util.List;

public class TagModel {
    private List<TagsBean> tags;

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public static class TagsBean {
        /**
         * tId : 10
         * name : 长腿欧巴
         */

        private String tId;
        private String name;
        private Boolean selected = false;


        public String gettId() {
            return tId;
        }

        public void settId(String tId) {
            this.tId = tId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getSelected() {
            return selected;
        }

        public void setSelected(Boolean selected) {
            this.selected = selected;
        }
    }
}
