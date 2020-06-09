package com.fjx.mg.main.payment.ask.editquestion;

import android.widget.EditText;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.List;

public interface EditContract {
    interface View extends BaseView {
        void ShowNum(String num);

        void voidEditSuccess();
    }

    abstract class Presenter extends BasePresenter<EditContract.View> {
        public Presenter(EditContract.View view) {
            super(view);
        }

        public abstract void searchWatcher(EditText etSearch);


        public abstract void updateImage(String question, String desc, List<String> images, String qid);


    }
}
