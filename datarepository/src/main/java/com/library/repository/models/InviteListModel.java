package com.library.repository.models;

import java.util.List;

public class InviteListModel {


    /**
     * hasNext : false
     * inviteList : [{"uId":"34","nickName":"王一阳","uImg":"http://47.97.159.184/Uploads/user/2019-06-20/5d0b23cdd7427.jpg"}]
     */

    private boolean hasNext;
    private List<InviteBean> inviteList;
    private String inviteNum;

    public String getInviteNum() {
        return inviteNum;
    }

    public void setInviteNum(String inviteNum) {
        this.inviteNum = inviteNum;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<InviteBean> getInviteList() {
        return inviteList;
    }

    public void setInviteList(List<InviteBean> inviteList) {
        this.inviteList = inviteList;
    }

    public static class InviteBean {
        /**
         * uId : 34
         * nickName : 王一阳
         * uImg : http://47.97.159.184/Uploads/user/2019-06-20/5d0b23cdd7427.jpg
         */

        private String uId;
        private String nickName;
        private String uImg;

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getUImg() {
            return uImg;
        }

        public void setUImg(String uImg) {
            this.uImg = uImg;
        }
    }
}
