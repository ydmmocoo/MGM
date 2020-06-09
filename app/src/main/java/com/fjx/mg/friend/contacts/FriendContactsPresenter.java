package com.fjx.mg.friend.contacts;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.library.common.utils.JsonUtil;
import com.library.common.utils.LanguageConvent;
import com.library.repository.models.FriendContactSectionModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FriendContactsPresenter extends FriendContactsContract.Presenter {
    private List<TIMFriend> myTimFriends;

    public FriendContactsPresenter(FriendContactsContract.View view) {
        super(view);
    }

    @Override
    void getAllFriend() {
        if (mView==null) return;
        mView.createAndShowDialog();
        RepositoryFactory.getChatRepository().getAllFriend(new TIMValueCallBack<List<TIMFriend>>() {

            @Override
            public void onError(int i, String s) {
                Log.d("getAllFriend", s);
                if (mView == null) return;
                mView.destoryAndDismissDialog();
            }

            @Override
            public void onSuccess(List<TIMFriend> timFriends) {
                myTimFriends = timFriends;
                if (mView == null) return;
                mView.destoryAndDismissDialog();

                RepositoryFactory.getLocalRepository().saveAllFriend(timFriends);

                Log.e("getAllFriend", JsonUtil.moderToString(timFriends));
                List<FriendContactSectionModel> datas = getContactList(timFriends);
                if (mView == null) return;
                mView.showContactList(datas);
            }
        });
    }

    @Override
    void getPendencyList() {
        RepositoryFactory.getChatRepository().getPendencyList(new TIMValueCallBack<TIMFriendPendencyResponse>() {
            @Override
            public void onError(int i, String s) {
                Log.d("getPendencyList", s);
            }

            @Override
            public void onSuccess(TIMFriendPendencyResponse response) {
                if (mView == null) return;
                mView.getPendencyListSuccess(response);
            }
        });

    }

    @Override
    public void searchWatcher(EditText etSearch) {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (myTimFriends == null) return;
                String content = s.toString();
                if (TextUtils.isEmpty(content)) {
                    List<FriendContactSectionModel> datas = getContactList(myTimFriends);
                    mView.showContactList(datas);
                } else {
                    List<TIMFriend> searchList = new ArrayList<>();
                    for (TIMFriend friend : myTimFriends) {
                        String phone = "";
                        byte[] bytes = friend.getTimUserProfile().getCustomInfo().get("phone");
                        if (bytes != null)
                            phone = new String(bytes);
                        boolean b = !TextUtils.isEmpty(phone) && phone.contains(content);
                        if (JsonUtil.moderToString(friend).contains(content)||b) {
                            searchList.add(friend);
                        }
                    }
                    List<FriendContactSectionModel> datas = getContactList(searchList);
                    mView.showContactList(datas);
                }
            }
        });
    }

    private List<FriendContactSectionModel> getContactList(final List<TIMFriend> timFriends) {
        List<FriendContactSectionModel> list = new ArrayList<>();

        Map<String, List<TIMFriend>> map = new HashMap<>();
        for (TIMFriend timFriend : timFriends) {
            String nickName = timFriend.getTimUserProfile().getNickName();
            String remark = timFriend.getRemark();
            String content = "";
            if (!TextUtils.isEmpty(remark)) {
                content = remark;
            } else if (!TextUtils.isEmpty(nickName)) {
                content = nickName;
            } else {
                content = timFriend.getTimUserProfile().getIdentifier();
            }

            String firstChar = LanguageConvent.getFirstChar(content);
            if (map.containsKey(firstChar)) {
                List<TIMFriend> mapList = map.get(firstChar);
                mapList.add(timFriend);
            } else {
                List<TIMFriend> mapList = new ArrayList<>();
                mapList.add(timFriend);
                map.put(firstChar, mapList);
            }
        }
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String section = it.next();
            List<TIMFriend> friends = map.get(section);
            list.add(new FriendContactSectionModel(true, section));
            for (TIMFriend friend : friends) {
                list.add(new FriendContactSectionModel(false,section,friend));
            }
        }

        return list;
    }
}
