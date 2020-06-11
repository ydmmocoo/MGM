package com.fjx.mg.main.fragment.home;

import android.location.Geocoder;
import android.util.Log;
import android.widget.TextView;

import com.fjx.mg.utils.SharedPreferencesUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.library.common.base.BaseApp;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.AdListModel;
import com.library.repository.models.NewsItemModel;
import com.library.repository.models.NewsListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.tencent.qcloud.uikit.TimConfig;

import java.util.Locale;

class HomePresenter extends HomeContract.Presenter {

    HomePresenter(HomeContract.View view) {
        super(view);
    }

    @Override
    void getRecommendStore() {
        mView.showMarqueeView(null);
        mView.showRecommendStore();
    }

    @Override
    void getNewsList(final int page) {
        RepositoryFactory.getRemoteNewsRepository().newsList(1, page, "")
                .compose(RxScheduler.<ResponseModel<NewsItemModel>>toMain())
                .as(mView.<ResponseModel<NewsItemModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NewsItemModel>() {
                    @Override
                    public void onSuccess(NewsItemModel data) {
                        if (mView != null) {
                            mView.showNewsList(data);
                        }
                        try {
                            if (data.getNewsList() != null && data.getNewsList().size() > 0) {
                                for (NewsListModel item : data.getNewsList()) {
                                    item.setTypeId(1);
                                    item.setUniqueId(String.valueOf(1).concat("_").concat(item.getNewsId()));
                                }
                            }
                            if (page == 1) {
                                DBDaoFactory.getNewsListDao().deleteAll(1);
                                DBDaoFactory.getNewsListDao().insertList(data.getNewsList());
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                        }
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null)
                            mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void recUseApp(String appId) {
        RepositoryFactory.getRemoteNewsRepository()
                .recUseApp(appId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                    }

                    @Override
                    public void onError(ResponseModel data) {
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                    }
                });
    }

    @Override
    void showLanguageDialog(TextView view) {
        //( zh-ch:中文简体,zh-tw:中文繁体，en-us:英语，fr:法语)
        String[] texts = null;
        if (TimConfig.isRelease) {
            texts = new String[]{"简体", "繁体", "English", "Français"};
        } else {
            texts = new String[]{"中文简体", "中文繁体", "English", "Français"};
        }

        new XPopup.Builder(mView.getCurActivity())
                .atView(view)// 依附于所点击的View，内部会自动判断在上方或者下方显示
                .hasShadowBg(false)
                .popupAnimation(PopupAnimation.ScrollAlphaFromLeftTop)
                .asAttachList(texts, new int[]{},
                        (position, text) -> {
                            mView.showCheckLanguage(position, text);
                            String l = "zh-ch";
                            if (position == 1) {
                                l = "zh-tw";
                            } else if (position == 2) {
                                l = "en-us";
                            } else if (position == 3) {
                                l = "fr";
                            }
                            RepositoryFactory.getLocalRepository().saveLanguageType(l);
                        })
                .show();
    }

    @Override
    void getAd() {
        RepositoryFactory.getRemoteRepository().getAd("1")
                .compose(RxScheduler.<ResponseModel<AdListModel>>toMain())
                .as(mView.<ResponseModel<AdListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AdListModel>() {
                    @Override
                    public void onSuccess(AdListModel data) {

                        if (mView != null) {
                            mView.showBanners(data);
                        }
                        SharedPreferencesUtil.name("BannerList".concat(String.valueOf(TimConfig.isRelease))).put("jsonHomeshowBanners", JsonUtil.moderToString(data)).apply();
                    }

                    @Override
                    public void onError(ResponseModel data) {

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                    }
                });
    }
}
