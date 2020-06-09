package com.library.repository.repository.chat;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;
import com.tencent.imsdk.friendship.TIMFriendResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.NonNull;

public interface ChatApi {


    void login(String timUserId, String sign, TIMCallBack callBack);

    void logout();

    /**
     * 获取我的全部好友
     *
     * @param callBack
     */
    void getAllFriend(TIMValueCallBack callBack);


    /**
     * 添加好友
     *
     * @param timUserId  即时通讯服务器中的用户id
     * @param remarks    备注
     * @param addWording 加好友说明
     * @param callBack
     */
    void addFriend(String timUserId, String remarks, String addWording, TIMValueCallBack callBack);

    /**
     * 获取当前未决定的好友请求
     */
    void getPendencyList(@NonNull TIMValueCallBack<TIMFriendPendencyResponse> cb);

    /**
     * 处理好友请求
     *
     * @param timUserId 对方id
     * @param remarks   备注
     * @param cb
     */
    void doResponse(String timUserId, String remarks, @NonNull TIMValueCallBack<TIMFriendResult> cb);

    void doResponseReject(String timUserId, String remarks, @NonNull TIMValueCallBack<TIMFriendResult> cb);

    /**
     * 拉取某个用户的信息
     *
     * @param timUserId
     * @param forceUpdate 是否强制拉取
     */
    void getUsersProfile(String timUserId, boolean forceUpdate, TIMValueCallBack callBack);

    void getUsersProfile(List<String> ids, boolean forceUpdate, TIMValueCallBack callBack);


    /**
     * 删除好友
     *
     * @param ids        删除的好友id
     * @param deleSingle 单方面删除
     * @param callBack
     */
    void deleteFriend(List<String> ids, boolean deleSingle, TIMValueCallBack callBack);

    void deleteFriend(String id, boolean deleSingle, TIMValueCallBack callBack);

    /**
     * 添加黑名单
     *
     * @param id       黑名单的好友id
     * @param callBack
     */
    void addBlackList(List<String> id, TIMValueCallBack callBack);

    /**
     * 移除黑名单
     *
     * @param id       黑名单的好友id
     * @param callBack
     */
    void deleteBlackList(List<String> id, TIMValueCallBack callBack);
    /**
     * 获取黑名单列表
     *
     * @param callBack
     */
    void getBlackList(TIMValueCallBack callBack);

    /**
     * 修改好友信息，这边只修改备注
     */
    void modifyFriend(String imUid, String remark, TIMCallBack callBack);

    /**
     * 修改自己的信息
     *
     * @param map
     * @param callBack
     */
    void modifySelfProfile(HashMap<String, Object> map, TIMCallBack callBack);

}
