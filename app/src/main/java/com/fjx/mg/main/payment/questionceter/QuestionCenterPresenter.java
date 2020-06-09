package com.fjx.mg.main.payment.questionceter;

import com.fjx.mg.R;
import com.fjx.mg.main.payment.questionceter.Fragment.CenterFragment;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.library.common.base.BaseFragment;
import com.library.repository.models.TabModel;

import java.util.ArrayList;
import java.util.List;

class QuestionCenterPresenter extends QuestionCenterContract.Presenter {

    QuestionCenterPresenter(QuestionCenterContract.View view) {
        super(view);
    }

    @Override
    void initData() {
        ArrayList<CustomTabEntity> tabEntity = new ArrayList<>();
        tabEntity.add(new TabModel(mView.getCurActivity().getString(R.string.going)));
        tabEntity.add(new TabModel(mView.getCurActivity().getString(R.string.finished)));
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(CenterFragment.newInstance(1));
        fragments.add(CenterFragment.newInstance(2));
        mView.showTabAndItems(fragments, tabEntity);
    }

}
