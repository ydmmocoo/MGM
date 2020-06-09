package com.library.repository.repository;

import com.common.paylibrary.api.PayApi;
import com.common.paylibrary.api.PayApiImpl;
import com.common.sharesdk.repostory.ShareApi;
import com.common.sharesdk.repostory.ShareApiImpl;
import com.library.repository.Constant;
import com.library.repository.core.net.HttpCenter;
import com.library.repository.repository.chat.ChatApi;
import com.library.repository.repository.chat.ChatApiImpl;
import com.library.repository.repository.local.LocalApi;
import com.library.repository.repository.local.LocalApiImpl;
import com.library.repository.repository.remote.RemoteAccountApi;
import com.library.repository.repository.remote.RemoteApi;
import com.library.repository.repository.remote.RemoteCompanyApi;
import com.library.repository.repository.remote.RemoteFoodApi;
import com.library.repository.repository.remote.RemoteFriendsApi;
import com.library.repository.repository.remote.RemoteJobApi;
import com.library.repository.repository.remote.RemoteMGMPayApi;
import com.library.repository.repository.remote.RemoteNearbyCityApi;
import com.library.repository.repository.remote.RemoteNewsApi;
import com.library.repository.repository.remote.RemotePayApi;
import com.library.repository.repository.remote.RemoteSalesNetworkApi;
import com.library.repository.repository.translate.ITranslate;
import com.library.repository.repository.translate.TranslateImpl;

import retrofit2.http.PUT;

public class RepositoryFactory {

    /**
     * 服务器接口仓库
     *
     * @return
     */
    public static RemoteApi getRemoteRepository() {
        return HttpCenter.getInstance().createRepertory(Constant.getHost(), RemoteApi.class);
    }

    public static RemotePayApi getRemotePayRepository() {
        return HttpCenter.getInstance().createRepertory(Constant.getHost(), RemotePayApi.class);
    }

    public static RemoteAccountApi getRemoteAccountRepository() {
        return HttpCenter.getInstance().createRepertory(Constant.getHost(), RemoteAccountApi.class);
    }

    public static RemoteNewsApi getRemoteNewsRepository() {
        return HttpCenter.getInstance().createRepertory(Constant.getHost(), RemoteNewsApi.class);
    }

    public static RemoteCompanyApi getRemoteCompanyApi() {
        return HttpCenter.getInstance().createRepertory(Constant.getHost(), RemoteCompanyApi.class);
    }

    public static RemoteJobApi getRemoteJobApi() {
        return HttpCenter.getInstance().createRepertory(Constant.getHost(), RemoteJobApi.class);
    }

    public static RemoteFoodApi getRemoteFoodApi() {
        return HttpCenter.getInstance().createRepertory(Constant.getHost(), RemoteFoodApi.class);
    }

    public static ITranslate getTranslateApi() {
        return new TranslateImpl();
    }

    /**
     * 本地数据
     *
     * @return
     */
    public static LocalApi getLocalRepository() {
        return new LocalApiImpl();
    }

    /**
     * 聊天相关api
     *
     * @return
     */
    public static ChatApi getChatRepository() {
        return new ChatApiImpl();
    }

    /**
     * sharesdk相关api
     *
     * @return
     */
    public static ShareApi getShareApi() {
        return new ShareApiImpl();
    }


    /**
     * 支付相关api
     *
     * @return
     */
    public static PayApi getPayApi() {
        return new PayApiImpl();
    }

    public static RemoteFriendsApi getRemoteFriendsApi() {
        return HttpCenter.getInstance().createRepertory(Constant.getHost(), RemoteFriendsApi.class);
    }

    /**
     * 同城
     *
     * @return
     */
    public static RemoteNearbyCityApi getRemoteNearbyCitysApi() {
        return HttpCenter.getInstance().createRepertory(Constant.getHost(), RemoteNearbyCityApi.class);
    }

    /**
     * 同城
     *
     * @return
     */
    public static RemoteSalesNetworkApi getRemoteSalesNetworkApi() {
        return HttpCenter.getInstance().createRepertory(Constant.getHost(), RemoteSalesNetworkApi.class);
    }

    /**
     * 支付sdk
     */
    public static RemoteMGMPayApi getRemoteMGMPayApi(){
        return HttpCenter.getInstance().createRepertory(Constant.getHost(), RemoteMGMPayApi.class);
    }
}
