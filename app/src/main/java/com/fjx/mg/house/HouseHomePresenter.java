package com.fjx.mg.house;

import com.fjx.mg.R;
import com.fjx.mg.house.fragment.HouseLeaseFragment;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.library.common.base.BaseFragment;
import com.library.repository.models.TabModel;

import java.util.ArrayList;
import java.util.List;

class HouseHomePresenter extends HouseHomeContract.Presenter {

    HouseHomePresenter(HouseHomeContract.View view) {
        super(view);
    }

    @Override
    void initData() {
        ArrayList<CustomTabEntity> tabEntity = new ArrayList<>();
        //	1:租房，2：售房
        tabEntity.add(new TabModel(mView.getCurContext().getString(R.string.Renting)));
        tabEntity.add(new TabModel(mView.getCurContext().getString(R.string.sell_house)));
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(HouseLeaseFragment.newInstance(1));
        fragments.add(HouseLeaseFragment.newInstance(2));
        mView.showTabAndItems(fragments, tabEntity);
    }

}
