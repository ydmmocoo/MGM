package com.fjx.mg.me.score;

import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.ScoreListModel;
import com.library.repository.repository.RepositoryFactory;

public class ScorePresenter extends ScoreContract.Presenter {

    ScorePresenter(ScoreContract.View view) {
        super(view);
    }

    @Override
    void getScoreList(int page) {
        RepositoryFactory.getRemoteAccountRepository().scoreRecord(page)
                .compose(RxScheduler.<ResponseModel<ScoreListModel>>toMain())
                .as(mView.<ResponseModel<ScoreListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<ScoreListModel>() {
                    @Override
                    public void onSuccess(ScoreListModel data) {
                        if (mView == null) return;
                        mView.showScoreList(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }


}
