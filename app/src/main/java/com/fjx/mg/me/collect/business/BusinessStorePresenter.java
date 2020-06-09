package com.fjx.mg.me.collect.business;


import java.util.ArrayList;
import java.util.List;

public class BusinessStorePresenter extends BusinessStoreContract.Presenter {

    BusinessStorePresenter(BusinessStoreContract.View view) {
        super(view);
    }

    @Override
    void getData() {
        List<Object> data = new ArrayList<>();
        data.add(new Object());
        data.add(new Object());
        data.add(new Object());
        data.add(new Object());
        data.add(new Object());
        mView.showDatas(data);
    }
}
