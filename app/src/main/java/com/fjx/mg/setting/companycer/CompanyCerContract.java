package com.fjx.mg.setting.companycer;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CompanyCerModel;

public interface CompanyCerContract {

    interface View extends BaseView {

        void uploadImageSucces(String key, String imgeUrl);

        void commitSuccess();

        void showCompanyCerInfo(CompanyCerModel model);

    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void updateImage(String filePath, final String key);

        /**
         * 企业认证资料上传
         *
         * @param name        公司民资
         * @param licenseNo   营业号
         * @param businessImg 营业执照
         * @param employImg   在职证明
         */
        abstract void certification(String name, String licenseNo, String businessImg, String employImg);

        abstract void companyAuditInfo();
    }

}
