package com.fjx.mg.main.fragment.friendnew;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fjx.mg.R;
import com.fjx.mg.dialog.FriendSettingDialogx;
import com.fjx.mg.friend.addfriend.SearchFriendActivity;
import com.fjx.mg.friend.chat.ChatActivity;
import com.fjx.mg.friend.contacts.FriendContactsActivity;
import com.fjx.mg.friend.nearby.NearbyActivity;
import com.fjx.mg.friend.notice.pay.IMNoticeActivity;
import com.fjx.mg.friend.notice.sys.SysNoticeActivity;
import com.fjx.mg.friend.search.FriendSearchActivity;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.main.scan.QRCodeCollectionActivity;
import com.fjx.mg.utils.DateFormateUtil;
import com.library.common.base.BaseFragment;
import com.library.common.receiver.NetStateReceiver;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.IMNoticeListModel;
import com.library.repository.models.IMNoticeModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriendPendencyItem;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;
import com.tencent.qcloud.uikit.business.session.model.SessionInfo;
import com.tencent.qcloud.uikit.business.session.view.SessionPanel;
import com.tencent.qcloud.uikit.business.session.view.wedgit.SessionClickListener;
import com.tencent.qcloud.uikit.operation.group.event.GroupConversionRefreshEvent;
import com.tencent.qcloud.uikit.receiver.ImLoginReceiver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description：聊天
 */
public class NewFriendFragment extends BaseFragment implements SessionClickListener {
    @BindView(R.id.session_panel)
    SessionPanel sessionPanel;

    @BindView(R.id.session_last_msg)
    TextView tvMessge;

    @BindView(R.id.session_time)
    TextView tvTime;

    @BindView(R.id.ivAddFriend)
    ImageView ivAddFriend;

    @BindView(R.id.viewDot)
    TextView viewDot;
    @BindView(R.id.tvNetError)
    TextView tvNetError;

    private String reequestNum;

    private IMNoticeListModel noticeListModel;
    private IMNoticeListModel sysNoticeListModel;
    private ImLoginReceiver imLoginReceiver;
    private BroadcastReceiver netStateReceiver;
    private FriendSettingDialogx settingDialog;

    public static NewFriendFragment newInstance() {
        Bundle args = new Bundle();
        NewFriendFragment fragment = new NewFriendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_new_friend;
    }


