package com.fjx.mg.me.partner;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.LevelHomeModel;

public interface PartnerContract {

    interface IView extends BaseView {




    }

    abstract class Presenter extends BasePresenter<IView> {
        public Presenter(IView view) {
            super(view);
        }



    }

}
