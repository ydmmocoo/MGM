package com.fjx.mg.main;

import android.Manifest;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fjx.mg.R;
import com.fjx.mg.food.FoodFragment;
import com.fjx.mg.main.cash.CashListActivity;
import com.fjx.mg.main.fragment.friendtab.TabFriendFragment;
import com.fjx.mg.main.fragment.home.HomeFragment;
import com.fjx.mg.main.fragment.life.LifeFragment;
import com.fjx.mg.main.fragment.me.MeFragment;
import com.fjx.mg.main.fragment.news.NewsFragment;
import com.fjx.mg.scan.ScanSuccessfulActivity;
import com.fjx.mg.utils.SharedPreferencesUtils;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.library.common.base.BaseApp;
import com.library.common.base.BaseFragment;
import com.library.common.callback.CActivityManager;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.AdListModel;
import com.library.repository.models.AgentInfoModel;
import com.library.repository.models.CashListModel;
import com.library.repository.models.CheckOrderIdModel;
import com.library.repository.models.MomentsReplyListModel;
import com.library.repository.models.NewsTypeModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.TabModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.models.VersionModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

class MainPresenter extends MainContract.Presenter {
    private long mExitTime;
    private MaterialDialog cashDialog;

    MainPresenter(MainContract.View view) {
        super(view);
    }

    @Override
    void ScanResult(String scan_result) {//扫描结果
        if (mView != null) {
            if (scan_result.startsWith("fjxagent-")) {
                mView.getCurContext().startActivity(ScanSuccessfulActivity.newInstance(mView.getCurContext(), scan_result));
            } else if (scan_result.startsWith("agent-")) {
                mView.getCurContext().startActivity(ScanSuccessfulActivity.newInstance(mView.getCurContext(), scan_result));
            }
        }
    }

    @Override
    void initDatas() {

        //设置要显示的fragment
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance());
        fragments.add(NewsFragment.newInstance());
        fragments.add(FoodFragment.newInstance());
        fragments.add(TabFriendFragment.newInstance());
        fragments.add(MeFragment.newInstance());

        //设置底部tab内容数据
        ArrayList<CustomTabEntity> tabEntity = new ArrayList<>();

        //tab文字
        String[] tabTitles = new String[]{
                mView.getCurContext().getString(R.string.home),
                mView.getCurContext().getString(R.string.news),
                mView.getCurContext().getString(R.string.life),
                mView.getCurContext().getString(R.string.friend),
                mView.getCurContext().getString(R.string.my)
        };

        //选中时图标
        int[] selectIcons = new int[]{
                R.drawable.home_tab_home2,
                R.drawable.home_tab_news2,
                R.mipmap.icon_tab_life_selected,  //选中时显示的图标
                R.drawable.home_tab_friend2,
                R.drawable.home_tab_me2
        };

        //未选中时显示的图标
        int[] unSeclects = new int[]{
                R.drawable.home_tab_home1,
                R.drawable.home_tab_news1,
                R.mipmap.icon_tab_life_normal,
                R.drawable.home_tab_friend1,
                R.drawable.home_tab_me1};