    private void initView() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        // 会话面板初始化默认功能
        UserCenter.imLogin();
        sessionPanel.mTitleBar.setVisibility(View.GONE);
        sessionPanel.setSessionClick(this);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GradientDrawableHelper.whit(viewDot).setColor(R.color.white);
        initView();
        imLoginReceiver = registImLoginReceiver();
        netStateReceiver = registNetStateReceiver();
    }


    @Override
    public void onSessionClick(SessionInfo session) {
        //此处为demo的实现逻辑，更根据会话类型跳转到相关界面，开发者可根据自己的应用场景灵活实现
        if (session.isGroup()) {
//            如果是群组，跳转到群聊界面
            ChatActivity.startGroupChat(getActivity(), session.getPeer());
        } else {
            String nickName = session.getTitle();
//            否则跳转到C2C单聊界面
            ChatActivity.startC2CChat(getActivity(), session.getPeer(), nickName);
        }
    }

    @OnClick({R.id.ivContacts, R.id.ivAddFriend, R.id.rlPayNotice, R.id.rlSysNotice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivContacts:
                startActivity(FriendSearchActivity.newInstance(getCurContext()));
                break;
            case R.id.ivAddFriend:
                showSettingDialog();
                break;

            case R.id.rlPayNotice:
                if (noticeListModel != null)
                    startActivity(IMNoticeActivity.newInstance(getCurContext(), JsonUtil.moderToString(noticeListModel)));
                break;

            case R.id.rlSysNotice:
                if (sysNoticeListModel != null)
                    startActivity(SysNoticeActivity.newInstance(getCurContext(), JsonUtil.moderToString(sysNoticeListModel)));
                break;
        }

    }


    @Override
    public void doLoadVisible() {
        if (UserCenter.hasImLogin()) {

            sessionPanel.initDefault();
            UserCenter.getAllFriend();
            Log.d("doLoadVisible", "doLoadVisible");
            getNotice();
            getPendencyList();
        }
    }

    private void getNotice() {
        Log.d("getNotice", "getNotice: ");
        RepositoryFactory.getRemotePayRepository().payNoticeList(1, 2)
                .compose(RxScheduler.<ResponseModel<IMNoticeListModel>>toMain())
                .as(this.<ResponseModel<IMNoticeListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<IMNoticeListModel>() {
                    @Override
                    public void onSuccess(IMNoticeListModel data) {
                        if (data != null) {
                            getSystemNotice();
                            noticeListModel = data;
                            tvMessge.setText(getString(R.string.hava_none_notice));
                            if (data.getRecordList() == null || data.getRecordList().size() <= 0 || data.getRecordList().isEmpty())
                                return;
                            if (null == data.getRecordList().get(0)) return;
                            IMNoticeModel model = data.getRecordList().get(0);
                            tvMessge.setText(model.getTitle());

                            //2019-06-05 17:03:31
                            String createDate = model.getCreateTime();
                            tvTime.setText(createDate.substring(10, createDate.length() - 3));
                            String lastDate = RepositoryFactory.getLocalRepository().getLaseImNoticeDate();
                            if (DateFormateUtil.getTime(createDate) - DateFormateUtil.getTime(lastDate) > 0) {
                                //有新消息
                                RepositoryFactory.getLocalRepository().saveLastIMNoticeDate(createDate);
                            } else {

                            }
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

    private void getSystemNotice() {
        RepositoryFactory.getRemotePayRepository().payNoticeList(1, 1)
                .compose(RxScheduler.<ResponseModel<IMNoticeListModel>>toMain())
                .as(this.<ResponseModel<IMNoticeListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<IMNoticeListModel>() {
                    @Override
                    public void onSuccess(IMNoticeListModel data) {
                        sysNoticeListModel = data;
                    }

                    @Override
                    public void onError(ResponseModel data) {

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }


    private void showSettingDialog() {
//        FriendSettingDialog settingDialog = new FriendSettingDialog(getCurActivity());
        settingDialog = new FriendSettingDialogx(getCurActivity(), new FriendSettingDialogx.FriendSettingDialogxClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tvAddFriend:
                        getContext().startActivity(SearchFriendActivity.newInstance(getContext()));
                        settingDialog.dismiss();
                        break;
                    case R.id.tvContact://通讯录
                        startActivityForResult(FriendContactsActivity.newInstance(getContext(), false), 1212);
                        settingDialog.dismiss();
                        break;
                    case R.id.nearAddFriend://附近的人
                        getContext().startActivity(NearbyActivity.newInstance(getActivity()));
                        settingDialog.dismiss();
                        break;
                    case R.id.tvQrScan:
                        settingDialog.dismiss();
                        Intent intent = new Intent(MainActivity.broadcast_tocpatureac);
                        getActivity().sendBroadcast(intent);
                        break;

                    case R.id.tvQrCode:
                        getContext().startActivity(QRCodeCollectionActivity.newInstance(getContext()));
                        settingDialog.dismiss();
                        break;
                }
            }
        });
        settingDialog.showDot(reequestNum);
        new XPopup.Builder(getCurContext()).atView(ivAddFriend).asCustom(settingDialog).show();
    }


    private void getPendencyList() {
        ((MainActivity) getCurActivity()).getPendencyList();
        RepositoryFactory.getChatRepository().getPendencyList(new TIMValueCallBack<TIMFriendPendencyResponse>() {
            @Override
            public void onError(int i, String s) {
                Log.d("getPendencyList", s);

            }

            @Override
            public void onSuccess(TIMFriendPendencyResponse response) {
                List<TIMFriendPendencyItem> datas = response.getItems();
                reequestNum = datas.size() + "";
                if (viewDot == null) return;
                if (TextUtils.isEmpty(reequestNum) || TextUtils.equals(reequestNum, "0")) {
                    viewDot.setVisibility(View.GONE);
                } else {
                    viewDot.setVisibility(View.VISIBLE);
                }
                viewDot.setText(reequestNum);
            }
        });
    }

    public ImLoginReceiver registImLoginReceiver() {

        ImLoginReceiver imLoginReceiver = new ImLoginReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ImLoginReceiver.MG_LOGIN_SUCCESS);
        intentFilter.addAction(ImLoginReceiver.MG_LOGIN_FAILED);
        imLoginReceiver.setImLogin(new ImLoginReceiver.IMLogin() {
            @Override
            public void loginSuccess() {
                tvNetError.setVisibility(View.GONE);
                if (UserCenter.hasImLogin()) {
                    sessionPanel.initDefault();
                }
            }

            @Override
            public void loginFailed() {
                tvNetError.setVisibility(View.VISIBLE);
            }
        });
        getActivity().registerReceiver(imLoginReceiver, intentFilter);
        return imLoginReceiver;
    }

    public NetStateReceiver registNetStateReceiver() {
        NetStateReceiver netStateReceiver = new NetStateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        netStateReceiver.setOnNetworkStateChangeListener(new NetStateReceiver.OnNetworkStateChangeListener() {
            @Override
            public void onStateChange(int curState) {
                if (curState == NetStateReceiver.STATE_NO_CONNECT) {
                    tvNetError.setVisibility(View.VISIBLE);
                } else {
                    tvNetError.setVisibility(View.GONE);
                    if (UserCenter.hasImLogin()) return;
                    UserCenter.imLogin();
                }
            }
        });
        getActivity().registerReceiver(netStateReceiver, intentFilter);
        return netStateReceiver;
    }

    @Override
    public void onDestroy() {
        if (imLoginReceiver != null) {
            getActivity().unregisterReceiver(imLoginReceiver);
        }
        if (netStateReceiver != null) {
            getActivity().unregisterReceiver(netStateReceiver);
        }
        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1212) {
            if (requestCode == -1) {
                sessionPanel.initDefault();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRefreshEvent(GroupConversionRefreshEvent event) {
        sessionPanel.initDefault();
    }
}
