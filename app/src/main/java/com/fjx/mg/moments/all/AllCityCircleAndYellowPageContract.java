package com.fjx.mg.moments.all;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CompanyListModel;
import com.library.repository.models.NearbyCItyGetListModel;
import com.library.repository.models.ResponseModel;

/**
 * Author    by hanlz
 * Date      on 2020/4/8.
 * Description：
 */
public interface AllCityCircleAndYellowPageContract {

    interface View extends BaseView {
        void responseFailed(ResponseModel data);
         void responseCityList(NearbyCItyGetListModel data);
        void responseCompanyList(CompanyListModel data);
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void requestCityList(String page, String k, String typeId, String uid);
        public abstract void requestCompanyList(String page, String uid);
    }
}
