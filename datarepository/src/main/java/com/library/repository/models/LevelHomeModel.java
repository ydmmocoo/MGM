package com.library.repository.models;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.List;

public class LevelHomeModel {


    private UserInfoBean userInfo;
    private List<RankConfBean> rankConf;

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public List<RankConfBean> getRankConf() {
        return rankConf;
    }

    public void setRankConf(List<RankConfBean> rankConf) {
        this.rankConf = rankConf;
    }

    public static class UserInfoBean {
        /**
         * nickName : 王一阳
         * uImg : http://47.97.159.184/Uploads/user/2019-06-20/5d0b23cdd7427.jpg
         * inviteNum : 1
         * rank : 1
         */

        private String nickName;
        private String uImg;
        private String inviteNum;
        private String rank;


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

        public String getInviteNum() {
            return inviteNum;
        }

        public void setInviteNum(String inviteNum) {
            this.inviteNum = inviteNum;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }
    }

    public static class RankConfBean {
        /**
         * rank : 1
         * inviteNum : 0
         * price : 0
         * shortPrice :
         * redLimits :
         * transferLimits :
         */

        private int rank;
        private String inviteNum;
        private String price;
        private String shortPrice;
        private String redLimits;
        private String transferLimits;

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getInviteNum() {
            return inviteNum;
        }

        public void setInviteNum(String inviteNum) {
            this.inviteNum = inviteNum;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getShortPrice() {
            return shortPrice;
        }

        public void setShortPrice(String shortPrice) {
            this.shortPrice = shortPrice;
        }

        public String getRedLimits() {
            return redLimits;
        }

        public void setRedLimits(String redLimits) {
            this.redLimits = redLimits;
        }

        public String getTransferLimits() {
            return transferLimits;
        }

        public void setTransferLimits(String transferLimits) {
            this.transferLimits = transferLimits;
        }
    }



}
