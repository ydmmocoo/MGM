package com.fjx.mg.food.presenter;

import com.fjx.mg.food.contract.StoreEvaluateContract;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.StoreEvaluateBean;
import com.library.repository.repository.RepositoryFactory;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class StoreEvaluatePresenter extends StoreEvaluateContract.Presenter {

    public StoreEvaluatePresenter(StoreEvaluateContract.View view) {
        super(view);
    }

    @Override
    public void getEvaluateList(String sId, String searchType, int page) {
        RepositoryFactory.getRemoteFoodApi().getStoreEvaluateList(sId, searchType, page)
                .compose(RxScheduler.<ResponseModel<StoreEvaluateBean>>toMain())
                .as(mView.<ResponseModel<StoreEvaluateBean>>bindAutoDispose())
                .subscribe(new CommonObserver<StoreEvaluateBean>() {
                    @Override
                    public void onSuccess(StoreEvaluateBean data) {
                        if (mView != null) {
                            mView.getEvaluateListSuccess(data);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }
                });
    }
}
