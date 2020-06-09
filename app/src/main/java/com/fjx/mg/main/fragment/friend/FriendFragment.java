//package com.fjx.mg.main.fragment.friend;
//
//import android.content.BroadcastReceiver;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.ConnectivityManager;
//import android.net.wifi.WifiManager;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.fjx.mg.R;
//import com.fjx.mg.dialog.FriendSettingDialogx;
//import com.fjx.mg.friend.addfriend.SearchFriendActivity;
//import com.fjx.mg.friend.chat.ChatActivity;
//import com.fjx.mg.friend.chat.StartGroupChatActivity;
//import com.fjx.mg.friend.contacts.FriendContactsActivity;
//import com.fjx.mg.friend.nearby.NearbyActivity;
//import com.fjx.mg.friend.notice.pay.IMNoticeActivity;
//import com.fjx.mg.friend.notice.sys.SysNoticeActivity;
//import com.fjx.mg.friend.search.FriendSearchActivity;
//import com.fjx.mg.main.MainActivity;
//import com.fjx.mg.main.fragment.event.ImLoginEvent;
//import com.fjx.mg.main.scan.QRCodeCollectionActivity;
//import com.fjx.mg.utils.DateFormateUtil;
//import com.library.common.base.BaseFragment;
//import com.library.common.receiver.NetStateReceiver;
//import com.library.common.utils.GradientDrawableHelper;
//import com.library.common.utils.JsonUtil;
//import com.library.repository.core.net.CommonObserver;
//import com.library.repository.core.net.RxScheduler;
//import com.library.repository.data.UserCenter;
//import com.library.repository.models.IMNoticeListModel;
//import com.library.repository.models.IMNoticeModel;
//import com.library.repository.models.ResponseModel;
//import com.library.repository.repository.RepositoryFactory;
//import com.lxj.xpopup.XPopup;
//import com.tencent.imsdk.TIMConversationType;
//import com.tencent.imsdk.TIMValueCallBack;
//import com.tencent.imsdk.friendship.TIMFriend;
//import com.tencent.imsdk.friendship.TIMFriendPendencyItem;
//import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;
//import com.tencent.qcloud.uikit.business.session.model.SessionInfo;
//import com.tencent.qcloud.uikit.business.session.view.SessionPanel;
//import com.tencent.qcloud.uikit.business.session.view.wedgit.SessionClickListener;
//import com.tencent.qcloud.uikit.receiver.ImLoginReceiver;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
///**
// * 好友聊天列表页
// */
//public class FriendFragment extends BaseFragment implements SessionClickListener {
//    @BindView(R.id.session_panel)
//    SessionPanel sessionPanel;
//
//    @BindView(R.id.session_last_msg)
//    TextView tvMessge;
//
//    @BindView(R.id.session_time)
//    TextView tvTime;
//
//    @BindView(R.id.ivAddFriend)
//    ImageView ivAddFriend;
//
//    @BindView(R.id.viewDot)
//    TextView viewDot;
//    @BindView(R.id.tvNetError)
//    TextView tvNetError;
//
//    private String reequestNum;
//
//    private IMNoticeListModel noticeListModel;
//    private IMNoticeListModel sysNoticeListModel;
//    private ImLoginReceiver imLoginReceiver;
//    private BroadcastReceiver netStateReceiver;
//    private FriendSettingDialogx settingDialog;
//
//    public static FriendFragment newInstance() {
//        Bundle args = new Bundle();
//        FriendFragment fragment = new FriendFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    protected int layoutId() {
//        return R.layout.fragment_friend;
//    }
//
//
//    private void initView() {
////        if (UserCenter.isTimFirstLogin()) {
//        // 会话面板初始化默认功能
//        UserCenter.imLogin();
//        sessionPanel.initDefault();
//        sessionPanel.mTitleBar.setVisibility(View.GONE);
//        sessionPanel.setSessionClick(this);
////        }
//    }
//
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
//        GradientDrawableHelper.whit(viewDot).setColor(R.color.white);
//        initView();
//        imLoginReceiver = registImLoginReceiver();
//        netStateReceiver = registNetStateReceiver();
//    }
//
//
////    @Override
////    public void onResume() {
////        super.onResume();
////        Log.d("im_session","onResume");
////        if (UserCenter.hasImLogin()) {
////            sessionPanel.refresh();
////            UserCenter.getAllFriend();
////        } else {
////            initView();
////        }
////        getNotice();
////        getPendencyList();
////    }
//
//
//    @Override
//    public void onSessionClick(SessionInfo session) {
//        //此处为demo的实现逻辑，更根据会话类型跳转到相关界面，开发者可根据自己的应用场景灵活实现
//        if (session.isGroup()) {
////            如果是群组，跳转到群聊界面
////            ChatInfo chatInfo = new ChatInfo();
////            chatInfo.setType(TIMConversationType.Group);
////            chatInfo.setId(session.getPeer());
////            chatInfo.setChatName(groupInfo.getGroupName());
////            Intent intent = new Intent(getCurActivity(), ChatActivity.class);
////            intent.putExtra(IntentConstants.CHAT_INFO, chatInfo);
////            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            ChatActivity.startGroupChat(getActivity(), session.getPeer());
//            ChatActivity.startGroupChat(getActivity(), session.getPeer());
//        } else {
//            String nickName = session.getPeer();
//            TIMFriend friend = UserCenter.getFriend(session.getPeer());
//            if (friend != null) {
//                if (!TextUtils.isEmpty(friend.getRemark())) {
//                    nickName = friend.getRemark();
//                } else if (!TextUtils.isEmpty(friend.getTimUserProfile().getNickName())) {
//                    nickName = friend.getTimUserProfile().getNickName();
//                }
//            }
//
////            否则跳转到C2C单聊界面
//            ChatActivity.startC2CChat(getActivity(), session.getPeer(), nickName);
//
//        }
//    }
//
//    @OnClick({R.id.ivContacts, R.id.ivAddFriend, R.id.rlPayNotice, R.id.rlSysNotice})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.ivContacts:
//                startActivity(FriendSearchActivity.newInstance(getCurContext()));
//                break;
//            case R.id.ivAddFriend:
//                showSettingDialog();
//                break;
//
//            case R.id.rlPayNotice:
//                startActivity(IMNoticeActivity.newInstance(getCurContext(), JsonUtil.moderToString(noticeListModel)));
//                break;
//
//            case R.id.rlSysNotice:
//                startActivity(SysNoticeActivity.newInstance(getCurContext(), JsonUtil.moderToString(sysNoticeListModel)));
//                break;
//        }
//
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onIMLoginEvent(ImLoginEvent event) {
//        checkLogin();
//    }
//
//    @Override
//    public void doLoadVisible() {
//        checkLogin();
//    }
//
//    /**
//     * 检查是否登录IM
//     * --是：刷新界面
//     * --否：执行登录操作且创建im会话列表
//     */
//    private void checkLogin() {
//        if (UserCenter.hasImLogin()) {
//            sessionPanel.refresh();
//            UserCenter.getAllFriend();
//        } else {
//            initView();
//        }
//        getNotice();
//        getPendencyList();
//    }
//
//    private void getNotice() {
//        Log.d("getNotice", "getNotice: ");
//        RepositoryFactory.getRemotePayRepository().payNoticeList(1, 2)
//                .compose(RxScheduler.<ResponseModel<IMNoticeListModel>>toMain())
//                .as(this.<ResponseModel<IMNoticeListModel>>bindAutoDispose())
//                .subscribe(new CommonObserver<IMNoticeListModel>() {
//                    @Override
//                    public void onSuccess(IMNoticeListModel data) {
//                        getSystemNotice();
//                        noticeListModel = data;
//                        tvMessge.setText(getString(R.string.hava_none_notice));
//                        if (data.getRecordList().isEmpty()) return;
//                        IMNoticeModel model = data.getRecordList().get(0);
//                        tvMessge.setText(model.getTitle());
//
//                        //2019-06-05 17:03:31
//                        String createDate = model.getCreateTime();
//                        tvTime.setText(createDate.substring(10, createDate.length() - 3));
//                        String lastDate = RepositoryFactory.getLocalRepository().getLaseImNoticeDate();
//                        if (DateFormateUtil.getTime(createDate) - DateFormateUtil.getTime(lastDate) > 0) {
//                            //有新消息
//                            RepositoryFactory.getLocalRepository().saveLastIMNoticeDate(createDate);
//                        } else {
//
//                        }
//                    }
//
//                    @Override
//                    public void onError(ResponseModel data) {
//
//                    }
//                });
//
//
//    }
//
//    private void getSystemNotice() {
//        RepositoryFactory.getRemotePayRepository().payNoticeList(1, 1)
//                .compose(RxScheduler.<ResponseModel<IMNoticeListModel>>toMain())
//                .as(this.<ResponseModel<IMNoticeListModel>>bindAutoDispose())
//                .subscribe(new CommonObserver<IMNoticeListModel>() {
//                    @Override
//                    public void onSuccess(IMNoticeListModel data) {
//                        sysNoticeListModel = data;
//                    }
//
//                    @Override
//                    public void onError(ResponseModel data) {
//
//                    }
//                });
//    }
//
//
//    private void showSettingDialog() {
////        FriendSettingDialog settingDialog = new FriendSettingDialog(getCurActivity());
//        settingDialog = new FriendSettingDialogx(getCurActivity(), new FriendSettingDialogx.FriendSettingDialogxClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (view.getId()) {
//                    case R.id.tvAddFriend:
//                        getContext().startActivity(SearchFriendActivity.newInstance(getContext()));
//                        settingDialog.dismiss();
//                        break;
//                    case R.id.tvContact:
//                        getContext().startActivity(FriendContactsActivity.newInstance(getContext(), false));
//                        settingDialog.dismiss();
//                        break;
//                    case R.id.nearAddFriend:
//                        getContext().startActivity(NearbyActivity.newInstance(getActivity()));
//                        settingDialog.dismiss();
//                        break;
//                    case R.id.tvQrScan:
//                        settingDialog.dismiss();
//                        Intent intent = new Intent(MainActivity.broadcast_tocpatureac);
//                        getActivity().sendBroadcast(intent);
//                        break;
//
//                    case R.id.tvQrCode:
//                        getContext().startActivity(QRCodeCollectionActivity.newInstance(getContext()));
//                        settingDialog.dismiss();
//                        break;
//                }
//            }
//        });
//        settingDialog.showDot(reequestNum);
//        new XPopup.Builder(getCurContext()).atView(ivAddFriend).asCustom(settingDialog).show();
//    }
//
//
//    private void getPendencyList() {
//        ((MainActivity) getCurActivity()).getPendencyList();
//        RepositoryFactory.getChatRepository().getPendencyList(new TIMValueCallBack<TIMFriendPendencyResponse>() {
//            @Override
//            public void onError(int i, String s) {
//                Log.d("getPendencyList", s);
//
//            }
//
//            @Override
//            public void onSuccess(TIMFriendPendencyResponse response) {
//                List<TIMFriendPendencyItem> datas = response.getItems();
//                reequestNum = datas.size() + "";
//                if (viewDot == null) return;
//                if (TextUtils.isEmpty(reequestNum) || TextUtils.equals(reequestNum, "0")) {
//                    viewDot.setVisibility(View.GONE);
//                } else {
//                    viewDot.setVisibility(View.VISIBLE);
//                }
//                viewDot.setText(reequestNum);
//            }
//        });
//    }
//
//    public ImLoginReceiver registImLoginReceiver() {
//
//        ImLoginReceiver imLoginReceiver = new ImLoginReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(ImLoginReceiver.MG_LOGIN_SUCCESS);
//        intentFilter.addAction(ImLoginReceiver.MG_LOGIN_FAILED);
//        imLoginReceiver.setImLogin(new ImLoginReceiver.IMLogin() {
//            @Override
//            public void loginSuccess() {
//                tvNetError.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void loginFailed() {
//                tvNetError.setVisibility(View.VISIBLE);
//            }
//        });
//        getActivity().registerReceiver(imLoginReceiver, intentFilter);
//        return imLoginReceiver;
//    }
//
//    public NetStateReceiver registNetStateReceiver() {
//        NetStateReceiver netStateReceiver = new NetStateReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        netStateReceiver.setOnNetworkStateChangeListener(new NetStateReceiver.OnNetworkStateChangeListener() {
//            @Override
//            public void onStateChange(int curState) {
//                if (curState == NetStateReceiver.STATE_NO_CONNECT) {
//                    tvNetError.setVisibility(View.VISIBLE);
//                } else {
//                    tvNetError.setVisibility(View.GONE);
//                    if (UserCenter.hasImLogin()) return;
//                    UserCenter.imLogin();
//                }
//            }
//        });
//        getActivity().registerReceiver(netStateReceiver, intentFilter);
//        return netStateReceiver;
//    }
//
//    @Override
//    public void onDestroy() {
//        getActivity().unregisterReceiver(imLoginReceiver);
//        getActivity().unregisterReceiver(netStateReceiver);
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this);
//        }
//        super.onDestroy();
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data == null) return;
//        if (resultCode == -1 && requestCode == 1) {
//        }
//    }
//}
//
