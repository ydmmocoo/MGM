package com.fjx.mg.main.fragment.friendtab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fjx.mg.R;
import com.fjx.mg.dialog.FriendSettingDialogx;
import com.fjx.mg.friend.addfriend.SearchFriendActivity;
import com.fjx.mg.friend.chat.StartGroupChatActivity;
import com.fjx.mg.friend.contacts.FriendContactsActivity;
import com.fjx.mg.friend.nearby.NearbyActivity;
import com.fjx.mg.friend.search.FriendSearchActivity;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.main.event.RedPointEvent;
import com.fjx.mg.main.fragment.event.ImLoginEvent;
import com.fjx.mg.main.fragment.event.ResumeEvent;
import com.fjx.mg.main.fragment.event.UnReadCountEvent;
import com.fjx.mg.main.impl.OnCountListener;
import com.fjx.mg.main.scan.paymentcode.PaymentCodeActivity;
import com.fjx.mg.moments.event.FriendsTipsEvent;
import com.fjx.mg.moments.event.RefreshEvent;
import com.library.common.utils.MulLanguageUtil;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.repository.models.MomentsReplyListModel;
import com.library.repository.models.UnReadCountBean;
import com.fjx.mg.main.fragment.friendnew.NewFriendFragment;
import com.fjx.mg.main.scan.QRCodeCollectionActivity;
import com.fjx.mg.moments.city.CityMomentFragment;
import com.fjx.mg.moments.friends.FriendsMomentsFragment;
import com.fjx.mg.utils.DateFormateUtil;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.widget.MsgView;
import com.library.common.base.BaseMvpFragment;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.IMNoticeListModel;
import com.library.repository.models.IMNoticeModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriendPendencyItem;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;
import com.tencent.qcloud.uikit.operation.group.GroupChatCreateActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static java.util.Locale.SIMPLIFIED_CHINESE;

/**
 * 朋友首页
 */
public class TabFriendFragment extends BaseMvpFragment<TabFriendPresenter> implements TabFriendContract.View {

