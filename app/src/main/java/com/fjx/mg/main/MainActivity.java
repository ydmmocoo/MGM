package com.fjx.mg.main;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.common.sharesdk.ShareOpenActEvent;
import com.fjx.mg.R;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.fjx.mg.friend.imuser.NewImUserDetailActivity;
import com.fjx.mg.main.event.RedPointEvent;
import com.fjx.mg.main.fragment.event.NewsEvent;
import com.fjx.mg.main.fragment.event.ResumeEvent;
import com.fjx.mg.main.fragment.home.HomeFragment;
import com.fjx.mg.main.impl.OnCountListener;
import com.fjx.mg.main.qrcode.CaptureActivity;
import com.fjx.mg.me.record.BillRecord2Activity;
import com.fjx.mg.me.transfer.MeTransferActivity;
import com.fjx.mg.mgmpay.MGMPayActivity;
import com.fjx.mg.pay.PayActivity;
import com.fjx.mg.scan.ScanShopActivity;
import com.fjx.mg.utils.DESDemo;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.fjx.mg.web.CommonWebActivity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.widget.MsgView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.common.base.BaseFragment;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.PagerAdapter;
import com.library.common.constant.IntentConstants;
import com.library.common.download.InstallReceiver;
import com.library.common.download.UpdateAppManager;
import com.library.common.utils.AppUtil;
import com.library.common.utils.CommonToast;
import com.library.common.utils.ContextManager;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.utils.StringUtil;
import com.library.common.view.shortcutbadger.ShortcutBadger;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.base.DBHelper;
import com.library.repository.db.model.DaoSession;
import com.library.repository.models.AgentInfoModel;
import com.library.repository.models.CheckOrderIdModel;
import com.library.repository.models.MomentsReplyListModel;
import com.library.repository.models.OtherUserModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.TransferLikerModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.models.VersionModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.imsdk.friendship.TIMFriendPendencyItem;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;
import com.tencent.qcloud.uikit.TimConfig;
import com.tencent.qcloud.uikit.business.session.model.SessionManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.functions.Consumer;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseMvpActivity<MainPresenter> implements MainContract.View, SessionManager.MessageUnreadWatcher, OnCountListener {

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @BindView(R.id.main_tab)
    CommonTabLayout bottomTab;

    private List<BaseFragment> fragments;

    private int lastPosition;
    private MsgView msgView;
    private MaterialDialog updateDliaog;
    private VersionModel versionModel;
    private int REQUESTCODE_STORAGE = 2;
    private int friendRequestCount;
    private int messageCount;
    private int mReplyListSize;
    private Dialog mDialog;
    DaoSession daoSession;
    List<TransferLikerModel> transferLikerModels;

    private InstallReceiver mInstallReceiver;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mCommonStatusBarEnable = false;
        super.onCreate(savedInstanceState);

        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        mPresenter.initDatas();

        mPresenter.requestPermission();
        SessionManager.getInstance().addUnreadWatcher(this);
        mPresenter.requestAdUrl();

        mPresenter.checkVersion();
        UserInfoModel infoModel = UserCenter.getUserInfo();
        if (infoModel != null) {
            //如果为设置手势或者 未验证绑定设备，那么清除登录信息
            if (!infoModel.hasGesture() || infoModel.isCheckDevice()) {
                infoModel.setToken("");
                UserCenter.saveUserInfo(infoModel);
            }
            if (TextUtils.isEmpty(infoModel.getUId())) {
                UserCenter.saveUserInfo(null);
            }
        }

        IntentFilter filters = new IntentFilter(broadcast_tocpatureac);
        registerReceiver(broadcastReceiverx, filters);
        IntentFilter filter = new IntentFilter(PayActivity.broadcast_codepay);
        registerReceiver(broadcastReceiver, filter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String code = intent.getStringExtra("code");
            String amount = intent.getStringExtra("amount");
            String status = intent.getStringExtra("status");
            WorkSend(code, status.equals("1") ? amount : "", status);
        }
    };
    BroadcastReceiver broadcastReceiverx = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (EasyPermissions.hasPermissions(MainActivity.this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), 1);
            } else {
                EasyPermissions.requestPermissions(MainActivity.this, getString(R.string.permission_camata_sd),
                        1, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    };

    public static final String broadcast_tocpatureac = "jason.broadcast.broadcast_tocpatureac";

    @Override
    public void onDestroy() {
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        if (broadcastReceiverx != null) {
            unregisterReceiver(broadcastReceiverx);
        }
        if (mInstallReceiver!=null){
            unregisterReceiver(mInstallReceiver);
        }
        dismissDialog();
        super.onDestroy();
    }


    @Override
    protected void initView() {
        super.initView();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mInstallReceiver = new InstallReceiver();
        registerReceiver(mInstallReceiver, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));

        /*String extrasMap = openActName.getString("extrasMap", "");
        if (StringUtil.isNotEmpty(extrasMap))
            openAct(extrasMap);*/

        daoSession = DBHelper.getInstance().getDaoSession();
        transferLikerModels = daoSession.loadAll(TransferLikerModel.class);
        StringBuilder stringBuilder = new StringBuilder();
        if (transferLikerModels != null && transferLikerModels.size() > 0) {
            for (TransferLikerModel model : transferLikerModels) {
                stringBuilder.append(model.getUnique());
                stringBuilder.append(",");
            }
            String orderId = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
            mPresenter.checkOrder(orderId);
        }

        if (getIntent().getExtras() != null) {
            openAct();
        }
    }

    private void openAct() {
        String type=getIntent().getExtras().getString("type");
        if ("1".equals(type)) {
            //新闻推送
            Intent intent = new Intent("mg_NewsDetailActivity");
            intent.putExtra("cid", getIntent().getExtras().getString("id"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if ("2".equals(type)) {
            //普通推送
            if (!UserCenter.hasLogin()) {
                return;
            }
            Intent intent = new Intent("com.fjx.SysNoticeActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if ("3".equals(type)) {
            //支付推送
            if (!UserCenter.hasLogin()) {
                return;
            }
            Intent intent = new Intent("com.fjx.IMNoticeActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void showBottomTab(final List<BaseFragment> fragments, ArrayList<CustomTabEntity> tabEntities) {
        this.fragments = fragments;
        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments));
        mViewPager.setOffscreenPageLimit(fragments.size());
        bottomTab.setTabData(tabEntities);
        bottomTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == fragments.size() - 1 || position == fragments.size() - 2) {
                    if (UserCenter.needLogin()) {
                        mViewPager.setCurrentItem(lastPosition, false);
                        bottomTab.setCurrentTab(lastPosition);
                    } else {
                        lastPosition = position;
                        mViewPager.setCurrentItem(position, false);
                    }
                } else {
                    lastPosition = position;
                    mViewPager.setCurrentItem(position, false);

                }
                if (position == 2) {
                    EventBus.getDefault().post(new ResumeEvent());
                } else if (position == 1) {
                    mPresenter.recUseApp("14");
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == fragments.size() - 1 || i == fragments.size() - 2) {
                    if (UserCenter.needLogin()) {
                        bottomTab.setCurrentTab(lastPosition);
                        mViewPager.setCurrentItem(lastPosition, false);
                    } else {
                        lastPosition = i;
                        bottomTab.setCurrentTab(i);
                    }
                } else {
                    lastPosition = i;
                    bottomTab.setCurrentTab(i);
                }
                if (i == 2) {
                    EventBus.getDefault().post(new ResumeEvent());
                } else if (i == 1) {
                    EventBus.getDefault().post(new NewsEvent());
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        getPendencyList();
    }

    private void initBottomCountParams() {
        try {
            msgView = bottomTab.getMsgView(3);
            msgView.setStrokeWidth(1);
            bottomTab.setMsgMargin(3, -5, 0);
            msgView.setMinWidth(DimensionUtil.dip2px(14));
            msgView.setMinHeight(DimensionUtil.dip2px(14));
            msgView.setBackgroundColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
            msgView.setTextSize(8);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) msgView.getLayoutParams();
            lp.height = ViewGroup.MarginLayoutParams.WRAP_CONTENT;
            lp.width = ViewGroup.MarginLayoutParams.WRAP_CONTENT;
            msgView.setLayoutParams(lp);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showUpdateDialog(final VersionModel data) {
        this.versionModel = data;
        updateDliaog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_update, true)
                .build();

        updateDliaog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = updateDliaog.getCustomView();
        if (view == null) return;

        TextView tvUpdate = view.findViewById(R.id.tvUpdate);
        TextView textView2 = view.findViewById(R.id.textView2);
        TextView tvVersion = view.findViewById(R.id.tvVersion);
        if (StringUtil.isNotEmpty(data.getContent())) {
            textView2.setText(data.getContent());
        } else {
            textView2.setText(getString(R.string.new_version));
        }
        if (StringUtil.isNotEmpty(data.getVersionName())) {
            tvVersion.setText("v".concat(data.getVersionName()));
        } else {
            tvVersion.setText("v".concat(AppUtil.getVersionName()));
        }
        view.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDliaog.dismiss();
            }
        });
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDliaog.dismiss();
                requestStoragePermission(data);
            }
        });
        GradientDrawableHelper.whit(tvUpdate).setColor(R.color.colorAccent).setCornerRadius(50);
        updateDliaog.show();
    }


    @Override
    public void showCashDialog() {
        mPresenter.showCashDialog();
    }

    /**
     * 下载前请求内存卡权限
     *
     * @param data
     */
    private void requestStoragePermission(final VersionModel data) {
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_storage), REQUESTCODE_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            UpdateAppManager.downloadApk(getCurContext(), data.getDownUrl(), getString(R.string.version_update), getString(R.string.app_name));
        }
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void onBackPressed() {
        mPresenter.onBackPress();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == REQUESTCODE_STORAGE) {
            if (versionModel == null) return;
            UpdateAppManager.downloadApk(getCurContext(), versionModel.getDownUrl(), getString(R.string.version_update), getString(R.string.app_name));
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //弹窗拒绝是调用
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            new AppSettingsDialog.Builder(this).build().show();
    }

    @Override
    public void updateUnread(int count) {
        messageCount = count;
        int totalCount = messageCount + friendRequestCount + mReplyListSize;
        if (count == 0) {
            EventBus.getDefault().post(new RedPointEvent(0));
        } else {
            EventBus.getDefault().post(new RedPointEvent(count));
        }
        ShortcutBadger.applyCount(ContextManager.getContext(), count);
        TimConfig.count = count;
        if (bottomTab == null) return;
        if (totalCount == 0) {
            bottomTab.hideMsg(3);
            return;
        }

        bottomTab.showMsg(3, totalCount);
        initBottomCountParams();
    }

    @Override
    public void friendsMomentsReply(MomentsReplyListModel data) {
        try {
            mReplyListSize = data.getReplyList().size();
            if (bottomTab != null) {
                if (messageCount + friendRequestCount + mReplyListSize == 0) {
                    bottomTab.hideMsg(3);
                    return;
                }
                bottomTab.showMsg(3, messageCount + friendRequestCount + mReplyListSize);
            }

            initBottomCountParams();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resopnseAagentInfo(AgentInfoModel model, String payCode, String price) {
        mPresenter.requestSendAgent(price, "", payCode, "2", model);
    }

    @Override
    public void responseSendAgent(AgentInfoModel model, String payCode, String price) {
        startActivity(ScanShopActivity.newIntent(getCurContext(), JsonUtil.moderToString(model), payCode, price));
    }

    @Override
    public void showSucDialog(CheckOrderIdModel data) {
        for (TransferLikerModel model : transferLikerModels) {
            if (TextUtils.equals(model.getUnique(), data.getOrderId())) {
                daoSession.delete(model);
            }
        }
        //转账成功显示提示框
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage(getString(R.string.transfer_suc))
                .setPositiveButton(getString(R.string.look), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        //跳转账单详情
                        startActivity(BillRecord2Activity.newInstance(MainActivity.this, "7"));
                    }
                })
                .setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        if (!dialog.isShowing() && !isFinishing()) {
            dialog.show();
        }
    }

    @Override
    public void showFailDialog(String msg) {
        //转账成功显示提示框
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage(getString(R.string.transfer_fal))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        daoSession.deleteAll(TransferLikerModel.class);
                    }
                }).create();
        if (!dialog.isShowing() && !isFinishing()) {
            dialog.show();
        }
    }

    public void getPendencyList() {
        RepositoryFactory.getChatRepository().getPendencyList(new TIMValueCallBack<TIMFriendPendencyResponse>() {
            @Override
            public void onError(int i, String s) {
                Log.d("getPendencyList", s);
            }

            @Override
            public void onSuccess(TIMFriendPendencyResponse response) {
                List<TIMFriendPendencyItem> datas = response.getItems();

                friendRequestCount = datas.size();
                if (bottomTab != null) {
                    if (messageCount + friendRequestCount + mReplyListSize == 0) {
                        bottomTab.hideMsg(3);
                        return;
                    }
                    bottomTab.showMsg(3, messageCount + friendRequestCount + mReplyListSize);
                }
                initBottomCountParams();
            }
        });
    }

    private void WorkSend(String payCode, String payPrice, String stat) {
        Log.e("payCode:" + payCode, "payPrice:" + payPrice + ";stat:" + stat);
        RepositoryFactory.getRemoteAccountRepository().workSend(payPrice, payCode, stat)
                .compose(RxScheduler.toMain())
                .as(this.bindAutoDispose())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        Gson gson = new Gson();
                        ResponseModel statusLs = gson.fromJson(o.toString(), new TypeToken<ResponseModel>() {
                        }.getType());
                        Log.e("WorkSend:" + statusLs.getCode(), "WorkSend:" + statusLs.getMsg());
                        if (statusLs.getCode() == 10000) {
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e("accept失败", "" + throwable.toString());
                    }
                });
    }

    private void WorkSends(final OtherUserModel data, final String payCode, final String payPrice, String stat) {
        Log.e("payCode:" + payCode, "payPrice:" + payPrice + ";stat:" + stat);

        RepositoryFactory.getRemoteAccountRepository().workSends(payCode, stat)
                .compose(RxScheduler.toMain())
                .as(this.bindAutoDispose())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        Gson gson = new Gson();
                        ResponseModel statusLs = gson.fromJson(o.toString(), new TypeToken<ResponseModel>() {
                        }.getType());
                        Log.e("WorkSend:" + statusLs.getCode(), "WorkSend:" + statusLs.getMsg());
                        if (statusLs.getCode() == 10000) {
                            startActivityForResult(MeTransferActivity.newInstance(getCurContext(), JsonUtil.moderToString(data), payPrice, payCode), 1);

                        }
                        dismissDialog();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e("accept失败", "" + throwable.toString());
                        dismissDialog();
                    }
                });
    }

    public void createDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(this);
        }
        mDialog.setContentView(R.layout.dialog_qr_layout);
        mDialog.getWindow().setGravity(Gravity.CENTER);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void dismissDialog() {
        if (!this.isFinishing() && mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private void GetUserInfo(final String PayCode, final String PayPrice) {
        createDialog();
        RepositoryFactory.getRemoteAccountRepository().getUserInfos(PayCode)
                .compose(RxScheduler.<ResponseModel<OtherUserModel>>toMain())
                .as(this.<ResponseModel<OtherUserModel>>bindAutoDispose())
                .subscribe(new CommonObserver<OtherUserModel>() {
                    @Override
                    public void onSuccess(OtherUserModel data) {
                        WorkSends(data, PayCode, PayPrice, "2");
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        dismissDialog();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        dismissDialog();
                    }
                });
    }

    void findImUser(String uid) {
        RepositoryFactory.getChatRepository().getUsersProfile(uid, true,
                new TIMValueCallBack<List<TIMUserProfile>>() {

                    @Override
                    public void onError(int i, String s) {
                        if (TextUtils.equals(s, "Err_Profile_Invalid_Account")) {
                            CommonToast.toast(getCurContext().getString(R.string.no_user));
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

    void getAllFriend(final TIMUserProfile userProfile) {
        TIMFriend friend = UserCenter.getFriend(userProfile.getIdentifier());
        if (friend == null) {
            getImUserSuccess(userProfile);
        } else {
            getImUserSuccess(friend);
        }
    }

    public void getImUserSuccess(TIMUserProfile profile) {
        startActivity(AddFriendActivity.newInstance(getCurContext(), JsonUtil.moderToString(profile)));
    }

    public void getImUserSuccess(TIMFriend friend) {
        startActivity(NewImUserDetailActivity.newInstance(getCurContext(),
                friend.getTimUserProfile().getIdentifier()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode && requestCode == 222) {
            bottomTab.setCurrentTab(0);
            mViewPager.setCurrentItem(0, false);
        }
        if (resultCode == -1 && requestCode == 1) {
            String stringExtra = data.getStringExtra("result");
            Log.e("requestCode:" + requestCode, ";resultCode:" + resultCode + "：stringExtra:" + stringExtra);
            SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
            String scan_result = sp.getString("scan_result");

            if (scan_result.startsWith("fjx-")) {
                if (TimConfig.isRelease) {
                    CommonToast.toast(R.string.no_result);
                } else {
                    String substring = scan_result.substring(4);
                    int i = substring.lastIndexOf("-");
                    if (i != -1) {
                        String substring1 = substring.substring(i + 1);
                        String substring2 = substring.substring(0, i);
                        GetUserInfo(substring2, substring1);
                        Log.e(substring1, substring2);
                    } else {
                        GetUserInfo(substring, "");
                    }
                }
            } else if (scan_result.startsWith("mg-")) {
                if (TimConfig.isRelease) {
                    String substring = scan_result.substring(3);
                    int i = substring.lastIndexOf("-");
                    if (i != -1) {
                        String substring1 = substring.substring(i + 1);
                        String substring2 = substring.substring(0, i);
                        GetUserInfo(substring2, substring1);
                        Log.e(substring1, substring2);
                    } else {
                        GetUserInfo(substring, "");
                    }
                    Log.d("结果2", substring);
                } else {
                    CommonToast.toast(R.string.no_result);
                }
            } else if (scan_result.startsWith("fjxagent-")) {
                mPresenter.ScanResult(scan_result);
            } else if (scan_result.startsWith("agent-")) {
                mPresenter.ScanResult(scan_result);
            } else if (scan_result.contains("/invite/index")) {//解密加好友
                int i = scan_result.lastIndexOf("/invite/index/");
                String substring = scan_result.substring(i + 14);
                Log.e("切割字符：", "" + substring);
                try {
                    byte[] bytes = DESDemo.decodeEBC(TimConfig.getKey(), Base64.decode(substring, 0));
                    Log.e("个人符号ID：", "" + new String(bytes));
                    if (UserCenter.getUserInfo().getIdentifier().equals(new String(bytes))) {
//                        CommonToast.toast("");
                    } else {
                        Log.e("FindFriend：", "FindFriend");
//                        GetUserInfobyIm(new String(bytes));
                        findImUser(new String(bytes));
                    }
                } catch (Exception e) {
                    Log.e("Exception：", "" + e.toString());
                    e.printStackTrace();
                }
            } else if (scan_result.startsWith("fjxshop-")) {
                String payCode = scan_result.substring(8);
                int i = payCode.lastIndexOf("-");
                if (i != -1) {
                    String substring2 = payCode.substring(0, i);
                    String substring1 = payCode.substring(i + 1);
                    mPresenter.requestAagentInfo(substring2, substring1);
                } else {
                    mPresenter.requestAagentInfo(payCode, "");
                }
                Log.e("结果2", payCode);
            } else if (scan_result.startsWith("mgshop-")) {
                String payCode = scan_result.substring(7);
                int i = payCode.lastIndexOf("-");
                if (i != -1) {
                    String substring2 = payCode.substring(0, i);
                    String substring1 = payCode.substring(i + 1);
                    mPresenter.requestAagentInfo(substring2, substring1);
                } else {
                    mPresenter.requestAagentInfo(payCode, "");
                }
                Log.e("结果2", payCode);
            } else if (scan_result.contains("www")) {
                CommonWebActivity.Options options = new CommonWebActivity.Options();
                options.setTitle("");
                options.setLoadUrl(scan_result);
                startActivity(CommonWebActivity.newInstance(MainActivity.this, JsonUtil.moderToString(options)));
            } else if (scan_result.startsWith("wfjx:")) {
                int index = scan_result.indexOf(":");
                String orderString = scan_result.substring(index + 1);
                Intent intent = new Intent(getCurContext(), MGMPayActivity.class);
                intent.putExtra(IntentConstants.ORDER_STRING, orderString);
                intent.putExtra(IntentConstants.PRICE, 0.0f);
                startActivity(intent);
                Log.e("结果2", scan_result);
            } else if (scan_result.startsWith(com.library.common.constant.Constant.WFJX) || scan_result.startsWith(com.library.common.constant.Constant.WMG)) {
                String v1 = scan_result.substring(scan_result.indexOf("-") + 1);
                String type = v1.substring(0, v1.indexOf("-"));
                String v2 = v1.substring(v1.indexOf("-") + 1);
                String payCode = v2.substring(0, v2.indexOf("-"));
                String v3 = v2.substring(v2.indexOf("-") + 1);
                String orderId = v3.substring(0, v3.indexOf("-"));
                String price = v3.substring(v3.indexOf("-") + 1);
                Map<String, Object> map = new HashMap<>();
                map.put("orderId", orderId);
                map.put("amount", price);
                map.put("type", type);
                map.put("payCode", payCode);
                String ext = JsonUtil.moderToString(new PayExtModel(UsagePayMode.three_scan_pay, map));
                startActivityForResult(PayActivity.newInstance(getCurContext(), ext), 123);
            } else {
                CommonToast.toast(scan_result);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.requestFriendsMomentsReply("1", "", "2");
    }

    @Override
    public void onCount(int count) {
        mPresenter.requestFriendsMomentsReply("1", "", "2");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventOpenAct(ShareOpenActEvent event) {
    }
}
