package com.fjx.mg.recharge.record;

import com.fjx.mg.R;
import com.fjx.mg.main.fragment.news.tab.NewsTabFragment;
import com.fjx.mg.recharge.record.fragment.BillRecordTabFragment;
import com.library.common.base.BaseFragment;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.InviteModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;

public class BillRecordPresenter extends BillRecordContract.Presenter {
    BillRecordPresenter(BillRecordContract.View view) {
        super(view);
    }

    @Override
    void initData() {
        String[] titles = new String[]{
                mView.getCurContext().getString(R.string.telephone_bill),
                mView.getCurContext().getString(R.string.flow),
                mView.getCurContext().getString(R.string.eectricity_bill),
                mView.getCurContext().getString(R.string.net_bill),
                mView.getCurContext().getString(R.string.water_bill)
        };
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(BillRecordTabFragment.newInstance(BillRecordTabFragment.TYPE_MOBILE,1));
        fragments.add(BillRecordTabFragment.newInstance(BillRecordTabFragment.TYPE_DATA,2));
        fragments.add(BillRecordTabFragment.newInstance(BillRecordTabFragment.TYPE_ELECT,3));
        fragments.add(BillRecordTabFragment.newInstance(BillRecordTabFragment.TYPE_NET,4));
        fragments.add(BillRecordTabFragment.newInstance(BillRecordTabFragment.TYPE_WATER,5));

        mView.showTabsAndFragment(titles, fragments);



    }


}
