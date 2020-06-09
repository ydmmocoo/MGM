package com.common.sharesdk;

public interface ShareLoginListener {
    void loginSuccess(String openid, String nickName, String avatar, String sex);

    void loginError(boolean isCabcle);
}
