package com.fjx.mg.moments.friends;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.fjx.mg.R;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.fjx.mg.friend.imuser.NewImUserDetailActivity;
import com.fjx.mg.image.ImageActivity;
import com.fjx.mg.image.NewImageActivity;
import com.fjx.mg.main.fragment.event.UnReadCountEvent;
import com.fjx.mg.moments.add.NewMomentsActivity;
import com.fjx.mg.moments.all.AllMomentsMessageActivity;
import com.fjx.mg.moments.detail.MomentsDetailActivity;
import com.fjx.mg.moments.event.AllMomentFinish2FriendsMomentEvent;
import com.fjx.mg.moments.event.FriendsTipsEvent;
import com.fjx.mg.moments.event.RefreshEvent;
import com.fjx.mg.setting.feedback.FeedBackActivity;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.video.VideoActivity;
import com.library.common.base.BaseMvpFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.NetworkUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StringUtil;
import com.library.common.view.popupwindow.CommonPopupWindow;
import com.library.common.view.popupwindow.Utils;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.data.UserCenter;
import com.library.repository.db.base.DBHelper;
import com.library.repository.models.CityCircleListModel;
import com.library.repository.models.CityCircleListModelDao;
import com.library.repository.models.ImUserRelaM;
import com.library.repository.models.MomentsListBean;
import com.library.repository.models.MomentsReplyListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UnReadCountBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description：朋友圈
 */
