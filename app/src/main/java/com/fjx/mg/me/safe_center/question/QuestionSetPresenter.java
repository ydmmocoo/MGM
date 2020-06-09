package com.fjx.mg.me.safe_center.question;

import android.text.TextUtils;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

public class QuestionSetPresenter extends QuestionSetContract.Presenter {

    QuestionSetPresenter(QuestionSetContract.View view) {
        super(view);
    }

    @Override
    void setProblem(String question, String answer) {

        if (TextUtils.isEmpty(question) || TextUtils.isEmpty(answer)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.no_empty));
            return;
        }


        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().setIssue(question, answer)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        UserInfoModel model = UserCenter.getUserInfo();
                        model.setSetSecurityIssues(true);
                        UserCenter.saveUserInfo(model);

                        mView.hideLoading();
                        mView.setProblemSuccess();
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


}
