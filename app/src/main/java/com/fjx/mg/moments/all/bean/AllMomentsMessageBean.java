package com.fjx.mg.moments.all.bean;

import java.util.List;

/**
 * create by hanlz
 * 2019/9/23
 * Describe:
 */
public class AllMomentsMessageBean {
    private boolean hasNext;

    private List<ReplyList> replyList;


    public class ReplyList{

        private String    replyId;//回复记录id

        private String content;//回复内容


        private String   createTime;//回复时间


        private String      userIdfer;//回复用户id


        private String userNickName;//回复昵称


        private String  userAvatar;//回复头像


        private String  isLike;//当前登陆过用户是否点赞过


        private String  LikeNum;//点赞数量


        private String  toUserNick;//对某个用户的回答在回复


        private String  toReplyCont;//上一条回复内容

        private String     mId;//朋友圈记录id


//        private String    content;//朋友圈文字内容


        private String    url;//图片或者视频的地址


        private String     toUserIdfer;//复制

        private String userId;//  回复某个用户的id;

    }
}
