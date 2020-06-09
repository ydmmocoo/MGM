package com.fjx.mg.main.payment.ask;

import android.widget.EditText;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.List;

public interface AskContract {
    interface View extends BaseView {
        void ShowNum(String num);

        void ShowPrice(String price);

        void ShowServicePrice(String price);

        void updateImageSuccess(String ext);
    }

    abstract class Presenter extends BasePresenter<AskContract.View> {
        public Presenter(AskContract.View view) {
            super(view);
        }

        public abstract void searchWatcher(EditText etSearch);

        public abstract void PayWatcher(EditText etSearch);

        public abstract void getServicePrice();

        public abstract void updateImage(String question, String desc, List<String> images, final String price);


    }
}
