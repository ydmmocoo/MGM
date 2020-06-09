package com.fjx.mg.friend.notice.detail;

import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.BalanceDetailModel;
import com.library.repository.models.BillRecordModel;
import com.library.repository.models.IMNoticeListModel;
import com.library.repository.models.IMNoticeModel;
import com.library.repository.models.InviteModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

public class IMNoticeDetailPresenter extends IMNoticeDetailContract.Presenter {

    public IMNoticeDetailPresenter(IMNoticeDetailContract.View view) {
        super(view);
    }

    @Override
    void getBillDetail(String billId) {
        if (mView == null) return;
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteAccountRepository().billDetail(billId)
                .compose(RxScheduler.<ResponseModel<BillRecordModel.RecordListBean>>toMain())
                .as(mView.<ResponseModel<BillRecordModel.RecordListBean>>bindAutoDispose())
                .subscribe(new CommonObserver<BillRecordModel.RecordListBean>() {
                    @Override
                    public void onSuccess(BillRecordModel.RecordListBean data) {
                        if (mView != null) {
                            mView.destoryAndDismissDialog();
                            IMNoticeModel model = new IMNoticeModel();
                            model.setBillId(data.getBillId());
                            model.setFrom(data.getFrom());
                            model.setCnyPrice(data.getCnyPrice());
                            model.setPayType(data.getPayType());
                            model.setCreateTime(data.getCreateTime());
                            model.setPrice(data.getPrice());
                            model.setReciveTime(data.getReciveTime());
                            model.setRemark(data.getRemark());
                            model.setOrderId(data.getOrderId());
                            model.setTitle(data.getTitle());
                            model.setTo(data.getTo());
                            model.setTypeName(data.getTypeName());
                            model.setType(data.getType());
                            model.setSendTime(data.getSendTime());
                            model.setUserRemark(data.getUserRemark());
                            model.setToUid(data.getToUid());
                            mView.showBillDetail(model);
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
                        CommonToast.toast(data.getMsg());
                        if (mView != null)
                            mView.destoryAndDismissDialog();
                    }
                });
    }

    @Override
    void getPayNoticeDetail(String nid) {
        if (mView == null) return;
        mView.createAndShowDialog();
        RepositoryFactory.getRemotePayRepository().noticeDetail(nid)
                .compose(RxScheduler.<ResponseModel<IMNoticeModel>>toMain())
                .as(mView.<ResponseModel<IMNoticeModel>>bindAutoDispose())
                .subscribe(new CommonObserver<IMNoticeModel>() {
                    @Override
                    public void onSuccess(IMNoticeModel data) {
                        if (mView != null) {
                            mView.destoryAndDismissDialog();
                            mView.showBillDetail(data);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null)
                            mView.destoryAndDismissDialog();
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
    void balanceDetail(String id) {
        RepositoryFactory.getRemoteAccountRepository().balanceDetail(id)
                .compose(RxScheduler.<ResponseModel<BalanceDetailModel.BalanceListBean>>toMain())
                .as(mView.<ResponseModel<BalanceDetailModel.BalanceListBean>>bindAutoDispose())
                .subscribe(new CommonObserver<BalanceDetailModel.BalanceListBean>() {
                    @Override
                    public void onSuccess(BalanceDetailModel.BalanceListBean data) {
                        if (mView != null) {
                            IMNoticeModel model = new IMNoticeModel();
                            model.setBillId(data.getBalanceId());
                            model.setFrom(data.getFrom());
                            model.setCnyPrice(data.getCnyPrice());
                            model.setPayType(data.getType());
                            model.setCreateTime(data.getCreateTime());
                            model.setPrice(data.getPrice());
                            model.setReciveTime(data.getReciveTime());
//                        model.setRemark(data.getRemark());
                            model.setTitle(data.getType());
                            model.setTo(data.getTo());
                            model.setOrderId(data.getOrderId());
                            model.setTypeName(data.getType());
                            model.setType(data.getCate());
                            model.setRemainBalance(data.getRemainBalance());
                            model.setSendTime(data.getSendTime());
                            model.setUserRemark(data.getUserRemark());
                            mView.showBillDetail(model);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null)
                            mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null)
                            mView.hideLoading();
                    }
                });
    }


}
