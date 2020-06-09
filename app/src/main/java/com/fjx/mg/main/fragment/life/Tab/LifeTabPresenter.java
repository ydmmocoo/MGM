package com.fjx.mg.main.fragment.life.Tab;

import com.fjx.mg.main.fragment.life.LifeContract;

import java.util.ArrayList;
import java.util.List;

class LifeTabPresenter extends LifeTabContract.Presenter {


    LifeTabPresenter(LifeTabContract.View view) {
        super(view);
    }

    @Override
    void initData() {
        List<Object> list = new ArrayList<>();
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        mView.showDataList(list);


    }


}
