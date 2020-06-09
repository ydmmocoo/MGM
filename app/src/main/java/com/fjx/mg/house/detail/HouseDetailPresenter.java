package com.fjx.mg.house.detail;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fjx.mg.R;
import com.fjx.mg.dialog.CommonDialogHelper;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.fjx.mg.friend.chat.ChatActivity;
import com.fjx.mg.house.publish.HousePublishActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.IntentUtil;
import com.library.common.utils.JsonUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.HouseDetailModel;
import com.library.repository.models.NewsCommentModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.tencent.imsdk.friendship.TIMFriend;

class HouseDetailPresenter extends HouseDetailContract.Presenter {

    private MaterialDialog dialog;

    HouseDetailPresenter(HouseDetailContract.View view) {
        super(view);
    }

    @Override
    void houseDetail(String hId) {
        String uid = "";
        if (UserCenter.hasLogin()) {
            uid = UserCenter.getUserInfo().getUId();
        }

        HouseDetailModel model = DBDaoFactory.getHouseDetailDao().queryModel(hId);
        if (model != null) {
            mView.showHouseDetail(model);
        }
        RepositoryFactory.getRemoteJobApi().houseDetail(hId, uid)
                .compose(RxScheduler.<ResponseModel<HouseDetailModel>>toMain())
                .as(mView.<ResponseModel<HouseDetailModel>>bindAutoDispose())
                .subscribe(new CommonObserver<HouseDetailModel>() {
                    @Override
                    public void onSuccess(HouseDetailModel data) {
                        DBDaoFactory.getHouseDetailDao().insertModel(data);
                        Log.d("intercept", "--:" + JsonUtil.moderToString(data));
                        mView.showHouseDetail(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView == null) return;
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void toggleCollect(boolean hasCollect, String hid) {

        mView.showLoading();
        if (hasCollect) {
            RepositoryFactory.getRemoteJobApi().cancelCollectHouse(hid)
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
                            mView.hideLoading();
                            mView.toggleCollectResult(false);
                        }

                        @Override
                        public void onError(ResponseModel data) {
                            mView.hideLoading();
                            CommonToast.toast(data.getMsg());
                        }

                        @Override
                        public void onNetError(ResponseModel data) {

                        }
                    });
        } else {
            RepositoryFactory.getRemoteJobApi().collectHouse(hid)
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
                            mView.hideLoading();
                            mView.toggleCollectResult(true);
                        }

                        @Override
                        public void onError(ResponseModel data) {
                            mView.hideLoading();
                            CommonToast.toast(data.getMsg());

                        }

                        @Override
                        public void onNetError(ResponseModel data) {

                        }
                    });
        }

    }

    @Override
    void getCommentList(int page, String hid) {
        RepositoryFactory.getRemoteJobApi().houseCommentList(page, hid)
                .compose(RxScheduler.<ResponseModel<NewsCommentModel>>toMain())
                .as(mView.<ResponseModel<NewsCommentModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NewsCommentModel>() {
                    @Override
                    public void onSuccess(NewsCommentModel data) {
                        mView.showCommentList(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.loadCommentError();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void addComment(String content, String hid) {
        mView.showLoading();
        RepositoryFactory.getRemoteJobApi().addComment(content, hid)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.commentSuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void showContactDialog(final String code, final boolean isPhone) {
        String title, content, positiveText;
        String negativeText = mView.getCurContext().getString(R.string.cancel);
        if (isPhone) {
            title = mView.getCurContext().getString(R.string.call_phone);
            content = mView.getCurContext().getString(R.string.hint_confirm_contact);
            positiveText = mView.getCurContext().getString(R.string.call_phone);
        } else {
            title = mView.getCurContext().getString(R.string.contact_mg);
            content = mView.getCurContext().getString(R.string.hint_mg_contact);
            positiveText = mView.getCurContext().getString(R.string.contact_mg);
        }

        dialog = new MaterialDialog.Builder(mView.getCurActivity())
                .title(title)
                .content(content)
                .positiveText(positiveText)
                .negativeText(negativeText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        if (isPhone) {
                            IntentUtil.callPhone(code);
                        } else {
                            TIMFriend friend = UserCenter.getFriend(code);
                            if (friend == null) {
                                mView.getCurContext().startActivity(AddFriendActivity.newInstance(mView.getCurContext(), code));
                            } else {
                                String string = friend.getTimUserProfile().getNickName();
                                if (!TextUtils.isEmpty(friend.getRemark()))
                                    string = friend.getRemark();
                                ChatActivity.startC2CChat(mView.getCurActivity(), friend.getIdentifier(), string);
                            }
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .negativeText(com.tencent.qcloud.uikit.R.string.cancel).build();

        dialog.show();
    }

    @Override
    void settingOptions(int position) {
        switch (position) {
            case 0:
                closeOrOpenHousePublish(mView.getHid());
                break;
            case 1:
                mView.getCurActivity().startActivity(HousePublishActivity.newInstance(mView.getCurActivity(), mView.getType(), mView.getHid()));

                break;
            case 2:
                deleteHousePublish(mView.getHid());
                break;
            case 3://收藏
               mView.toggleCollect();
                break;
            case 4://分享
               mView.share();
                break;
                default:
                    break;
        }
    }

    @Override
    void showPublishDialog(android.view.View view, boolean isExpire, boolean isClosed, boolean isLove, boolean isShare) {
        CommonDialogHelper.showPublishDialog(mView.getCurActivity(), view, new OnSelectListener() {
            @Override
            public void onSelect(int position, String text) {
                settingOptions(position);
            }
        }, isExpire, isClosed, isLove, isShare);
    }

    @Override
    void deleteHousePublish(String hid) {
        mView.showLoading();
        RepositoryFactory.getRemoteJobApi().deleteHouse(hid)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.getCurActivity().setResult(111);
                        mView.getCurActivity().finish();
                        CommonToast.toast(mView.getCurContext().getString(R.string.delete_success));

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void closeOrOpenHousePublish(String hid) {
        String status;
        if (TextUtils.equals(mView.getStatus(), "1")) {
            status = "2";
        } else {
            status = "1";
        }
        mView.showLoading();
        RepositoryFactory.getRemoteJobApi().setStatus(hid, status)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.refreshData();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }


}
