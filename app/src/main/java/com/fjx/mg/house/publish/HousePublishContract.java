package com.fjx.mg.house.publish;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.HouseDetailModel;

import java.util.List;
import java.util.Map;

public interface HousePublishContract {

    interface View extends BaseView {

        void commitSuccess();

        void selectType(String s, int i);

        void selecrtAddress(String countryName, String cityName, String countryId, String cityId);

        void selectHType(String name, String layoutId);

        void selectLanguage(String name, String laguageId);

        void selectExpire(String name, String id);

        int getPublishType();

        void showHouseDetail(HouseDetailModel houseModel);

    }

    abstract class Presenter extends BasePresenter<HousePublishContract.View> {

        Presenter(HousePublishContract.View view) {
            super(view);
        }

        abstract void getConfig();

        abstract void showTypeDialog();

        abstract void showAddressDialog();

        abstract void showHtypeDialog();

        abstract void showLanguageDialog();

        abstract void showExpireDialog();

        abstract void updateImage(Map<String, Object> map, final List<String> filePaths);

        abstract void commit(Map<String, Object> map);

        abstract void edit(Map<String, Object> map);

        abstract void houseDetail(String hId);

        abstract String getTypeIdByName(String type, String name);

        abstract Object getDefaultData(String type);


    }

}
