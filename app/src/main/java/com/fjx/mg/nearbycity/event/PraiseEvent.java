package com.fjx.mg.nearbycity.event;

/**
 * Author    by hanlz
 * Date      on 2019/10/19.
 * Description：
 */
public class PraiseEvent {

    private String commentId;
    private String replyId;
    private String cId;

    public PraiseEvent(String commentId, String replyId, String cId) {
        this.commentId = commentId;
        this.replyId = replyId;
        this.cId = cId;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getReplyId() {
        return replyId;
    }

    public String getcId() {
        return cId;
    }
}
