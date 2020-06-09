package com.fjx.mg.nearbycity.mvp;

import android.util.Log;

import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.db.base.DBHelper;
import com.library.repository.db.model.DaoSession;
import com.library.repository.models.NearbyCItyGetListModel;
import com.library.repository.models.NearbyCItyGetListModelDao;
import com.library.repository.models.NearbyCityConfigModel;
import com.library.repository.models.NearbyCityConfigModelDao;
import com.library.repository.models.NearbyCityHotCompanyModel;
import com.library.repository.models.NearbyCityHotCompanyModelDao;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

public class NearbyCityPresenter extends NearbyCityContract.Presenter {

    public NearbyCityPresenter(NearbyCityContract.View view) {
        super(view);
    }

    @Override
    public void requestConfig() {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .getConf()
                .compose(RxScheduler.<ResponseModel<NearbyCityConfigModel>>toMain())
                .as(mView.<ResponseModel<NearbyCityConfigModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NearbyCityConfigModel>() {
                    @Override
                    public void onSuccess(NearbyCityConfigModel data) {
                        if (mView != null) {
                            mView.responseConfigDatas(data);
                            DaoSession daoSession = DBHelper.getInstance().getDaoSession();
                            NearbyCityConfigModelDao dao = daoSession.getNearbyCityConfigModelDao();
                            if (dao.loadAll().isEmpty()) {
                                dao.insertOrReplace(data);
                            } else {
                                dao.deleteAll();
                                dao.insertOrReplace(data);
                            }
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                        }
                    }
                });
    }

    @Override
    public void requestHotCompany() {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .getHotCompany()
                .compose(RxScheduler.<ResponseModel<NearbyCityHotCompanyModel>>toMain())
                .as(mView.<ResponseModel<NearbyCityHotCompanyModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NearbyCityHotCompanyModel>() {
                    @Override
                    public void onSuccess(NearbyCityHotCompanyModel data) {
                        if (mView != null) {
                            mView.responseHotCompanyDatas(data);
                            DaoSession daoSession = DBHelper.getInstance().getDaoSession();
                            NearbyCityHotCompanyModelDao dao = daoSession.getNearbyCityHotCompanyModelDao();
                            if (dao.loadAll().isEmpty()) {
                                dao.insertOrReplace(data);
                            } else {
                                dao.deleteAll();
                                dao.insertOrReplace(data);
                            }
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                        }
                    }
                });
    }


    @Override
    public void requestNearbyCityList(String page, String k, String typeId) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .getList(page, k, typeId)
                .compose(RxScheduler.<ResponseModel<NearbyCItyGetListModel>>toMain())
                .as(mView.<ResponseModel<NearbyCItyGetListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NearbyCItyGetListModel>() {
                    @Override
                    public void onSuccess(NearbyCItyGetListModel data) {
                        if (mView != null) {
                            mView.responseNearbyCityList(data);
                            DaoSession daoSession = DBHelper.getInstance().getDaoSession();
                            NearbyCItyGetListModelDao dao = daoSession.getNearbyCItyGetListModelDao();
                            if (dao.loadAll().isEmpty()) {
                                dao.insertOrReplace(data);
                            } else {
                                dao.deleteAll();
                                dao.insertOrReplace(data);
                            }
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                        }
                    }
                });
    }



    @Override
    public void requestPraise(String commentId, String replyId, String cId) {
        if (mView != null) {
            mView.showLoading();
        }
        RepositoryFactory.getRemoteNearbyCitysApi()
                .praise(commentId, replyId, cId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.responsePraise();
                            mView.hideLoading();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.hideLoading();
                            mView.responseFailed(data);
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.hideLoading();
                            mView.responseFailed(data);
                        }
                    }
                });
    }


}
