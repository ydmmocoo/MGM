package com.fjx.mg.me.comment.fragment;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.MyCommentModel;
import com.library.repository.models.MyCompanyCommentModel;
import com.library.repository.models.MyHouseCommentModel;
import com.library.repository.models.MyNewsCommentModel;
import com.library.repository.models.MyReplyListCommentModel;

public interface MyCommentFragmentContract {


    interface View extends BaseView {
        void showMyHouseCommentModel(MyHouseCommentModel model);

        void showMyNewsCommentModel(MyNewsCommentModel model);

        void ShowMyCompanyComment(MyCompanyCommentModel model);

        void ShowMyReplyListComment(MyReplyListCommentModel model);

        void loadError();

        void deleteReplySuccess();

        void responseMyCommentList(MyCommentModel data);

    }

    abstract class Presenter extends BasePresenter<MyCommentFragmentContract.View> {

        Presenter(MyCommentFragmentContract.View view) {
            super(view);
        }

        abstract void deleteh(String id, boolean isReply);

        abstract void deleteN(String id, boolean isReply);

        abstract void deletY(String id, boolean isReply);

        abstract void deletQ(String id );

        abstract void getMyHouseComment(int page);

        abstract void getMyNewsComment(int page);

        abstract void getMyCompanyComment(int page);

        abstract void getMyQuestionComment(int page);


    }
}
