package com.fjx.mg.setting.address.list;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.AddressModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.ConfirmPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;

public class AddressListPresenter extends AddressListContract.Presenter {


    public AddressListPresenter(AddressListContract.View view) {
        super(view);
    }

    @Override
    void getAddressList(int page) {
        RepositoryFactory.getRemoteAccountRepository().addressList(page)
                .compose(RxScheduler.<ResponseModel<AddressModel>>toMain())
                .as(mView.<ResponseModel<AddressModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AddressModel>() {
                    @Override
                    public void onSuccess(AddressModel data) {
                        if (mView == null) return;
                        mView.showAddressList(data);

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.loadAddressError();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                    }
                });
    }

    @Override
    void deleteAddress(String addressId, final int position) {
        RepositoryFactory.getRemoteAccountRepository().delAddress(addressId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        CommonToast.toast(R.string.delete_success);
                        mView.deleteSuccess(position);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }

    @Override
    void showDeleteDialog(final String addressId, final int position) {
        ConfirmPopupView popupView = new XPopup.Builder(mView.getCurContext())
                .asConfirm(mView.getCurActivity().getString(R.string.tips), mView.getCurContext().getString(R.string.confirm_delete_address),
                        new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                deleteAddress(addressId, position);
                            }
                        });
        popupView.show();
    }


}