        for (int i = 0; i < tabTitles.length; i++) {
            tabEntity.add(new TabModel(tabTitles[i], selectIcons[i], unSeclects[i]));
        }
        if (mView != null)
            mView.showBottomTab(fragments, tabEntity);
        baseRequest();
    }

    @Override
    public void onBackPress() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            CommonToast.toast(mView.getCurContext().getString(R.string.exit_app));
            mExitTime = System.currentTimeMillis();
        } else {
            //System.exit(0);
            //int pid = android.os.Process.myPid(); //获取当前应用程序的PID
            //android.os.Process.killProcess(pid); //杀死当前进程
            CActivityManager.finishAllActivity();
        }
    }

    @Override
    void requestPermission() {
        if (!EasyPermissions.hasPermissions(mView.getCurActivity(), Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            EasyPermissions.requestPermissions(mView.getCurActivity(), mView.getCurContext().getString(R.string.permission_contacts_message), 1,
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    void checkVersion() {
        RepositoryFactory.getRemoteRepository().checkVersion("2")
                .compose(RxScheduler.<ResponseModel<VersionModel>>toMain())
                .as(mView.<ResponseModel<VersionModel>>bindAutoDispose())
                .subscribe(new CommonObserver<VersionModel>() {
                    @Override
                    public void onSuccess(VersionModel data) {
                        if (TextUtils.isEmpty(data.getDownUrl())) {
//                            CommonToast.toast(mView.getCurContext().getString(R.string.newest_version));
                            //不弹出升级弹窗的时候，获取是否有红包可领的弹窗
                            getCashList();
                            return;
                        }
                        if (mView != null)
                            mView.showUpdateDialog(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        getCashList();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                    }
                });

    }

    @Override
    void getCashList() {
        if (!UserCenter.hasLogin()) return;
        UserInfoModel infoModel = UserCenter.getUserInfo();
        if (infoModel == null) return;
        //如果为设置手势或者 未验证绑定设备，那么清除登录信息
        if (!infoModel.hasGesture() || infoModel.isCheckDevice()) return;
        RepositoryFactory.getRemoteAccountRepository().getCashList(1, 2)
                .compose(RxScheduler.<ResponseModel<CashListModel>>toMain())
                .as(mView.<ResponseModel<CashListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CashListModel>() {
                    @Override
                    public void onSuccess(CashListModel data) {
                        if (data == null || data.getCashList() == null || data.getCashList().isEmpty())
                            return;
                        if (mView != null)
                            mView.showCashDialog();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                    }
                });

    }

    @Override
    void showCashDialog() {
        cashDialog = new MaterialDialog.Builder(mView.getCurActivity())
                .customView(R.layout.dialog_cash, true)
                .backgroundColor(ContextCompat.getColor(mView.getCurContext(), R.color.trans))
                .build();
        cashDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = cashDialog.getCustomView();
        if (view == null) return;
        TextView tvReceive = view.findViewById(R.id.tvReceive);
        tvReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashDialog.dismiss();
                mView.getCurActivity().startActivity(CashListActivity.newInstance(mView.getCurContext()));
            }
        });
        GradientDrawableHelper.whit(tvReceive).setCornerRadius(10);
        cashDialog.show();

    }

    @Override
    void requestFriendsMomentsReply(String page, String commentId, String isRead) {
        if (!UserCenter.hasLogin()) {
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
                            mView.friendsMomentsReply(data);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }


    private void baseRequest() {
//        HouseConfigModel houseConfigModel = RepositoryFactory.getLocalRepository().getHouseConfig();
//        Log.d("HouseConfigModel", JsonUtil.moderToString(houseConfigModel));
//        if (houseConfigModel == null) {
//            RepositoryFactory.getRemoteJobApi().getFabuConf()
//                    .compose(RxScheduler.<ResponseModel<HouseConfigModel>>toMain())
//                    .as(mView.<ResponseModel<HouseConfigModel>>bindAutoDispose())
//                    .subscribe(new CommonObserver<HouseConfigModel>() {
//                        @Override
//                        public void onSuccess(HouseConfigModel data) {
//                            RepositoryFactory.getLocalRepository().saveHouseConfig(data);
//                        }
//
//                        @Override
//                        public void onError(ResponseModel data) {
//
//                        }
//                    });
//        }
//
//
//        RepositoryFactory.getRemoteJobApi().getConf()
//                .compose(RxScheduler.<ResponseModel<JobConfigModel>>toMain())
//                .as(mView.<ResponseModel<JobConfigModel>>bindAutoDispose())
//                .subscribe(new CommonObserver<JobConfigModel>() {
//                    @Override
//                    public void onSuccess(JobConfigModel data) {
//                        RepositoryFactory.getLocalRepository().saveJobConfig(data);
//                    }
//
//                    @Override
//                    public void onError(ResponseModel data) {
//
//                    }
//                });

    }

    public void requestAdUrl() {
        RepositoryFactory.getRemoteRepository().getAd("5")
                .compose(RxScheduler.<ResponseModel<AdListModel>>toMain())
                .subscribe(new CommonObserver<AdListModel>() {
                    @Override
                    public void onSuccess(AdListModel data) {
                        if (data == null || data.getAdList() == null || data.getAdList().isEmpty()) {
                            SharedPreferencesUtils.setString(BaseApp.getInstance().getApplicationContext(), "ad_image", "");
                            SharedPreferencesUtils.setString(BaseApp.getInstance().getApplicationContext(), "ad_url", "");
                        }else {
                            SharedPreferencesUtils.setString(BaseApp.getInstance().getApplicationContext(), "ad_image", data.getAdList().get(0).getImg());
                            SharedPreferencesUtils.setString(BaseApp.getInstance().getApplicationContext(), "ad_url", data.getAdList().get(0).getUrl());
                        }
                        /*AdModel adModel = data.getAdList().get(0);
                        RepositoryFactory.getLocalRepository().saveEntranceAd(adModel);*/
                    }

                    @Override
                    public void onError(ResponseModel data) {

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    /**
     * user/getAgentInfo
     */
    public void requestAagentInfo(final String payCode, final String price) {
        if (mView == null) {
            return;
        }
        final MainActivity activity = (MainActivity) mView.getCurActivity();
        activity.createDialog();
        RepositoryFactory.getRemoteRepository()
                .getAgentInfo(payCode)
                .compose(RxScheduler.<ResponseModel<AgentInfoModel>>toMain())
                .subscribe(new CommonObserver<AgentInfoModel>() {
                    @Override
                    public void onSuccess(AgentInfoModel data) {
                        if (mView != null) {
                            mView.resopnseAagentInfo(data, payCode, price);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        activity.dismissDialog();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        activity.dismissDialog();
                    }
                });

    }

    public void requestSendAgent(final String price, String servicePrice, final String payCode, String status, final AgentInfoModel model) {
        if (mView == null) {
            return;
        }
        final MainActivity activity = (MainActivity) mView.getCurActivity();
        RepositoryFactory.getRemoteRepository()
                .sendAgent(price, servicePrice, payCode, status)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.responseSendAgent(model, payCode, price);
                            activity.dismissDialog();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null)
                            activity.dismissDialog();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }

    /**
     * @param outOrderId app生成的订单号,多个订单用逗号分隔
     */
    public void checkOrder(String outOrderId) {
        if (mView == null) return;
        RepositoryFactory.getRemoteRepository()
                .checkOrder(outOrderId)
                .compose(RxScheduler.<ResponseModel<CheckOrderIdModel>>toMain())
                .as(mView.<ResponseModel<CheckOrderIdModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CheckOrderIdModel>() {

                    @Override
                    public void onSuccess(CheckOrderIdModel data) {
                        String status = data.getStatus();//状态，1:等待收款,2:成功,3:超时取消
                        switch (status) {
                            case "1":
                            case "3":
                                if (mView != null)
                                    mView.showFailDialog(data.getMsg());
                                break;
                            case "2":
                                if (mView != null)
                                    mView.showSucDialog(data);
                                break;
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    void recUseApp(String appId) {
        RepositoryFactory.getRemoteNewsRepository()
                .recUseApp(appId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                    }

                    @Override
                    public void onError(ResponseModel data) {
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    void getNewsTypes() {
        final List<NewsTypeModel> dataList = DBDaoFactory.getNewsTypeDao().queryList();
        RepositoryFactory.getRemoteNewsRepository().newsTypeList()
                .compose(RxScheduler.<ResponseModel<List<NewsTypeModel>>>toMain())
                .as(mView.<ResponseModel<List<NewsTypeModel>>>bindAutoDispose())
                .subscribe(new CommonObserver<List<NewsTypeModel>>() {
                    @Override
                    public void onSuccess(List<NewsTypeModel> data) {
                        if (dataList.isEmpty()) {
                            DBDaoFactory.getNewsTypeDao().insertList(data);
                        } else {
                            //每次创建重新加载tab分类
                            DBDaoFactory.getNewsTypeDao().deleteList(dataList);
                            DBDaoFactory.getNewsTypeDao().insertList(data);
                        }

                    }

                    @Override
                    public void onError(ResponseModel data) {

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

}
