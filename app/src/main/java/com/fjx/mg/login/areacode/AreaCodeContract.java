package com.fjx.mg.login.areacode;

import android.widget.EditText;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.AddressModel;
import com.library.repository.models.AreaCodeModel;
import com.library.repository.models.AreaCodeSectionModel;

import java.util.List;

public interface AreaCodeContract {

    interface View extends BaseView {

        void showAreaCodeList(List<AreaCodeSectionModel> datas, List<String> heads);

        void showNationList(List<AreaCodeModel> datas);


    }

    abstract class Presenter extends BasePresenter<AreaCodeContract.View> {

        Presenter(AreaCodeContract.View view) {
            super(view);
        }


        abstract void getAreaCodeList(List<AreaCodeModel> list);

        abstract void searchWatcher(EditText editText);


        abstract void getNationList();
    }
}
