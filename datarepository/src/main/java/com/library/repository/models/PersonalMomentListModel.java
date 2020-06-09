package com.library.repository.models;

import java.util.List;

public class PersonalMomentListModel {
    private boolean hasNext;
    private List<MomentsListBean> momentsList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<MomentsListBean> getMomentsList() {
        return momentsList;
    }

    public void setMomentsList(List<MomentsListBean> momentsList) {
        this.momentsList = momentsList;
    }

    public static class MomentsListBean {
        /**
         * mId : 11
         * content : 这一刻更精彩！
         * createTime : 22小时前16:35
         * urls : ["http://47.97.159.184/Uploads/image/2019-09-10/5d7760527b50c.png","http://47.97.159.184/Uploads/image/2019-09-10/5d77605292d15.jpg","http://47.97.159.184/Uploads/image/2019-09-10/5d776052952c7.png","http://47.97.159.184/Uploads/image/2019-09-10/5d77605295f00.JPEG","http://47.97.159.184/Uploads/image/2019-09-10/5d7760529b920.png","http://47.97.159.184/Uploads/image/2019-09-10/5d776052a1c04.png","http://47.97.159.184/Uploads/image/2019-09-10/5d776052a10c8.png","http://47.97.159.184/Uploads/image/2019-09-10/5d7760529f60a.JPEG","http://47.97.159.184/Uploads/image/2019-09-10/5d776052ab1a6.png"]
         */

        private String mId;
        private String content;
        private String createTime;
        private List<String> urls;
        private ShareInfoBean shareInfo;

        public ShareInfoBean getShareInfo() {
            return shareInfo;
        }

        public void setShareInfo(ShareInfoBean shareInfo) {
            this.shareInfo = shareInfo;
        }

        public String getMId() {
            return mId;
        }

        public void setMId(String mId) {
            this.mId = mId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public List<String> getUrls() {
            return urls;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }
    }

    public static class ShareInfoBean {

        private String shareType;//分享分类，1：黄页，2：新闻，3：同城
        private String shareId;//记录id
        private String title;//标题
        private String desc;//内容
        private String img;//封面
        private String typeName;//同城类型(当分享类型为同城时候此参数才不为空)
        private String typeId;//同城类型id(当分享类型为同城时候此参数才不为空)

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getShareType() {
            return shareType;
        }

        public void setShareType(String shareType) {
            this.shareType = shareType;
        }

        public String getShareId() {
            return shareId;
        }

        public void setShareId(String shareId) {
            this.shareId = shareId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }


}
