package com.fjx.mg.me.collect.house;


import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.HouseListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;

public class HouseResourcePresenter extends HouseResourceContract.Presenter {

    HouseResourcePresenter(HouseResourceContract.View view) {
        super(view);
    }


    @Override
    void getMyCollectHouse(int page) {
        RepositoryFactory.getRemoteJobApi().myCollectHouse(page)
                .compose(RxScheduler.<ResponseModel<HouseListModel>>toMain())
                .as(mView.<ResponseModel<HouseListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<HouseListModel>() {
                    @Override
                    public void onSuccess(HouseListModel data) {
                        mView.showHouseList(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                        mView.loadError();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.loadError();
                    }
                });

    }
}
