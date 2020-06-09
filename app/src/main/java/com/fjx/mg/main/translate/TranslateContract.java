package com.fjx.mg.main.translate;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.LanTranslateResultM;
import com.library.repository.models.TranslateLanModel;

public interface TranslateContract {

    interface View extends BaseView {
        void showTranslateResult(LanTranslateResultM model);

        void showLanguage(boolean isFrom, TranslateLanModel lanModel);

        void initData(String fromText, String from, String toText, String to);


    }

    abstract class Presenter extends BasePresenter<TranslateContract.View> {

        Presenter(TranslateContract.View view) {
            super(view);
        }

        abstract void initData();

        abstract void translate(String content, String from, String to);

        abstract void showlanguageDialog(boolean isFrom);

    }


}
