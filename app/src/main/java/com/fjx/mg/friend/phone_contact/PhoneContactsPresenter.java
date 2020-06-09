package com.fjx.mg.friend.phone_contact;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.fjx.mg.R;
import com.google.gson.JsonObject;
import com.library.common.utils.CommonToast;
import com.library.common.utils.ContactUtil;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.LanguageConvent;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.FriendContactSectionModel;
import com.library.repository.models.PhoneContactListModel;
import com.library.repository.models.PhoneContactModel;
import com.library.repository.models.PhoneContactSection;
import com.library.repository.models.RemoteContactModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PhoneContactsPresenter extends PhoneContactsContract.Presenter {
    public PhoneContactsPresenter(PhoneContactsContract.View view) {
        super(view);
    }

    private List<PhoneContactModel> phoneModels;

    @Override
    void getPhoneContactList() {
        try {
            List<JSONObject> list = ContactUtil.getContactInfo(mView.getCurContext());
            List<PhoneContactModel> datas = new ArrayList<>();
            for (JSONObject object : list) {
                PhoneContactModel m = JsonUtil.strToModel(object.toString(), PhoneContactModel.class);
                if (m == null || TextUtils.isEmpty(m.getMobile())) continue;
                datas.add(m);
            }
            batchCheckUser(datas);
            Log.d("ContactUtil", JsonUtil.moderToString(list));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    void batchCheckUser(final List<PhoneContactModel> contactModels) {
        String phones = "";
        for (PhoneContactModel model : contactModels) {
            phones = phones.concat(model.getMobile()).concat(",");
        }

        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().batchCheckUser(phones)
                .compose(RxScheduler.<ResponseModel<PhoneContactListModel>>toMain())
                .as(mView.<ResponseModel<PhoneContactListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<PhoneContactListModel>() {
                    @Override
                    public void onSuccess(PhoneContactListModel datas) {
                        mView.inviteContent(datas.getSmsTemplate());
                        for (RemoteContactModel m : datas.getPhoneList()) {
                            for (PhoneContactModel model : contactModels) {
                                if (TextUtils.equals(m.getPhone(), model.getMobile())) {
                                    model.setIsExits(m.getIsExits());
                                    break;
                                }
                            }
                        }
                        mView.hideLoading();
                        mView.showContactList(contactModels);
                        phoneModels = contactModels;
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());

                    }
                });

    }

    @Override
    void findImUser(String imUId) {

        RepositoryFactory.getChatRepository().getUsersProfile(imUId, true,
                new TIMValueCallBack<List<TIMUserProfile>>() {

                    @Override
                    public void onError(int i, String s) {
                        if (TextUtils.equals(s, "Err_Profile_Invalid_Account")) {
                            CommonToast.toast(mView.getCurContext().getString(R.string.no_user));
                        }
                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                        Log.d(Constant.TIM_LOG, JsonUtil.moderToString(timUserProfiles));
                        if (timUserProfiles.size() == 0) return;
                        mView.getImUserSuccess(timUserProfiles.get(0));
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
                String content = s.toString();
                if (phoneModels == null) return;
                if (TextUtils.isEmpty(content)) {
                    mView.showContactList(phoneModels);
                } else {
                    searchList(content, phoneModels);
                }

            }
        });
    }


    private void searchList(String content, List<PhoneContactModel> phoneModels) {

        List<PhoneContactModel> searchList = new ArrayList<>();
        for (PhoneContactModel model : phoneModels) {
            if (JsonUtil.moderToString(model).contains(content)) {
                searchList.add(model);
            }
        }
        mView.showContactList(searchList);
    }


    public List<PhoneContactSection> getContactList(List<PhoneContactModel> contactModels) {
        List<PhoneContactSection> list = new ArrayList<>();

        Map<String, List<PhoneContactModel>> map = new HashMap<>();
        for (PhoneContactModel contactModel : contactModels) {
            String firstChar = LanguageConvent.getFirstChar(contactModel.getDisplay_name());
            if (map.containsKey(firstChar)) {
                List<PhoneContactModel> mapList = map.get(firstChar);
                mapList.add(contactModel);
            } else {
                List<PhoneContactModel> mapList = new ArrayList<>();
                mapList.add(contactModel);
                map.put(firstChar, mapList);
            }
        }

        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String section = it.next();
            List<PhoneContactModel> contacts = map.get(section);
            list.add(new PhoneContactSection(true, section));
            for (PhoneContactModel c : contacts) {
                list.add(new PhoneContactSection(false,section,c.getDisplay_name(),c.getFirstName(),c.getMobile()));
            }
        }

        return list;
    }
}
