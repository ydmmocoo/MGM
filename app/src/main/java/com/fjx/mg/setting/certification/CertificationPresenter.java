package com.fjx.mg.setting.certification;

import android.text.TextUtils;

import com.fjx.mg.R;
import com.google.gson.JsonObject;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.MultipartBodyHBuilder;
import com.library.repository.data.UserCenter;
import com.library.repository.models.PersonCerModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

public class CertificationPresenter extends CertificationContract.Presenter {

    public CertificationPresenter(CertificationContract.View view) {
        super(view);
    }

    @Override
    void certification(String realName, String idCard, String phone, String sn, String front, String back) {

        if (TextUtils.isEmpty(realName)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_realname));
            return;
        }
        if (TextUtils.isEmpty(idCard)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_idcode));
            return;
        }
//        if (TextUtils.isEmpty(phone)) {
//            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_mibile_num));
//            return;
//        }

        mView.createAndShowDialog();
        RepositoryFactory.getRemoteAccountRepository().certification(realName, idCard, "", "", front, back)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        UserInfoModel infoModel = UserCenter.getUserInfo();
                        infoModel.setRealName(2);
                        UserCenter.saveUserInfo(infoModel);
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(mView.getCurContext().getString(R.string.commit_success));
                        mView.commitSuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null)
                            mView.destoryAndDismissDialog();
                    }
                });

    }

    @Override
    void userAuditInfo() {
        mView.commitSuccess();
        RepositoryFactory.getRemoteAccountRepository().userAuditInfo()
                .compose(RxScheduler.<ResponseModel<PersonCerModel>>toMain())
                .as(mView.<ResponseModel<PersonCerModel>>bindAutoDispose())
                .subscribe(new CommonObserver<PersonCerModel>() {
                    @Override
                    public void onSuccess(PersonCerModel data) {
                        mView.destoryAndDismissDialog();
                        mView.showPersonCerInfo(data);

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null)
                            mView.destoryAndDismissDialog();
                    }
                });
    }


    @Override
    void updateImage(String filePath, final String key) {
        List<MultipartBody.Part> requestBody
                = MultipartBodyHBuilder.Builder()
                .addParams("k", key)
                .addParams(key, new File(filePath))
                .builder();
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteRepository().uploadFile(requestBody)
                .compose(RxScheduler.<ResponseModel<JsonObject>>toMain())
                .as(mView.<ResponseModel<JsonObject>>bindAutoDispose())
                .subscribe(new CommonObserver<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject data) {
                        Map<String, Object> map = JsonUtil.jsonToMap(data.toString());
                        String value = (String) map.get(key);
                        mView.uploadImageSucces(key, value);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null)
                            mView.destoryAndDismissDialog();
                    }
                });

    }
}
