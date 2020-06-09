package com.fjx.mg.main.more.search;

import android.widget.EditText;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

public interface SearchAppContract {
    interface View extends BaseView {
        void showClearImage(boolean enableShow);

        void watchSearch(String search);

        void showUsed(String s);
    }

    abstract class Presenter extends BasePresenter<SearchAppContract.View> {
        public Presenter(View view) {
            super(view);
        }

        abstract void bindTextWatcher(EditText editText);

        abstract void recUseApp(String appId);
    }
}
