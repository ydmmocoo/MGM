package com.fjx.mg.main.rate;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.AdListModel;
import com.library.repository.models.RateModel;
import com.library.repository.repository.translate.model.RateResultModel;

import java.util.List;

public interface RateContract {

    interface View extends BaseView {

        void showConvert(RateResultModel model);

        void addRateItem(RateModel rateModel);

        void ChangeFrom(RateModel rateModel);
        void DeletTo(RateModel rateModel);

        void defaultShow(List<RateModel> dates);

        void showConvertList(List<RateModel> dates);

        void showBanners(AdListModel data);

    }

    abstract class Presenter extends BasePresenter<RateContract.View> {

        Presenter(RateContract.View view) {
            super(view);
        }

        abstract void initData( );


        abstract void convert(String amount, String from, String to);

        abstract void getCurrency();


        abstract void showRateList(List<RateModel> list, String s);

        abstract void shoFromList(List<RateModel> list);

        abstract void batchConvert(String amount, String from, List<RateModel> toList);
        abstract void getAd();

    }

}
