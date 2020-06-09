package com.fjx.mg.network.mvp;

import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.SearchAgentModel;
import com.library.repository.repository.RepositoryFactory;

/**
 * Author    by hanlz
 * Date      on 2020/1/14.
 * Description：
 */
public class MvolaNetworkPresenter extends MvolaNetworkContract.Presenter {
    public MvolaNetworkPresenter(MvolaNetworkContract.View view) {
        super(view);
    }

    @Override
    public void requestAgentList(final String lng, final String lat, String type, String remark, String price) {
        RepositoryFactory.getRemoteSalesNetworkApi()
                .agentList(lng, lat, type, remark,price)
                .compose(RxScheduler.<ResponseModel<SearchAgentModel>>toMain())
                .as(mView.<ResponseModel<SearchAgentModel>>bindAutoDispose())
                .subscribe(new CommonObserver<SearchAgentModel>() {
                    @Override
                    public void onSuccess(SearchAgentModel data) {
                        if (mView != null) {
                            mView.responseAgentList(data.getAgentList(), lng, lat);
                            mView.hideLoading();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                            mView.hideLoading();
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                            mView.hideLoading();
                        }
                    }
                });
    }
}
