package com.fjx.mg.main.payment.detail;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.PriceListModel;
import com.library.repository.models.QuestionInfo;
import com.library.repository.models.QuestionReplyModel;

import java.util.List;

public interface AskDetailContract {
    interface View extends BaseView {
        void ShowInfo(QuestionInfo info);

        void CloseSuccess();

        void acceptReplySuccess();

        void ShowList(QuestionReplyModel list);

        void deleteReplySuccess();

        void ShowPriceList(List<PriceListModel.PriceListBean> list);
    }

    abstract class Presenter extends BasePresenter<AskDetailContract.View> {
        public Presenter(AskDetailContract.View view) {
            super(view);
        }

        abstract void getQuestionInfo(String qId);

        abstract void closeQuestion(String qId);

        abstract void deletQ(String id);

        abstract void acceptReply(String rId);

        abstract void getPriceList();

        abstract void getQuestionReplyList(String qId, int page, Boolean isMine, int status);

    }
}
