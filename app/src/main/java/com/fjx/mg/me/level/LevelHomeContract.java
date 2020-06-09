package com.fjx.mg.me.level;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.LevelHomeModel;

public interface LevelHomeContract {

    interface IView extends BaseView {

        void showLevelHomeModel(LevelHomeModel model);


    }

    abstract class Presenter extends BasePresenter<IView> {
        public Presenter(IView view) {
            super(view);
        }

        abstract void getUserRank();

    }

}
