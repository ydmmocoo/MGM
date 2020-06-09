package com.fjx.mg.setting.certification;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.PersonCerModel;

public interface CertificationContract {

    interface View extends BaseView {

        void uploadImageSucces(String key, String imgeUrl);

        void commitSuccess();

        void showPersonCerInfo(PersonCerModel model);

    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void updateImage(String filePath, final String key);

        abstract void certification(String realName, String idCard, String phone, String sn, String front, String back);

        abstract void userAuditInfo();
    }

}
