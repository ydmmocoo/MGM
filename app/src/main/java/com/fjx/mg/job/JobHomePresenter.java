package com.fjx.mg.job;

import com.fjx.mg.R;
import com.fjx.mg.job.fragment.JobHuntinFragment;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.library.common.base.BaseFragment;
import com.library.repository.models.TabModel;

import java.util.ArrayList;
import java.util.List;

class JobHomePresenter extends JobHomeContract.Presenter {

    JobHomePresenter(JobHomeContract.View view) {
        super(view);
    }

    @Override
    void initData() {
        ArrayList<CustomTabEntity> tabEntity = new ArrayList<>();
        //1： 招聘2：求职
        tabEntity.add(new TabModel(mView.getCurContext().getString(R.string.job_wanted)));
        tabEntity.add(new TabModel(mView.getCurContext().getString(R.string.recruit)));
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(JobHuntinFragment.newInstance(2));
        fragments.add(JobHuntinFragment.newInstance(1));
        mView.showTabAndItems(fragments, tabEntity);
    }

}