public class FriendsMomentsFragment extends BaseMvpFragment<FriendsMomentsPresenter>
        implements FriendsMomentsContract.View, CommonPopupWindow.ViewInterface{

    private static final int REQUEST_CODE = 233;

    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.flContainer)
    FrameLayout mFlContainer;

    private View mHeaderView;//朋友圈回复点赞(wx朋友圈效果)
    private CopyOnWriteArraySet<View> mHeaderViews = new CopyOnWriteArraySet<>();//复用HeaderView
    private FriendsMomentsAdapter cityCircleAdapter;
    private String mId = "";
    private String rId = "";
    private String uId = "";
    private int mPage = 1;
    private CommonPopupWindow popWindow;
    private EditText comments;

    public static FriendsMomentsFragment newInstance() {
        Bundle args = new Bundle();
        FriendsMomentsFragment fragment = new FriendsMomentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_friends_moments;
    }


    @SuppressLint("WrongConstant")
    public void SetWhoCanWatch() {//全屏弹出增加照片
        if (popWindow != null && popWindow.isShowing()) return;
        View upView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_up_comment, null);
        //测量View的宽高
        Utils.measureWidthAndHeight(upView);
        popWindow = new CommonPopupWindow.Builder(getActivity())
                .setView(R.layout.popup_up_comment)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.AnimUp)
                .setViewOnclickListener(this)
                .create();
        popWindow.showAtLocation(getActivity().findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
        popWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        comments.requestFocus();//弹出键盘
        SoftInputUtil.showSoftInput(getActivity(), comments);
    }

    private void popupInputMethodWindow() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    /**
     * 本地先添加回复数据
     */
    private void addReply() {
        if (cityCircleAdapter != null) {
            List<MomentsListBean> data = cityCircleAdapter.getData();
            for (int i = 0; i < data.size(); i++) {
                MomentsListBean momentsListBean = data.get(i);
                MomentsListBean.MomentsInfoBean momentsInfo = momentsListBean.getMomentsInfo();
                if (TextUtils.equals(mId, momentsInfo.getMId())) {
                    //对该条朋友圈回复
                    MomentsListBean.ReplyListBean replyList = data.get(i).getReplyList();
                    List<MomentsListBean.ListBean> list = replyList.getList();
                    if (list == null && list.size() <= 0) {
                        list = new ArrayList<>();
                    }
                    MomentsListBean.ListBean listBean = new MomentsListBean.ListBean();
                    listBean.setContent(comments.getText().toString());
                    listBean.setToReplyCont("");
                    listBean.setUserId(UserCenter.getUserInfo().getUId());
                    listBean.setToUserIdfer(UserCenter.getUserInfo().getUId());
                    listBean.setUserAvatar(UserCenter.getUserInfo().getUImg());
                    listBean.setUserNickName(UserCenter.getUserInfo().getUNick());
                    listBean.setToUserNick("");
                    list.add(0, listBean);
                    replyList.setList(list);
                    momentsListBean.setReplyList(replyList);
                }
            }
            cityCircleAdapter.setList(data);
        }
    }

    @Override
    public void getChildView(View view, int layoutResId) {

        if (layoutResId == R.layout.popup_up_comment) {
            comments = view.findViewById(R.id.circleEt);
            ImageView popsendIv = view.findViewById(R.id.sendIv);
            popsendIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popWindow != null) {
                        mPresenter.addReplyMid(mId, comments.getText().toString(), uId, rId);
                        popWindow.dismiss();
                        if (StringUtil.isNotEmpty(comments.getText().toString()) && new NetworkUtil().isNetworkConnected(getCurContext()))
                            addReply();
                    }
                }
            });

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        cityCircleAdapter = new FriendsMomentsAdapter();
        cityCircleAdapter.setCirclePresenter(mPresenter);

        recycler.setLayoutManager(new LinearLayoutManager(getCurContext(), LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new SpacesItemDecoration(0, 0));
        ((SimpleItemAnimator) recycler.getItemAnimator()).setSupportsChangeAnimations(false);
        recycler.setAdapter(cityCircleAdapter);

        recycler.setOnTouchListener(new View.OnTouchListener() {//列表触碰收回输入框
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updateEditTextBodyVisible(View.GONE);
                return false;
            }
        });

        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPage = page;
                mPresenter.getCityCircleList("" + page);//请求数据
                EventBus.getDefault().post(new UnReadCountEvent("0", "1", false));
                mPresenter.requestFriendsMomentsReply("1", "", "2");
            }
        });
        CityCircleListModelDao dao = DBHelper.getInstance().getDaoSession().getCityCircleListModelDao();
        List<CityCircleListModel> cityCircleListModels = dao.loadAll();
        if (!cityCircleListModels.isEmpty()) {
            cityCircleAdapter.setList(cityCircleListModels.get(0).getMomentsList());
        } else {
            refreshView.autoRefresh();
        }
    }

    @Override
    protected FriendsMomentsPresenter createPresenter() {
        return new FriendsMomentsPresenter(this);
    }


    @OnClick(R.id.floatbutton)//发布朋友圈
    public void onAddClicked() {
        if (UserCenter.hasLogin()) {
            startActivityForResult(NewMomentsActivity.newInstance(getCurContext()), 200);
        } else {
            new DialogUtil().showAlertDialog(getActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    UserCenter.goLoginActivity();
                }
            });
        }

    }

    @Override
    public void responseFailed(ResponseModel data) {
        refreshView.finishLoading();
    }

    @Override//展示列表
    public void ShowCityCircleList(CityCircleListModel data) {
        refreshView.finishLoading();
        if (data != null && !data.getMomentsList().isEmpty()) {
            if (mPage == 1) {//直接替换数据
                cityCircleAdapter.setList(data.getMomentsList());
            } else {//刷新数据
                refreshView.noticeAdapterData(cityCircleAdapter, data.getMomentsList(), data.isHasNext());//考虑实际，不显示无更多数据，hasNext=true
                recycler.invalidateItemDecorations();
            }
        }

    }

    @Override//点赞成功
    public void PraiseSuccess() {
//        mPage = 1;
//        mPresenter.getCityCircleList("1");
    }


    @Override
    public void updateEditTextBodyVisible(int visibility) {
        if (popWindow != null) {
            popWindow.dismiss();
        }
    }

    @Override
    public void ToUserInfo(String identifier, String userAvatar) {
        mPresenter.findUser(identifier);
    }

    @Override
    public void showUserInfo(ImUserRelaM userRelaM) {
        if (userRelaM.isFriend()) {
            startActivity(NewImUserDetailActivity.newInstance(getCurContext(), userRelaM.getUserProfile().getIdentifier()));
        } else {
            startActivity(AddFriendActivity.newInstance(getCurContext(), JsonUtil.moderToString(userRelaM.getUserProfile())));
        }
    }

    @Override
    public void MomentsDelSuccess() {
        mPage = 1;
        mPresenter.getCityCircleList("1");
    }

    @Override
    public void complaintsUser(String identifier) {
        startActivity(FeedBackActivity.newInstance(getCurContext(), identifier, 3));
    }

    @Override
    public void toDetail(String Mid) {
        startActivity(MomentsDetailActivity.newInstance(getCurContext(), Mid));
    }

    @Override
    public void friendsMomentsReply(MomentsReplyListModel data) {
        initFriendsMomentTips(data);
    }

    @Override
    public void isShowRedPoint(UnReadCountBean data) {
        //当上层点击切换到朋友圈页 eventbus会调此接口如果有红点数据则刷新
        if (StringUtil.equals("0", data.getMomentsFriendCount())) {
            //1.不做任何操作 为0表示没有新的朋友圈数据
            //2.新版本后可以先浏览页面后登录 所以会遇到没登录不请求朋友圈数据 第一次进来页面是空的(没缓存的情况下)所以判断下是否加在过数据 没有就强制刷新下
            try {
                List<MomentsListBean> data1 = cityCircleAdapter.getData();
                if (data1 == null) {
                    data1 = new ArrayList<>();
                }
                if (data1.size() == 0) {
//                    mPresenter.getCityCircleList("1");//请求数据
                    refreshView.autoRefresh();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else {
            refreshView.autoRefresh();
        }
    }

    @Override
    public void BodyVisible(String mid, String rid, String uid) {
        mId = mid;
        rId = rid;
        uId = uid;
        SetWhoCanWatch();
        popupInputMethodWindow();
    }

    @Override
    public void ShowDetail(int position, List<String> item) {
        String url = item.get(0);
        String reg = "(mp4|flv|avi|rm|rmvb|wmv)";
        Pattern p = Pattern.compile(reg);
        if (p.matcher(url).find()) {
//            PictureSelector.create(getActivity()).externalPictureVideo(url);// 预览视频
            startActivity(VideoActivity.newIntent(getActivity(), url));
        } else {// 预览图片 可自定长按保存路径
            startActivity(NewImageActivity.newInstance(getCurActivity(), JsonUtil.moderToString(item), position));
        }
    }

    @Override
    public void ShowCommentDialog(String replyId, String content, boolean showDele) {
        CommentFriendsMomentDialog dialog = new CommentFriendsMomentDialog(getActivity(), mPresenter, replyId, content, showDele);
        dialog.show();
    }

    @Override
    public void delReplySuccess() {
        mPresenter.getCityCircleList("1");
    }

    @Override
    public void GetNewData() {
        mPage = 1;
        mPresenter.getCityCircleList("1");
    }

    @Override
    public void replyFailed() {
        if (cityCircleAdapter != null) {
            List<MomentsListBean> data = cityCircleAdapter.getData();
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    MomentsListBean momentsListBean = data.get(i);
                    MomentsListBean.MomentsInfoBean momentsInfo = momentsListBean.getMomentsInfo();
                    if (TextUtils.equals(mId, momentsInfo.getMId())) {
                        //对该条朋友圈回复
                        MomentsListBean.ReplyListBean replyList = data.get(i).getReplyList();
                        List<MomentsListBean.ListBean> list = replyList.getList();
                        list.remove(0);
                    }
                }
                cityCircleAdapter.setList(data);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            refreshView.autoRefresh();
        }
    }

    /**
     * 朋友圈点赞Tips
     */
    private void initFriendsMomentTips(MomentsReplyListModel data) {
        if (!mHeaderViews.isEmpty()) {
            for (View v : mHeaderViews) {
                mHeaderView = v;
            }
            mHeaderViews.remove(mHeaderView);
        } else {
            mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_friends_moments_reply, null);
        }
        if (mHeaderView != null) {
            ViewGroup parentViewGroup = (ViewGroup) mHeaderView.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeView(mHeaderView);
            }
        }
        mHeaderViews.add(mHeaderView);
        mHeaderView.findViewById(R.id.rlFriendsMomentTips).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllMomentsMessageActivity.launch(getActivity());
                mFlContainer.removeAllViews();
                mFlContainer.setVisibility(View.GONE);
            }
        });
        ImageView ivLeftHeaderIcon = mHeaderView.findViewById(R.id.ivLeftHeaderIcon);//头像
        TextView tvHeaderContent = mHeaderView.findViewById(R.id.tvHeaderContent);//几条未读
        List<MomentsReplyListModel.ReplyListBean> replyList = data.getReplyList();
        try {
            if (!replyList.isEmpty()) {
                CommonImageLoader.load(replyList.get(replyList.size() - 1).getUserAvatar()).noAnim().placeholder(R.drawable.food_default).into(ivLeftHeaderIcon);
                tvHeaderContent.setText(replyList.size() + getString(R.string.new_msg_num));
                mFlContainer.addView(mHeaderView);
                mFlContainer.setVisibility(View.VISIBLE);
            } else {
                mFlContainer.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroy() {
        if (!mHeaderViews.isEmpty()) {
            mHeaderViews.clear();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFriendsTipsEvent(FriendsTipsEvent event) {
        mPresenter.requestFriendsMomentsReply("1", "", "2");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(RefreshEvent event) {//收到上层指令刷新. 在TabFriendsFragment走接口发现有新的朋友圈会通知这里刷新
//        mPresenter.getCityCircleList("1");//请求数据
        if (event.getFlag() == 1) {
            mPresenter.requestIsShowRedPoint(); //当上层点击切换到朋友圈页 eventbus会调此接口如果有红点数据则刷新
        } else if (event.getFlag() == 2) {
            refreshView.autoRefresh();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAllMomentActFinish(AllMomentFinish2FriendsMomentEvent event) {//收到上层指令刷新. 在TabFriendsFragment走接口发现有新的朋友圈会通知这里刷新
//        EventBus.getDefault().post(new UnReadCountEvent("0", "1", true));
//        EventBus.getDefault().post(new ResumeEvent());
        refreshView.autoRefresh();
    }
}
