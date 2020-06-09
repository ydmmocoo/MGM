package com.fjx.mg.scan;

import androidx.fragment.app.FragmentManager;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.AgentInfoModel;
import com.library.repository.models.UserInfoModel;

public interface ScanSuccessfulContract {
    interface View extends BaseView {

        FragmentManager fragmentManager();

        void DoWithDrawal();

        void WorkManSuccess(Boolean paySuccess);

        void ShowAgentInfo(AgentInfoModel info);

        void showUserInfo(UserInfoModel data);
    }

    abstract class Presenter extends BasePresenter<View> {
        public Presenter(ScanSuccessfulContract.View view) {
            super(view);
        }

        abstract void chargeUserBalance(String price, String servicePrice, String payCode);

        abstract void showPayPasswordDiaog(String price);

        abstract void getAgentInfo(String payCode);

        abstract void SendUser(String price, String servicePrice, String payCode, String status, Boolean isFinish);
    }
}
