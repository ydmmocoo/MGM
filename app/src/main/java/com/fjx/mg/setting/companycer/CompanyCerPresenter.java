package com.fjx.mg.setting.companycer;

import android.text.TextUtils;

import com.fjx.mg.R;
import com.google.gson.JsonObject;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.MultipartBodyHBuilder;
import com.library.repository.models.CompanyCerModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

public class CompanyCerPresenter extends CompanyCerContract.Presenter {

    public CompanyCerPresenter(CompanyCerContract.View view) {
        super(view);
    }

    @Override
    void certification(String name, String licenseNo, String businessImg, String employImg) {

        if (TextUtils.isEmpty(name)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_companyname));
            return;
        }
        if (TextUtils.isEmpty(licenseNo)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_Registration_No));
            return;
        }
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteCompanyApi().certification(name, licenseNo, businessImg, employImg)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
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
    void companyAuditInfo() {
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteAccountRepository().companyAuditInfo()
                .compose(RxScheduler.<ResponseModel<CompanyCerModel>>toMain())
                .as(mView.<ResponseModel<CompanyCerModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CompanyCerModel>() {
                    @Override
                    public void onSuccess(CompanyCerModel data) {
                        mView.destoryAndDismissDialog();
                        mView.showCompanyCerInfo(data);

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
