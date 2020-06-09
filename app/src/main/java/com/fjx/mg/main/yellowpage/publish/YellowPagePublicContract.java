package com.fjx.mg.main.yellowpage.publish;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.db.model.CompanyTypeModel;
import com.library.repository.models.CmpanydetaisModel;
import com.library.repository.models.CompanyDetailModel;

import java.util.List;

import retrofit2.http.Field;

public interface YellowPagePublicContract {

    interface View extends BaseView {

        void commitSuccess();

        void showCompanyDetail(CmpanydetaisModel data);

        String getRemoteImages();

        String getCid();

        void showSelectData(CompanyTypeModel model);

        void selectService(String serviceId, String serviceName, String secondServiceIs, String secondServiceName);

        void selecrtAddress(String countryName, String cityName, String countryId, String cityId);
    }

    abstract class Presenter extends BasePresenter<YellowPagePublicContract.View> {

        Presenter(YellowPagePublicContract.View view) {
            super(view);
        }

        abstract void updateImage(String title, String type, String contact, String phone,
                                  String address, String desc, final List<String> filePaths,
                                  String secondService,
                                  String countryId,
                                  String cityId);

        abstract void commit(String title, String type, String contact, String phone,
                             String address, String desc, String imageUrls,
                             String secondService,
                             String countryId,
                             String cityId);


        abstract void companyEdit(String title, String type, String contact, String phone,
                                  String address, String desc, String cid, String imageUrls,
                                  String secondService,
                                  String countryId,
                                  String cityId);

        abstract void getCompanyDetail(String cId);

        abstract void getCompanyTypesV1();

        abstract void showTypeDialog();

        abstract void showAreaDialog();
    }

}
