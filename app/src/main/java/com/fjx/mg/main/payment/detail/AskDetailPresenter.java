package com.fjx.mg.main.payment.detail;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.PriceListModel;
import com.library.repository.models.QuestionReplyModel;
import com.library.repository.models.RechargeModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.QuestionInfo;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.util.List;

class AskDetailPresenter extends AskDetailContract.Presenter {

    AskDetailPresenter(AskDetailContract.View view) {
        super(view);
    }

    @Override
    void getQuestionInfo(String qId) {
        if (mView == null) return;
        mView.showLoading();
        RepositoryFactory.getRemoteCompanyApi().QuestionInfo(qId)
                .compose(RxScheduler.<ResponseModel<QuestionInfo>>toMain())
                .as(mView.<ResponseModel<QuestionInfo>>bindAutoDispose())
                .subscribe(new CommonObserver<QuestionInfo>() {
                    @Override
                    public void onSuccess(QuestionInfo data) {
                        mView.ShowInfo(data);
                        mView.hideLoading();

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }

    @Override
    void closeQuestion(String qId) {
        if (mView == null) return;
        mView.showLoading();
        RepositoryFactory.getRemoteCompanyApi().closeQuestion(qId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        CommonToast.toast("已关闭提问");
                        mView.CloseSuccess();
                        mView.hideLoading();

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }

    @Override
    void deletQ(final String id) {
        new XPopup.Builder(mView.getCurContext()).asConfirm("提示",
                mView.getCurContext().getString(R.string.delete_confirm_message), new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        deleteReplyQ(id);

                    }
                }).show();


    }

    private void deleteReplyQ(String replyId) {//删除有偿问答回复
        if (mView == null) return;
        RepositoryFactory.getRemoteNewsRepository().delQuestionReply(replyId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.deleteReplySuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void acceptReply(String rId) {
        if (mView == null) return;
        mView.showLoading();
        RepositoryFactory.getRemoteCompanyApi().acceptReply(rId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        CommonToast.toast("已采纳");
                        mView.acceptReplySuccess();
                        mView.hideLoading();

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }

    @Override
    void getPriceList() {
        RepositoryFactory.getRemoteRepository()
                .getPriceList()
                .compose(RxScheduler.<ResponseModel<PriceListModel>>toMain())
                .as(mView.<ResponseModel<PriceListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<PriceListModel>() {
                    @Override
                    public void onSuccess(PriceListModel model) {
                        List<PriceListModel.PriceListBean> data = model.getPriceList();
                        if (data != null && data.size() > 0)
                            data.get(0).setClick(true);
                        if (data.size() == 9) {
                            data.remove(8);
                        }
                        if (data != null && mView != null) {
                            mView.ShowPriceList(data);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void getQuestionReplyList(String qId, int page, final Boolean mine, final int status) {
        mView.showLoading();
        RepositoryFactory.getRemoteNewsRepository()
                .QuestionReplyList(qId, page)
                .compose(RxScheduler.<ResponseModel<QuestionReplyModel>>toMain())
                .as(mView.<ResponseModel<QuestionReplyModel>>bindAutoDispose())
                .subscribe(new CommonObserver<QuestionReplyModel>() {
                    @Override
                    public void onSuccess(QuestionReplyModel data) {
                        if (mView == null) return;
                        List<QuestionReplyModel.ReplyListBean> replyList = data.getReplyList();
                        for (int i = 0; i < replyList.size(); i++) {
                            replyList.get(i).setMine(mine);
                            replyList.get(i).setStatus(status);
                        }
                        mView.ShowList(data);
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }


}
