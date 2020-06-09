package com.fjx.mg.me.userinfo;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.fjx.mg.R;
import com.google.gson.JsonObject;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.MultipartBodyHBuilder;
import com.library.repository.data.UserCenter;
import com.library.repository.models.JobConfigModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

import static com.tencent.imsdk.TIMUserProfile.TIM_PROFILE_TYPE_KEY_FACEURL;
import static com.tencent.imsdk.TIMUserProfile.TIM_PROFILE_TYPE_KEY_GENDER;
import static com.tencent.imsdk.TIMUserProfile.TIM_PROFILE_TYPE_KEY_LOCATION;
import static com.tencent.imsdk.TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK;

public class UserInfoPresenter extends UserInfoContract.Presenter {

    private boolean onlyUpdateImage;

    UserInfoPresenter(UserInfoContract.View view) {
        super(view);
    }

    @Override
    void getDefaultAvatar() {

        mView.createAndShowDialog();
        RepositoryFactory.getRemoteAccountRepository().getDefaultAvatar()
                .compose(RxScheduler.<ResponseModel<List<String>>>toMain())
                .as(mView.<ResponseModel<List<String>>>bindAutoDispose())
                .subscribe(new CommonObserver<List<String>>() {
                    @Override
                    public void onSuccess(List<String> data) {
                        if (mView != null) {
                            mView.destoryAndDismissDialog();
                            List<String> avatars = new ArrayList<>();
                            for (int i = 0; i < data.size(); i++) {
                                avatars.add(data.get(i));
                            }
                            mView.showPickPhotoDialog(avatars);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null)
                            mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null)
                            mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void getUserInfo() {

        mView.createAndShowDialog();
        RepositoryFactory.getRemoteAccountRepository().getUserProfile()
                .compose(RxScheduler.<ResponseModel<UserInfoModel>>toMain())
                .as(mView.<ResponseModel<UserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<UserInfoModel>() {
                    @Override
                    public void onSuccess(UserInfoModel data) {
                        mView.destoryAndDismissDialog();
                        UserInfoModel model = UserCenter.getUserInfo();
                        data.setToken(model.getToken());
                        data.setUseRig(model.getUseRig());
                        UserCenter.saveUserInfo(model);
                        mView.showUserInfo(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void updateProfile(final String filePath, String nickName, String sex, String address, String inviteCode) {
        mView.createAndShowDialog();
        String longitude = RepositoryFactory.getLocalRepository().getLongitude();
        String latitude = RepositoryFactory.getLocalRepository().getLatitude();
        RepositoryFactory.getRemoteAccountRepository().updateProfile(nickName, sex, longitude, latitude, filePath, address, inviteCode)
                .compose(RxScheduler.<ResponseModel<UserInfoModel>>toMain())
                .as(mView.<ResponseModel<UserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<UserInfoModel>() {
                    @Override
                    public void onSuccess(UserInfoModel data) {
                        if (mView == null) return;
                        mView.destoryAndDismissDialog();
                        if (onlyUpdateImage) {
                            onlyUpdateImage = false;
                            return;
                        }
                        CommonToast.toast("修改成功");
                        UserCenter.getAllFriend();
                        getUserProFile();
                        mView.upateSuccess(!TextUtils.isEmpty(filePath));

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView == null) return;
                        mView.destoryAndDismissDialog();
                        onlyUpdateImage = false;
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void updateImage(String filePath, final String nickName, final String sex, final String address, final String inviteCode) {
        List<MultipartBody.Part> requestBody
                = MultipartBodyHBuilder.Builder()
                .addParams("k", "avatar")
                .addParams("avatar", new File(filePath))
                .builder();
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteRepository().uploadFile(requestBody)
                .compose(RxScheduler.<ResponseModel<JsonObject>>toMain())
                .as(mView.<ResponseModel<JsonObject>>bindAutoDispose())
                .subscribe(new CommonObserver<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject data) {
                        Map<String, Object> map = JsonUtil.jsonToMap(data.toString());
                        String avatar = (String) map.get("avatar");
                        updateImUserImage(Constant.HOST.concat(avatar), nickName, sex, address, inviteCode);
                        updateProfile(avatar, nickName, sex, address, inviteCode);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        onlyUpdateImage = false;
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                    }
                });

    }

    @Override
    void doUpdate(String filePath, String nickName, String sex, String address, String inviteCode) {

        if (TextUtils.isEmpty(nickName)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_nick));
            return;
        }


        if (!TextUtils.isEmpty(filePath)) {
            updateImage(filePath, nickName, sex, address, inviteCode);
        } else {
            updateImUserImage("", nickName, sex, address, inviteCode);
            updateProfile(filePath, nickName, sex, address, inviteCode);
        }

    }

    @Override
    void doUpdateImage(String filePath) {
        onlyUpdateImage = true;
        updateImage(filePath, "", "", "", "");
    }

    @Override
    void updateImUserImage(String imageUrl, String nickName, String sex, String address, String inviteCode) {
        HashMap<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(imageUrl))
            map.put(TIM_PROFILE_TYPE_KEY_FACEURL, imageUrl);
        if (!TextUtils.isEmpty(nickName))
            map.put(TIM_PROFILE_TYPE_KEY_NICK, nickName);

        if (!TextUtils.isEmpty(sex))
            map.put(TIM_PROFILE_TYPE_KEY_GENDER, Integer.parseInt(sex));
        if (!TextUtils.isEmpty(address))
            map.put(TIM_PROFILE_TYPE_KEY_LOCATION, address);
        RepositoryFactory.getChatRepository().modifySelfProfile(map, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("updateImUserImage", s);
            }

            @Override
            public void onSuccess() {
                Log.d("updateImUserImage", "onSuccess: ");
                getUserProFile();
            }
        });
    }

    @Override
    void getConfig() {
        RepositoryFactory.getRemoteJobApi().getConf()
                .compose(RxScheduler.<ResponseModel<JobConfigModel>>toMain())
                .as(mView.<ResponseModel<JobConfigModel>>bindAutoDispose())
                .subscribe(new CommonObserver<JobConfigModel>() {
                    @Override
                    public void onSuccess(JobConfigModel data) {
                        RepositoryFactory.getLocalRepository().saveJobConfig(data);
                        if (data == null || data.getCountryList() == null || data.getCountryList().isEmpty())
                            return;
                        showAddressDialog();
                    }

                    @Override
                    public void onError(ResponseModel data) {

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }


    @Override
    void showAddressDialog() {
        final List<String> countryList = new ArrayList<>();
        final List<List<String>> cityList = new ArrayList<>();
        JobConfigModel configModel = RepositoryFactory.getLocalRepository().getJobConfig();

        if (configModel == null) {
            getConfig();
            return;
        }
        final List<JobConfigModel.CountryListBean> addressList = configModel.getCountryList();
        if (addressList == null || addressList.isEmpty()) return;

        for (JobConfigModel.CountryListBean bean : addressList) {
            countryList.add(bean.getCountryName());
            List<String> cities = new ArrayList<>();
            for (JobConfigModel.CountryListBean.CityListBean city : bean.getCityList()) {
                cities.add(city.getCityName());
            }
            cityList.add(cities);
        }

        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mView.getCurContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                String countryName = addressList.get(options1).getCountryName();
                String countryId = addressList.get(options1).getCId();
                String cityName = addressList.get(options1).getCityList().get(option2).getCityName();
                String cityId = addressList.get(options1).getCityList().get(option2).getCityId();


                mView.selecrtAddress(countryName, cityName, countryId, cityId);
            }
        }).build();
        pvOptions.setPicker(countryList, cityList);
        pvOptions.show();
    }

    private void getUserProFile() {
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {
                Log.d(Constant.TIM_LOG, s);
            }

            @Override
            public void onSuccess(TIMUserProfile timUserProfile) {
                UserCenter.savaTimUser(timUserProfile);
            }
        });
    }


}
