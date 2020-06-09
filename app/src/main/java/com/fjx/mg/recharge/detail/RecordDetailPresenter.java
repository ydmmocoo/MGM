package com.fjx.mg.recharge.detail;

import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.RechargeDetailModel;
import com.library.repository.models.RechargePhoneDetailModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

public class RecordDetailPresenter extends RecordDetailContract.Presenter {
    RecordDetailPresenter(RecordDetailContract.View view) {
        super(view);
    }

    @Override
    void getPhoneRecordDetail(String id, String type) {

        mView.showLoading();
        RepositoryFactory.getRemoteRepository().phoneChargeDetail(id, type)
                .compose(RxScheduler.<ResponseModel<RechargePhoneDetailModel>>toMain())
                .as(mView.<ResponseModel<RechargePhoneDetailModel>>bindAutoDispose())
                .subscribe(new CommonObserver<RechargePhoneDetailModel>() {
                    @Override
                    public void onSuccess(RechargePhoneDetailModel data) {
                        mView.hideLoading();
                        mView.showPhoneDetail(data);
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
    void getRecordDetail(String id, String type) {
        mView.showLoading();
        RepositoryFactory.getRemoteRepository().chargeRecordDetail(id, type)
                .compose(RxScheduler.<ResponseModel<RechargeDetailModel>>toMain())
                .as(mView.<ResponseModel<RechargeDetailModel>>bindAutoDispose())
                .subscribe(new CommonObserver<RechargeDetailModel>() {
                    @Override
                    public void onSuccess(RechargeDetailModel data) {
                        mView.hideLoading();
                        mView.showDetail(data);
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
