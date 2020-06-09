package com.fjx.mg.main.fragment.friendtab;

import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.MomentsReplyListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UnReadCountBean;
import com.library.repository.repository.RepositoryFactory;

class TabFriendPresenter extends TabFriendContract.Presenter {

    TabFriendPresenter(TabFriendContract.View view) {
        super(view);
    }

    @Override
    void requestIsShowRedPoint() {
        if (!UserCenter.hasLogin()){
            return;
        }
        RepositoryFactory.getRemoteFriendsApi().unReadCount()
                .compose(RxScheduler.<ResponseModel<UnReadCountBean>>toMain())
                .as(mView.<ResponseModel<UnReadCountBean>>bindAutoDispose())
                .subscribe(new CommonObserver<UnReadCountBean>() {
                    @Override
                    public void onSuccess(UnReadCountBean data) {
                        mView.isShowRedPoint(data);
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

    void requestFriendsMomentsReply(String page, String commentId, String isRead, final String momentsFriendCount) {
        if (!UserCenter.hasLogin()){
            return;
        }
        RepositoryFactory.getRemoteRepository()
                .MomentsReplyList(page, commentId, isRead)
                .compose(RxScheduler.<ResponseModel<MomentsReplyListModel>>toMain())
                .as(mView.<ResponseModel<MomentsReplyListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<MomentsReplyListModel>() {
                    @Override
                    public void onSuccess(MomentsReplyListModel data) {
                        if (mView != null && data != null) {
                            mView.friendsMomentsReply(data,momentsFriendCount);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
//                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }


}
