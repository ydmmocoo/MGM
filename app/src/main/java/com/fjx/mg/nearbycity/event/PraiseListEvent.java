package com.fjx.mg.nearbycity.event;

/**
 * Author    by hanlz
 * Date      on 2019/10/23.
 * Description：
 */
public class PraiseListEvent {
    private String commentId;
    private String replyId;
    private String cId;

    public PraiseListEvent(String commentId, String replyId, String cId) {
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
