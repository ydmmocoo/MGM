package com.fjx.mg.main.rate;

import android.text.TextUtils;
import android.util.Log;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.AdListModel;
import com.library.repository.models.RateModel;
import com.library.repository.models.RemoteRateModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.library.repository.repository.translate.rate.TranslateListener;
import com.library.repository.repository.translate.model.RateResultModel;
import com.library.repository.repository.translate.model.TranslateCurrencyM;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.util.ArrayList;
import java.util.List;

class RatePresenter extends RateContract.Presenter {
    List<RateModel> dataList = new ArrayList<>();

    RatePresenter(RateContract.View view) {
        super(view);
    }

    @Override
    void initData() {
        dataList.add(new RateModel(R.drawable.area_icon_china, mView.getCurContext().getString(R.string.rate_chinese), "CNY"));
        dataList.add(new RateModel(R.drawable.area_icon_mdjsj, mView.getCurContext().getString(R.string.rate_aly), "MGA"));
        dataList.add(new RateModel(R.drawable.area_icon_xg, mView.getCurContext().getString(R.string.rate_HongKong), "HKD"));
        dataList.add(new RateModel(R.drawable.area_icon_ama, mView.getCurContext().getString(R.string.rate_english), "USD"));
        dataList.add(new RateModel(R.drawable.area_icon_o, mView.getCurContext().getString(R.string.rate_Euro), "EUR"));
        dataList.add(new RateModel(R.drawable.area_icon_japan, mView.getCurContext().getString(R.string.rate_japan), "JPY"));
        dataList.add(new RateModel(R.drawable.area_icon_eng, mView.getCurContext().getString(R.string.rate_Pound), "GBP"));
        List<RateModel> localList = RepositoryFactory.getLocalRepository().getRateConvertList();
        if (localList.isEmpty()) {
            localList = new ArrayList<>();
            localList.add(new RateModel(R.drawable.area_icon_china, mView.getCurContext().getString(R.string.rate_chinese), "CNY"));
        }
        mView.defaultShow(localList);
    }

    @Override
    void convert(String amount, String from, String to) {
        if (mView == null) return;
        mView.createAndShowDialog();
        RepositoryFactory.getTranslateApi().convert(amount, from, to, new TranslateListener<RateResultModel>() {
            @Override
            public void onSuccess(RateResultModel model) {
                if (mView == null) return;
                mView.showConvert(model);
                mView.destoryAndDismissDialog();
            }

            @Override
            public void onError(String code) {
                if (mView == null) return;
                mView.destoryAndDismissDialog();
            }
        });
    }

    @Override
    void getCurrency() {
        RepositoryFactory.getTranslateApi().getCurrency(new TranslateListener<List<TranslateCurrencyM>>() {
            @Override
            public void onSuccess(List<TranslateCurrencyM> translateCurrencyMS) {
                String getCurrency = JsonUtil.moderToString(translateCurrencyMS);
                Log.d("getCurrency", JsonUtil.moderToString(translateCurrencyMS));

            }

            @Override
            public void onError(String code) {

            }
        });
    }

    @Override
    void showRateList(final List<RateModel> list, final String s) {
        if (mView == null) return;
        new XPopup.Builder(mView.getCurContext())
                .asBottomList(mView.getCurContext().getString(R.string.hint_select),
                        new String[]{
                                mView.getCurContext().getString(R.string.rate_chinese),
                                mView.getCurContext().getString(R.string.rate_aly),
                                mView.getCurContext().getString(R.string.rate_HongKong),
                                mView.getCurContext().getString(R.string.rate_english),
                                mView.getCurContext().getString(R.string.rate_Euro),
                                mView.getCurContext().getString(R.string.rate_japan),
                                mView.getCurContext().getString(R.string.rate_Pound)},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                RateModel currency = dataList.get(position);
                                for (RateModel r : list) {
                                    if (TextUtils.equals(r.getAmountKey(), currency.getAmountKey())) {
                                        CommonToast.toast(mView.getCurContext().getString(R.string.notice_add_second));
                                        return;
                                    }
                                }
                                if (s.contains(currency.getAmountName())) {
                                    CommonToast.toast("查询源不参与汇率计算");
                                    return;
                                }
                                mView.addRateItem(currency);
                            }
                        })
                .show();
    }

    @Override
    void shoFromList(final List<RateModel> list) {
        if (mView == null) return;
        new XPopup.Builder(mView.getCurContext())
                .asBottomList(mView.getCurContext().getString(R.string.hint_select),
                        new String[]{
                                mView.getCurContext().getString(R.string.rate_chinese),
                                mView.getCurContext().getString(R.string.rate_aly),
                                mView.getCurContext().getString(R.string.rate_HongKong),
                                mView.getCurContext().getString(R.string.rate_english),
                                mView.getCurContext().getString(R.string.rate_Euro),
                                mView.getCurContext().getString(R.string.rate_japan),
                                mView.getCurContext().getString(R.string.rate_Pound)},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {

                                RateModel currency = dataList.get(position);
                                mView.DeletTo(currency);
                            }
                        })
                .show();
    }

    @Override
    void batchConvert(String amount, String from, final List<RateModel> toList) {
        if (toList.isEmpty()) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_rate));
            return;
        }
        String to = "";
        for (RateModel model : toList) {
            to = to.concat(model.getAmountKey()).concat(",");
        }
        if (mView == null) return;
        mView.createAndShowDialog();
        RepositoryFactory.getLocalRepository().saveRateAmount(amount);
        RepositoryFactory.getRemoteRepository().batchConvert(amount, from, to)
                .compose(RxScheduler.<ResponseModel<List<RemoteRateModel>>>toMain())
                .as(mView.<ResponseModel<List<RemoteRateModel>>>bindAutoDispose())
                .subscribe(new CommonObserver<List<RemoteRateModel>>() {
                    @Override
                    public void onSuccess(List<RemoteRateModel> remoteList) {
                        mView.destoryAndDismissDialog();
                        if (remoteList != null) {
                            for (RateModel local : toList) {
                                for (RemoteRateModel remote : remoteList) {
                                    if (TextUtils.equals(local.getAmountKey(), remote.getTo())) {
                                        local.setToAmount(remote.getCAmount());
                                        break;
                                    }
                                }
                            }
                            RepositoryFactory.getLocalRepository().saveRateConvertList(toList);
                            mView.showConvertList(toList);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                    }
                });

    }

    @Override
    void getAd() {
        RepositoryFactory.getRemoteRepository().getAd("8")
                .compose(RxScheduler.<ResponseModel<AdListModel>>toMain())
                .as(mView.<ResponseModel<AdListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AdListModel>() {
                    @Override
                    public void onSuccess(AdListModel data) {
                        if (mView != null) {
                            if (data.getAdList() != null && data != null) {
                                mView.showBanners(data);
                            }
                        }

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        Log.e("广告失败", "" + data);
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }


}
