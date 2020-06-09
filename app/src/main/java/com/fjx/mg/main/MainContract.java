package com.fjx.mg.main;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.library.common.base.BaseFragment;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.AgentInfoModel;
import com.library.repository.models.CheckOrderIdModel;
import com.library.repository.models.MomentsReplyListModel;
import com.library.repository.models.VersionModel;

import java.util.ArrayList;
import java.util.List;

public interface MainContract {

    interface View extends BaseView {

        void showBottomTab(List<BaseFragment> fragments, ArrayList<CustomTabEntity> mTabEntities);

        void showUpdateDialog(VersionModel data);

        void showCashDialog();

        void friendsMomentsReply(MomentsReplyListModel data);

        void resopnseAagentInfo(AgentInfoModel model, String payCode, String price);

        void responseSendAgent(AgentInfoModel model, String payCode, String price);

        void showSucDialog(CheckOrderIdModel data);

        void showFailDialog(String msg);

    }

    abstract class Presenter extends BasePresenter<MainContract.View> {

        Presenter(MainContract.View view) {
            super(view);
        }

        abstract void ScanResult(String scan_result);

        abstract void initDatas();

        abstract void onBackPress();

        abstract void requestPermission();

        abstract void checkVersion();

        abstract void getCashList();

        abstract void showCashDialog();

        abstract void requestFriendsMomentsReply(String page, String commentId, String isRead);
    }

}
