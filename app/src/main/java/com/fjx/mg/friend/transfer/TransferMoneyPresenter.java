package com.fjx.mg.friend.transfer;

import android.text.TextUtils;
import android.util.Log;

import com.common.paylibrary.model.UsagePayMode;
import com.common.paylibrary.model.WXPayModel;
import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.List;

public class TransferMoneyPresenter extends TransferMoneyContact.Presenter {

    public TransferMoneyPresenter(TransferMoneyContact.View view) {
        super(view);
    }

    @Override
    void transMoneyByWX(String receiveUserId, String amount, String instruction) {

//        if (StringUtil.convertToFloat(amount, 0f) < 100) {
//            CommonToast.toast("转账金额不能小于100AR");
//            return;
//        }


        mView.showLoading();
        RepositoryFactory.getRemotePayRepository()
                .transferByWx(receiveUserId, amount, instruction, Constant.ImTransType.ACCOUNT)
                .compose(RxScheduler.<ResponseModel<WXPayModel>>toMain())
                .as(mView.<ResponseModel<WXPayModel>>bindAutoDispose())
                .subscribe(new CommonObserver<WXPayModel>() {
                    @Override
                    public void onSuccess(WXPayModel data) {
                        mView.hideLoading();
                        mView.getWXOrderSuccess(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });

    }

    @Override
    void payWX(WXPayModel payModel) {
        RepositoryFactory.getPayApi().weChatPay(UsagePayMode.im_transfer, payModel);
    }

    @Override
    void findUser(String imUid) {
        if (mView == null) return;
        mView.showLoading();
        RepositoryFactory.getChatRepository().getUsersProfile(imUid, true,
                new TIMValueCallBack<List<TIMUserProfile>>() {

                    @Override
                    public void onError(int i, String s) {
                        mView.hideLoading();
                        if (TextUtils.equals(s, "Err_Profile_Invalid_Account")) {
                            CommonToast.toast(mView.getCurContext().getString(R.string.no_user));
                            mView.getCurActivity().finish();
                        }
                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                        Log.d(Constant.TIM_LOG, JsonUtil.moderToString(timUserProfiles));
                        if (timUserProfiles.size() == 0) return;
                        getAllFriend(timUserProfiles.get(0));
                    }
                });
    }

    @Override
    void checkPrice(String price) {
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().checkMoneyLimit("1", price)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.checkSuccess();
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    void getAllFriend(final TIMUserProfile userProfile) {
        mView.hideLoading();
        TIMFriend friend = UserCenter.getFriend(userProfile.getIdentifier());
        if (friend == null) {
            mView.showUserInfo(userProfile);
        } else {
            mView.showUserInfo(friend);
        }
    }
}
