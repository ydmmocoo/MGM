package com.fjx.mg.moments.city;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.fjx.mg.R;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.fjx.mg.friend.imuser.NewImUserDetailActivity;
import com.fjx.mg.image.ImageActivity;
import com.fjx.mg.main.fragment.event.UnReadCountEvent;
import com.fjx.mg.moments.CityCache;
import com.fjx.mg.moments.add.NewMomentsActivity;
import com.fjx.mg.moments.detail.MomentsDetailActivity;
import com.fjx.mg.moments.event.RefreshEvent;
import com.fjx.mg.setting.feedback.FeedBackActivity;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.video.VideoActivity;
import com.library.common.base.BaseMvpFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.NetworkUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StringUtil;
import com.library.common.view.popupwindow.CommonPopupWindow;
import com.library.common.view.popupwindow.Utils;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.data.UserCenter;
import com.library.repository.models.CityCircleListModel;
import com.library.repository.models.ImUserRelaM;
import com.library.repository.models.MomentsListBean;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.TypeListModel;
import com.library.repository.models.UnReadCountBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 同城首页
 */
public class CityMomentFragment extends BaseMvpFragment<CityMomentsPresenter> implements CityMomentsContract.View, CommonPopupWindow.ViewInterface {

    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;

    @BindView(R.id.recycler)
    RecyclerView recycler;


    private CityMomentsAdapter cityCircleAdapter;
    private String mId = "";
    private String rId = "";
    private String uId = "";
    private int mPage = 1;

    //    @BindView(R.id.recyclerType)
//    RecyclerView recyclerType;
//    private MomentsTypeAdapter imageAdapter;
    private CommonPopupWindow popWindow;
    private EditText comments;
    private static ViewPager mViewPager;

    public static CityMomentFragment newInstance(ViewPager viewPager) {
        Bundle args = new Bundle();
        CityMomentFragment fragment = new CityMomentFragment();
        fragment.setArguments(args);
        mViewPager = viewPager;
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_city_moment;
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        cityCircleAdapter = new CityMomentsAdapter();
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

        refreshView.setRefreshListener((page, pageSize) -> {
            mPage = page;
            mPresenter.getCityCircleList("" + page);//请求数据
            EventBus.getDefault().post(new UnReadCountEvent("1", "0", true));
        });

        try {
            CityCircleListModel model = CityCache.getInstance().getModel();
            if (model == null) {
                return;
            }
            List<MomentsListBean> momentsList = model.getMomentsList();
            if (momentsList != null && momentsList.size() > 0) {
                cityCircleAdapter.setList(momentsList);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void lazyLoadData() {
        super.lazyLoadData();
        refreshView.autoRefresh();
    }

    @Override
    protected CityMomentsPresenter createPresenter() {
        return new CityMomentsPresenter(this);
    }

    @OnClick(R.id.floatbutton)
    public void onAddClicked() {
        if (UserCenter.hasLogin()) {
            startForResult(NewMomentsActivity.newInstance(getCurContext()), 300);
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
    public void ShowCityCircleList(CityCircleListModel data) {
        refreshView.finishLoading();
        cityCircleAdapter.setDatas(data.getMomentsList());
        if (mPage == 1) {//直接替换数据
            cityCircleAdapter.replaceData(data.getMomentsList());
        } else {//刷新数据
            refreshView.noticeAdapterData(cityCircleAdapter, data.getMomentsList(), data.isHasNext());//考虑实际，不显示无更多数据，hasNext=true
            recycler.invalidateItemDecorations();
        }
    }

    @Override
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
    public void responseFailed(ResponseModel data) {
        refreshView.finishLoading();
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
    public void ShowTypeList(List<TypeListModel.TypeListBean> list) {
//        imageAdapter.setList(list);
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

    /**
     * 点击更多
     *
     * @param Mid
     */
    @Override
    public void toDetail(String Mid) {
        startActivity(MomentsDetailActivity.newInstance(getCurContext(), Mid));
    }

    @Override
    public void isShowRedPoint(UnReadCountBean data) {
        if (StringUtil.equals("0", data.getMomentsRecCount())) {

        } else {
            refreshView.autoRefresh();
        }
    }

    /**
     * 显示评论弹窗
     *
     * @param mid
     * @param rid
     * @param uid
     */
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
            startActivity(ImageActivity.newInstance(getCurActivity(), JsonUtil.moderToString(item), position));
        }
    }

    @Override
    public void ShowCommentDialog(String replyId, String content, boolean showDele) {
        CommentDialog dialog = new CommentDialog(getActivity(), mPresenter, replyId, content, showDele);
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
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300) {
            mViewPager.setCurrentItem(1);
            EventBus.getDefault().post(new RefreshEvent(2));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(RefreshEvent event) {//收到上层指令刷新. 在TabFriendsFragment走接口发现有新的朋友圈会通知这里刷新
        if (3 == event.getFlag()) {
            mPresenter.requestIsShowRedPoint();
        }
    }
}
