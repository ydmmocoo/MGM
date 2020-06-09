package com.fjx.mg.job.detail;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.fjx.mg.R;
import com.fjx.mg.dialog.CommonDialogHelper;
import com.fjx.mg.job.publish.JobPublicActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.JobModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.List;

public class JobDetailPresenter extends JobDetailContract.Presenter {

    JobDetailPresenter(JobDetailContract.View view) {
        super(view);
    }

    @Override
    void getJobDetail(String jobId) {

        String uid = "";
        if (UserCenter.hasLogin()) {
            uid = UserCenter.getUserInfo().getUId();
        }
        JobModel model = DBDaoFactory.getJobListModelDao().queryModel(jobId);
        if (model != null) {
            mView.showJobDetaol(model);
        }

        mView.showLoading();
        RepositoryFactory.getRemoteJobApi().jobDetail(jobId, uid)
                .compose(RxScheduler.<ResponseModel<JobModel>>toMain())
                .as(mView.<ResponseModel<JobModel>>bindAutoDispose())
                .subscribe(new CommonObserver<JobModel>() {
                    @Override
                    public void onSuccess(JobModel data) {
                        DBDaoFactory.getJobListModelDao().insertModel(data);
                        mView.hideLoading();
                        mView.showJobDetaol(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
//                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void toggleCollect(String jobId, boolean hasCollect) {

        mView.showLoading();
        if (hasCollect) {
            RepositoryFactory.getRemoteJobApi().cancelCollectJob(jobId)
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
//                            CommonToast.toast(data.getMsg());
                        }

                        @Override
                        public void onNetError(ResponseModel data) {

                        }
                    });

        } else {
            RepositoryFactory.getRemoteJobApi().collectJob(jobId)
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
//                            CommonToast.toast(data.getMsg());
                        }

                        @Override
                        public void onNetError(ResponseModel data) {

                        }
                    });

        }

    }

    @Override
    void findImUser(String uid) {
        RepositoryFactory.getChatRepository().getUsersProfile(uid, true,
                new TIMValueCallBack<List<TIMUserProfile>>() {

                    @Override
                    public void onError(int i, String s) {
                        if (TextUtils.equals(s, "Err_Profile_Invalid_Account")) {
                            CommonToast.toast(mView.getCurContext().getString(R.string.no_user));
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
    void showPublishDialog(View view, boolean isLove, boolean isShare) {
        JobModel model = mView.getJobModel();
        boolean isClosed = TextUtils.equals(model.getStatus(), "1");
        boolean isExpire = TextUtils.equals(model.getStatus(), "3");
        CommonDialogHelper.showPublishDialog(mView.getCurActivity(), view, new OnSelectListener() {
            @Override
            public void onSelect(int position, String text) {
                settingOptions(position);
            }
        }, isExpire, isClosed, isLove, isShare);

    }

    private void settingOptions(int position) {
        switch (position) {
            case 0://关闭或者开启
                closeOrOpenHousePublish(mView.getJobModel().getJobId());
                break;
            case 1://编辑
                mView.getCurActivity().startActivityForResult(JobPublicActivity
                        .newInstance(mView.getCurContext(), mView.getJobModel().getType(), mView.getJobModel().getJobId()), 11);

                break;
            case 2://删除
                deleteJob(mView.getJobModel().getJobId());
                break;
            case 3://收藏
                mView.toggleCollect();
                break;
            case 4://分享
                mView.share();
                break;
        }
    }

    void getAllFriend(final TIMUserProfile userProfile) {
        TIMFriend friend = UserCenter.getFriend(userProfile.getIdentifier());
        if (friend == null) {
            mView.getImUserSuccess(userProfile);
        } else {
            mView.getImUserSuccess(friend);
        }
    }

    private void deleteJob(String jobId) {
        mView.showLoading();
        RepositoryFactory.getRemoteJobApi().deleteJob(jobId)
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
//                        CommonToast.toast(data.getMsg());
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    private void closeOrOpenHousePublish(String jid) {
        if (mView.getJobModel() == null) return;

        String status = mView.getJobModel().getStatus();
        if (TextUtils.equals(status, "1")) {
            status = "2";
        } else {
            status = "1";
        }
        mView.showLoading();
        RepositoryFactory.getRemoteJobApi().setJobStatus(jid, status)
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
//                        CommonToast.toast(data.getMsg());
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }
}
