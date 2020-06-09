package com.library.repository.models;

public class InviteModel {


    /**
     * registerUrl : http://47.97.159.184/index.php/invite/register?iv=yikekg
     * inviteCode : yikekg
     */

    private String registerUrl;
    private String inviteCode;
    /**
     * inviteTitle :
     * inviteTip :
     * inviteDesc :
     * smsTemplate :
     */

    private String inviteTitle;
    private String inviteTip;
    private String inviteDesc;
    private String smsTemplate;


    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getInviteTitle() {
        return inviteTitle;
    }

    public void setInviteTitle(String inviteTitle) {
        this.inviteTitle = inviteTitle;
    }

    public String getInviteTip() {
        return inviteTip;
    }

    public void setInviteTip(String inviteTip) {
        this.inviteTip = inviteTip;
    }

    public String getInviteDesc() {
        return inviteDesc;
    }

    public void setInviteDesc(String inviteDesc) {
        this.inviteDesc = inviteDesc;
    }

    public String getSmsTemplate() {
        return smsTemplate;
    }

    public void setSmsTemplate(String smsTemplate) {
        this.smsTemplate = smsTemplate;
    }
}
