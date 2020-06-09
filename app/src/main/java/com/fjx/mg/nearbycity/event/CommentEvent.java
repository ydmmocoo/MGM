package com.fjx.mg.nearbycity.event;


/**
 * Author    by hanlz
 * Date      on 2019/10/20.
 * Description：
 */
public class CommentEvent {

    private String cid;


    public CommentEvent(String cid) {
        this.cid = cid;
    }


    public String getCid() {
        return cid;
    }

}