    @BindView(R.id.tabs)
    SlidingTabLayout slidingTabLayout;
    //    PagerSlidingTabStrip slidingTabLayout;
//    private String[] TITLES = {"同城", "朋友圈", "聊天"};
    private String[] TITLES;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.ivAddFriend)
    ImageView ivAddFriend;
    @BindView(R.id.viewDot)
    TextView viewDot;


    private FriendSettingDialogx settingDialog;
    private String reequestNum;
    private OnCountListener mListener;

    private IMNoticeListModel noticeListModel;
    private IMNoticeListModel sysNoticeListModel;

    public static TabFriendFragment newInstance() {
        Bundle args = new Bundle();
        TabFriendFragment fragment = new TabFriendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected TabFriendPresenter createPresenter() {
        return new TabFriendPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_tab_friend;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnCountListener) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String[] TITLES = {getString(R.string.chat), getString(R.string.friend_circle), getString(R.string.recommend)};
        this.TITLES = TITLES;
        EventBus.getDefault().register(this);
        FragmentManager fm = getChildFragmentManager();
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(fm);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    EventBus.getDefault().post(new RefreshEvent(1));
                } else if (position == 2) {
                    EventBus.getDefault().post(new RefreshEvent(3));
                } else if (position == 0) {
                    EventBus.getDefault().post(new ImLoginEvent());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        slidingTabLayout.setViewPager(viewPager, TITLES);
        viewPager.setCurrentItem(0);
//        for (int i = 0; i < slidingTabLayout.getTabCount(); i++) {
////            TextView titleView = slidingTabLayout.getTitleView(i);
////            titleView.setMaxEms(4);
////            titleView.setMaxLines(1);
////        }
    }



    public void showMsg(int position, int leftpadding, int bootomPadding) {
        slidingTabLayout.showMsg(position, 0);
        slidingTabLayout.setMsgMargin(position, leftpadding, bootomPadding);
        MsgView msgView = slidingTabLayout.getMsgView(position);
        ViewGroup.LayoutParams layoutParams = msgView.getLayoutParams();
        layoutParams.width = 18;
        layoutParams.height = 18;
        msgView.setLayoutParams(layoutParams);
    }

    public void showMsg(int position) {
        Locale locale = MulLanguageUtil.getLocalLanguage();
        if (locale.equals(SIMPLIFIED_CHINESE)) {
            this.showMsg(position, 40, 5);
        } else if (locale.equals(Locale.TRADITIONAL_CHINESE)) {
            this.showMsg(position, 40, 5);
        } else if (locale.equals(Locale.FRENCH)) {
            if (position == 2) {
                this.showMsg(position, 15, 5);
            } else if (position==0){
                this.showMsg(position, 40, 5);
            }else {
                this.showMsg(position, 12, 5);
            }
        } else if (locale.equals(Locale.ENGLISH)) {
            if (position == 2) {
                this.showMsg(position, 25, 5);
            } else if (position==0){
                this.showMsg(position, 40, 5);
            } else {
                this.showMsg(position, 40, 5);
            }
        }

    }

    private void hideMsg(int position) {
        slidingTabLayout.hideMsg(position);
    }

    @Override
    public void isShowRedPoint(UnReadCountBean bean) {

        String momentsFriendCount = bean.getMomentsFriendCount();
        String momentsRecCount = bean.getMomentsRecCount();
        mPresenter.requestFriendsMomentsReply("1", "", "2", momentsFriendCount);
//        if (StringUtil.equals("0", momentsFriendCount)) {//TODO 新增小红点外的评论提示个数 此处不管是否有朋友发布朋友圈也要走此接口
////            hideMsg(1);
//            mPresenter.requestFriendsMomentsReply("1", "", "2", momentsFriendCount);
//        } else {
////            showMsg(1);
//            mPresenter.requestFriendsMomentsReply("1", "", "2", momentsFriendCount);
////            EventBus.getDefault().post(new RefreshEvent());//有新消息通知FriendsMomentFragment刷新内容
//        }

        if (StringUtil.equals("0", momentsRecCount)) {
            hideMsg(2);
        } else {
            showMsg(2);
        }
    }

    @Override
    public void friendsMomentsReply(MomentsReplyListModel data, String momentsFriendCount) {
        try {
            if (data.getReplyList().size() > 0 && data.getReplyList() != null) {
                slidingTabLayout.showMsg(1, data.getReplyList().size());
                Locale locale = MulLanguageUtil.getLocalLanguage();
                if (locale.equals(SIMPLIFIED_CHINESE)) {
                    slidingTabLayout.setMsgMargin(1, 35, 10);
                } else if (locale.equals(Locale.TRADITIONAL_CHINESE)) {
                    slidingTabLayout.setMsgMargin(1, 35, 10);
                } else if (locale.equals(Locale.FRENCH)) {
                    slidingTabLayout.setMsgMargin(1, 15, 3);
                } else if (locale.equals(Locale.ENGLISH)) {
                    slidingTabLayout.setMsgMargin(1, 35, 3);
                }
                MsgView msgView = slidingTabLayout.getMsgView(1);
                ViewGroup.LayoutParams layoutParams = msgView.getLayoutParams();
                layoutParams.width = 46;
                layoutParams.height = 46;
                msgView.setLayoutParams(layoutParams);
                EventBus.getDefault().post(new FriendsTipsEvent());
                mListener.onCount(data.getReplyList().size());
            } else {
                if (StringUtil.equals("0", momentsFriendCount)) {
                    hideMsg(1);
                } else {
                    showMsg(1);
                }
            }
        } catch (NullPointerException e) {
            if (StringUtil.equals("0", momentsFriendCount)) {
                hideMsg(1);
            } else {
                showMsg(1);
            }
        }
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return NewFriendFragment.newInstance();//聊天
                case 1:
                    return FriendsMomentsFragment.newInstance();//朋友圈
                case 2:
                    return CityMomentFragment.newInstance(viewPager);//同城
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
        }
    }

    @OnClick({R.id.ivSearch, R.id.ivAddFriend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivSearch:
                startActivity(FriendSearchActivity.newInstance(getCurContext()));
                break;
            case R.id.ivAddFriend:
                showSettingDialog();
                break;
        }

    }

    private void showSettingDialog() {
        settingDialog = new FriendSettingDialogx(getCurActivity(), new FriendSettingDialogx.FriendSettingDialogxClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tvCreateGroupChat://发起群聊
                        startActivity(new Intent(getContext(), StartGroupChatActivity.class));
                        settingDialog.dismiss();
                        break;
                    case R.id.tvAddFriend://添加好友
                        getContext().startActivity(SearchFriendActivity.newInstance(getContext(),true));
                        settingDialog.dismiss();
                        break;
                    case R.id.tvContact://通讯录
                        getContext().startActivity(FriendContactsActivity.newInstance(getContext(), false));
                        settingDialog.dismiss();
                        break;
                    case R.id.nearAddFriend://附近的人
                        getContext().startActivity(NearbyActivity.newInstance(getActivity()));
                        settingDialog.dismiss();
                        break;
                    case R.id.tvQrScan://扫一扫
                        settingDialog.dismiss();
                        Intent intent = new Intent(MainActivity.broadcast_tocpatureac);
                        getActivity().sendBroadcast(intent);
                        break;
                    case R.id.tvQrCode://收款码
                        getContext().startActivity(QRCodeCollectionActivity.newInstance(getContext()));
                        settingDialog.dismiss();
                        break;
                    case R.id.tvBarCode://付款码
                        getContext().startActivity(PaymentCodeActivity.newIntent(getContext()));
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
//                    viewDot.setVisibility(View.GONE);
                    ViewUtil.setDrawableBackGround(viewDot, R.drawable.solid_red_d1_shape);

                } else {
                    ViewUtil.setDrawableBackGround(viewDot, R.drawable.solid_nomal_shape);
//                    viewDot.setVisibility(View.VISIBLE);
                }
                viewDot.setText(reequestNum);
            }
        });
    }

    @Override
    public void doLoadVisible() {
        getNotice();
        getPendencyList();
    }

    private void getNotice() {
        Log.d("getNotice", "getNotice: ");
        RepositoryFactory.getRemotePayRepository().payNoticeList(1, 2)
                .compose(RxScheduler.<ResponseModel<IMNoticeListModel>>toMain())
                .as(this.<ResponseModel<IMNoticeListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<IMNoticeListModel>() {
                    @Override
                    public void onSuccess(IMNoticeListModel data) {
                        getSystemNotice();
                        noticeListModel = data;
                        try {
                            if (data == null) return;
                            if (data.getRecordList().isEmpty()) return;
                            IMNoticeModel model = data.getRecordList().get(0);

                            //2019-06-05 17:03:31
                            String createDate = model.getCreateTime();
                            String lastDate = RepositoryFactory.getLocalRepository().getLaseImNoticeDate();
                            if (DateFormateUtil.getTime(createDate) - DateFormateUtil.getTime(lastDate) > 0) {
                                //有新消息
                                RepositoryFactory.getLocalRepository().saveLastIMNoticeDate(createDate);
                            } else {

                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEventMain(RedPointEvent event) {
        if (event.getPoint() == 0) {
            hideMsg(0);
        } else {
            showMsg(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUnReadCount(UnReadCountEvent event) {
        if (StringUtil.equals("0", event.getMomentsFriendCount())) {
            if (!event.isShow()) {
                if (slidingTabLayout != null) {
                    MsgView msgView = slidingTabLayout.getMsgView(1);
                    if (msgView != null) {
                        if (StringUtil.isNotEmpty(msgView.getText().toString())) {
                            mPresenter.requestIsShowRedPoint();
                        } else {
                            hideMsg(1);
                        }
                    } else {
                        hideMsg(1);
                    }
                } else {
                    hideMsg(1);
                }
            } else {
                mPresenter.requestIsShowRedPoint();
            }

        } else if (StringUtil.equals("0", event.getMomentsRecCount())) {
            hideMsg(2);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventResume(ResumeEvent event) {//当MAinActivity点击tab选中朋友时接收到eventbus通知去请求是否有未读朋友圈数据
        mPresenter.requestIsShowRedPoint();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
