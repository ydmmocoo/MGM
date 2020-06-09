package com.fjx.mg.nearbycity.event;

/**
 * Author    by hanlz
 * Date      on 2020/3/24.
 * Description：先本地添加评论
 */
public class AddCommentEvent {
    private String content;
    private String cid;

    public AddCommentEvent(String content, String cid) {
        this.content = content;
        this.cid = cid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
