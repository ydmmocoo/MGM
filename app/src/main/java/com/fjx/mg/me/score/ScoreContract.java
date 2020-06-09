package com.fjx.mg.me.score;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.ScoreListModel;
import com.library.repository.models.UserInfoModel;

public interface ScoreContract {


    interface View extends BaseView {
        void showScoreList(ScoreListModel model);

        void loadError();


    }

    abstract class Presenter extends BasePresenter<ScoreContract.View> {

        Presenter(ScoreContract.View view) {
            super(view);
        }

        abstract void getScoreList(int page);


    }
}
