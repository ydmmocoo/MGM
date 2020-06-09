package com.fjx.mg.me.wallet.detail;

import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.BalanceDetailModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

public class BalanceDetailPresenter extends BalanceDetailContract.Presenter {


    BalanceDetailPresenter(BalanceDetailContract.View view) {
        super(view);
    }

    @Override
    void getBalanceList(final int page) {
        RepositoryFactory.getRemoteAccountRepository().balanceRecod(page)
                .compose(RxScheduler.<ResponseModel<BalanceDetailModel>>toMain())
                .as(mView.<ResponseModel<BalanceDetailModel>>bindAutoDispose())
                .subscribe(new CommonObserver<BalanceDetailModel>() {
                    @Override
                    public void onSuccess(BalanceDetailModel data) {
                        mView.showBalanceDetail(data,page);


                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                        mView.loadBalanceError();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }


}
