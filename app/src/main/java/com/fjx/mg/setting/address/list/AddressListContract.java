package com.fjx.mg.setting.address.list;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.AddressModel;

public interface AddressListContract {

    interface View extends BaseView {
        void showAddressList(AddressModel addressModel);

        void loadAddressError();

        void deleteSuccess(int position);


    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void getAddressList(int page);

        abstract void deleteAddress(String addressId, int position);

        abstract void showDeleteDialog(String addressId, int position);

    }

}
