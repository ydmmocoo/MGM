package com.fjx.mg.moments.all;

import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.CompanyListModel;
import com.library.repository.models.NearbyCItyGetListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

/**
 * Author    by hanlz
 * Date      on 2020/3/9.
 * Description：
 */
public class AllCityCircleAndYellowPagePresenter extends AllCityCircleAndYellowPageContract.Presenter {
    public AllCityCircleAndYellowPagePresenter(AllCityCircleAndYellowPageContract.View view) {
        super(view);
    }

    @Override
    public void requestCityList(String page, String k, String typeId, String uid) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .getListV1(page, k, typeId, uid)
                .compose(RxScheduler.<ResponseModel<NearbyCItyGetListModel>>toMain())
                .as(mView.<ResponseModel<NearbyCItyGetListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NearbyCItyGetListModel>() {
                    @Override
                    public void onSuccess(NearbyCItyGetListModel data) {
                        if (mView != null) {
                            mView.responseCityList(data);
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (mView != null) {
                            mView.responseFailed(null);
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
                        if (mView == null) return;
                        mView.responseFailed(data);
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    public void requestCompanyList(String page, String uid) {
        RepositoryFactory.getRemoteCompanyApi().companyListV2(page, "", "", "", "", "", uid)
                .compose(RxScheduler.<ResponseModel<CompanyListModel>>toMain())
                .as(mView.<ResponseModel<CompanyListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CompanyListModel>() {
                    @Override
                    public void onSuccess(CompanyListModel data) {
                        if (mView == null) return;
                        mView.responseCompanyList(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView == null) return;
                        mView.responseFailed(data);
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView == null) return;
                        mView.responseFailed(data);
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    public void requestPraise(String commentId, String replyId, String cId) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .praise(commentId, replyId, cId)
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

    public void requestCancelPraise(String commentId, String replyId, String cId) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .cancelPraise(commentId, replyId, cId)
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

}
