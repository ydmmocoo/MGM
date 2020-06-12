package com.fjx.mg.network.mvp;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.SearchAgentModel;
import com.library.repository.repository.RepositoryFactory;

public class SalesNetworkSearchPresenter extends SalesNetworkSearchContract.Presenter {

    public SalesNetworkSearchPresenter(SalesNetworkSearchContract.View view) {
        super(view);

    }

    @Override
    public void requestAgentList(final String lng, final String lat, String type, String remark, String price, final String sName) {
        RepositoryFactory.getRemoteSalesNetworkApi()
                .agentList(lng, lat, type, remark, price)
                .compose(RxScheduler.<ResponseModel<SearchAgentModel>>toMain())
                .as(mView.<ResponseModel<SearchAgentModel>>bindAutoDispose())
                .subscribe(new CommonObserver<SearchAgentModel>() {
                    @Override
                    public void onSuccess(SearchAgentModel data) {
                        if (mView != null) {
                            mView.responseAgentList(data.getAgentList(), lng, lat, sName);
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
