package com.library.repository.repository.chat;

import android.util.Log;

import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMDelFriendType;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.imsdk.friendship.TIMFriendPendencyRequest;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;
import com.tencent.imsdk.friendship.TIMFriendRequest;
import com.tencent.imsdk.friendship.TIMFriendResponse;
import com.tencent.imsdk.friendship.TIMFriendResult;
import com.tencent.imsdk.friendship.TIMPendencyType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tencent.imsdk.friendship.TIMFriendResponse.TIM_FRIEND_RESPONSE_AGREE_AND_ADD;
import static com.tencent.imsdk.friendship.TIMFriendResponse.TIM_FRIEND_RESPONSE_REJECT;

public class ChatApiImpl implements ChatApi {


    @Override
    public void login(String timUserId, String sign, TIMCallBack callBack) {
        if (UserCenter.hasImLogin()) return;
        if (UserCenter.hasLogin())
            TIMManager.getInstance().login(timUserId, sign, callBack);
    }

    @Override
    public void logout() {
        //登出
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码 code 和错误描述 desc，可用于定位请求失败原因
                //错误码 code 列表请参见错误码表
                Log.d(Constant.TIM_LOG, "logout failed. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess() {
                //登出成功
            }
        });
    }

    @Override
    public void getAllFriend(TIMValueCallBack callBack) {
        TIMFriendshipManager.getInstance().getFriendList(callBack);
    }

    @Override
    public void addFriend(String timUserId, String remarks, String addWording, TIMValueCallBack callBack) {
        TIMFriendRequest timFriendRequest = new TIMFriendRequest(timUserId);
        timFriendRequest.setAddWording(addWording);
        timFriendRequest.setRemark(remarks);
        timFriendRequest.setAddSource("AddSource_Type_android");
        TIMFriendshipManager.getInstance().addFriend(timFriendRequest, callBack);
    }

    @Override
    public void getPendencyList(TIMValueCallBack<TIMFriendPendencyResponse> cb) {
        TIMFriendPendencyRequest request = new TIMFriendPendencyRequest();
        request.setNumPerPage(100);
        request.setTimPendencyGetType(TIMPendencyType.TIM_PENDENCY_COME_IN);
        TIMFriendshipManager.getInstance().getPendencyList(request, cb);
    }

    @Override
    public void doResponse(String timUserId, String remarks, TIMValueCallBack<TIMFriendResult> callback) {
        TIMFriendResponse response = new TIMFriendResponse();
        response.setIdentifier(timUserId);
        response.setRemark(remarks);
        response.setResponseType(TIM_FRIEND_RESPONSE_AGREE_AND_ADD);
        TIMFriendshipManager.getInstance().doResponse(response, callback);
    }

    @Override
    public void doResponseReject(String timUserId, String remarks, TIMValueCallBack<TIMFriendResult> callback) {
        TIMFriendResponse response = new TIMFriendResponse();
        response.setIdentifier(timUserId);
        response.setRemark(remarks);
        response.setResponseType(TIM_FRIEND_RESPONSE_REJECT);
        TIMFriendshipManager.getInstance().doResponse(response, callback);
    }

    @Override
    public void getUsersProfile(String timUserId, boolean forceUpdate, TIMValueCallBack callBack) {
        List<String> userIds = new ArrayList<>();
        userIds.add(timUserId);
        getUsersProfile(userIds, forceUpdate, callBack);
    }

    @Override
    public void getUsersProfile(List<String> ids, boolean forceUpdate, TIMValueCallBack callBack) {
        TIMFriendshipManager.getInstance().getUsersProfile(ids, forceUpdate, callBack);
    }

    @Override
    public void deleteFriend(List<String> ids, boolean deleSingle, TIMValueCallBack callBack) {
        int flag = deleSingle ? TIMDelFriendType.TIM_FRIEND_DEL_SINGLE : TIMDelFriendType.TIM_FRIEND_DEL_BOTH;
        TIMFriendshipManager.getInstance().deleteFriends(ids, flag, callBack);
    }


    @Override
    public void deleteFriend(String id, boolean deleSingle, TIMValueCallBack callBack) {
        List<String> userIds = new ArrayList<>();
        userIds.add(id);
        deleteFriend(userIds, deleSingle, callBack);
    }

    @Override
    public void addBlackList(List<String> id, TIMValueCallBack callBack) {
        TIMFriendshipManager.getInstance().addBlackList(id, callBack);
    }

    @Override
    public void deleteBlackList(List<String> id, TIMValueCallBack callBack) {
        TIMFriendshipManager.getInstance().deleteBlackList(id, callBack);
    }

    @Override
    public void getBlackList(TIMValueCallBack callBack) {
        TIMFriendshipManager.getInstance().getBlackList(callBack);
    }


    @Override
    public void modifyFriend(String imUid, String remark, TIMCallBack callBack) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(TIMFriend.TIM_FRIEND_PROFILE_TYPE_KEY_REMARK, remark);
        TIMFriendshipManager.getInstance().modifyFriend(imUid, hashMap, callBack);
    }

    @Override
    public void modifySelfProfile(HashMap<String, Object> map, TIMCallBack callBack) {
        TIMFriendshipManager.getInstance().modifySelfProfile(map, callBack);
    }


}
