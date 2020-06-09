package com.fjx.mg.friend.chat.redpacket;

import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.GroupRedPacketDetailModel;
import com.library.repository.models.ReciveRedRrecordModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

/**
 * Author    by hanlz
 * Date      on 2019/12/8.
 * Description：
 */
public class GroupRedPacketDetailPresenter extends GroupRedPacketDetailContact.Presenter {


    public GroupRedPacketDetailPresenter(GroupRedPacketDetailContact.View view) {
        super(view);
    }

    @Override
    public void requestRedPacketDetail(String rId) {
        RepositoryFactory.getRemotePayRepository()
                .redEnvelopeInfo(rId)
                .compose(RxScheduler.<ResponseModel<GroupRedPacketDetailModel>>toMain())
                .as(mView.<ResponseModel<GroupRedPacketDetailModel>>bindAutoDispose())
                .subscribe(new CommonObserver<GroupRedPacketDetailModel>() {
                    @Override
                    public void onSuccess(GroupRedPacketDetailModel data) {
                        if (mView != null) {
                            mView.responseRedPacketDetail(data);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    public void requestReciveRedRrecord(String rId, String page) {
        RepositoryFactory.getRemotePayRepository()
                .reciveRedRrecord(rId,page)
                .compose(RxScheduler.<ResponseModel<ReciveRedRrecordModel>>toMain())
                .as(mView.<ResponseModel<ReciveRedRrecordModel>>bindAutoDispose())
                .subscribe(new CommonObserver<ReciveRedRrecordModel>() {
                    @Override
                    public void onSuccess(ReciveRedRrecordModel data) {
                        if (mView!=null){
                            mView.responseReciveRedRrecord(data);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }
                });
    }
}
