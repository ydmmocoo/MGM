package com.fjx.mg.recharge.ewnbill;

import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.ServiceModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;

public class BillPresenter extends BillContract.Presenter {
    private List<ServiceModel> serviceModels;

    BillPresenter(BillContract.View view) {
        super(view);
    }

    @Override
    void getServiceType(int type) {
        mView.showLoading();
        RepositoryFactory.getRemoteRepository().getServiceByType(type)
                .compose(RxScheduler.<ResponseModel<List<ServiceModel>>>toMain())
                .as(mView.<ResponseModel<List<ServiceModel>>>bindAutoDispose())
                .subscribe(new CommonObserver<List<ServiceModel>>() {
                    @Override
                    public void onSuccess(List<ServiceModel> data) {
                        mView.hideLoading();
                        serviceModels = data;
                        if (data.size() > 0) {
                            mView.selectService(data.get(0));
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });

    }

    @Override
    void showServiceTypeDialog() {
        if (serviceModels == null) return;
        final List<String> servicesName = new ArrayList<>();
        for (ServiceModel m : serviceModels) {
            servicesName.add(m.getServiceName());
        }
        if (servicesName.isEmpty()) return;

        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mView.getCurContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                mView.selectService(serviceModels.get(options1));
            }
        }).build();
        pvOptions.setPicker(servicesName);
        pvOptions.show();
    }
}
