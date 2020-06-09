package com.fjx.mg.main.search;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.CompanyListModel;
import com.library.repository.models.HouseListModel;
import com.library.repository.models.JobListModel;
import com.library.repository.models.NewsItemModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.interfaces.OnSelectListener;

class SearchPresenter extends SearchContract.Presenter {

    SearchPresenter(SearchContract.View view) {
        super(view);
    }

    @Override
    void showTypeDialog(TextView view) {

        String[] texts = {mView.getCurContext().getString(R.string.news),
//                mView.getCurContext().getString(R.string.housing_resources),
//                mView.getCurContext().getString(R.string.recruit),
                mView.getCurContext().getString(R.string.yellow_page)};
        new XPopup.Builder(mView.getCurActivity())
                .atView(view)// 依附于所点击的View，内部会自动判断在上方或者下方显示
                .hasShadowBg(false)
                .popupAnimation(PopupAnimation.ScrollAlphaFromLeftTop)
                .asAttachList(texts, new int[]{},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                mView.selectType(position, text);
                            }
                        })
                .show();
    }

    @Override
    void search(int type, String content, int page) {

        switch (type) {
            case 0:
                searchNews(content, page);
                break;
            case 1:
                searchCompany(content, page);
//                searchHouse(content, page);
                break;
//            case 2:
//                searchJob(content, page);//TODO 新版本屏蔽房源和招聘
//                break;
//            case 3:
//                searchCompany(content, page);
////                searchFriend(content, page);
//                break;
        }

    }

    @Override
    protected void searchNews(String content, int page) {
        RepositoryFactory.getRemoteNewsRepository().newsList(page, content)
                .compose(RxScheduler.<ResponseModel<NewsItemModel>>toMain())
                .as(mView.<ResponseModel<NewsItemModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NewsItemModel>() {
                    @Override
                    public void onSuccess(NewsItemModel data) {
                        if (data.getNewsList().size() == 0) {
                            CommonToast.toast(mView.getCurActivity().getString(R.string.no_search_result));
                        }
                        mView.showNewsList(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.loadError();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.loadError();
                    }
                });

    }

    @Override
    protected void searchHouse(String content, int page) {
        RepositoryFactory.getRemoteJobApi().houseList(page, content)
                .compose(RxScheduler.<ResponseModel<HouseListModel>>toMain())
                .as(mView.<ResponseModel<HouseListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<HouseListModel>() {
                    @Override
                    public void onSuccess(HouseListModel data) {
                        if (data.getHouseList().size() == 0) {
                            CommonToast.toast(R.string.no_search_result);
                        }
                        mView.showHouseList(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.loadError();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.loadError();
                    }
                });

    }

    @Override
    protected void searchJob(String content, int page) {
        RepositoryFactory.getRemoteJobApi().jobsList(page, content)
                .compose(RxScheduler.<ResponseModel<JobListModel>>toMain())
                .as(mView.<ResponseModel<JobListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<JobListModel>() {
                    @Override
                    public void onSuccess(JobListModel data) {
                        if (data.getJobsList().size() == 0) {
                            CommonToast.toast(R.string.no_search_result);
                        }
                        mView.showJobList(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.loadError();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.loadError();
                    }
                });
    }

    @Override
    protected void searchFriend(String content, int page) {

    }

    @Override
    protected void searchCompany(String content, int page) {

        RepositoryFactory.getRemoteCompanyApi().companyList(page, content, "")
                .compose(RxScheduler.<ResponseModel<CompanyListModel>>toMain())
                .as(mView.<ResponseModel<CompanyListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CompanyListModel>() {
                    @Override
                    public void onSuccess(CompanyListModel data) {
                        if (data.getCompanyList().size() == 0) {
                            CommonToast.toast(R.string.no_search_result);
                        }
                        mView.showCompanyList(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.loadError();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.loadError();
                    }
                });

    }

    @Override
    void bindTextWatcher(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mView.showClearImage(!TextUtils.isEmpty(s.toString()));
            }
        });
    }


}
