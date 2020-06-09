package com.fjx.mg.nearbycity;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.dialog.ShareDialog;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.fjx.mg.friend.imuser.NewImUserDetailActivity;
import com.fjx.mg.image.ImageActivity;
import com.fjx.mg.main.payment.detail.AskDetailImageAdapter;
import com.fjx.mg.nearbycity.adapter.NearbyCityVpAdapter;
import com.fjx.mg.nearbycity.event.AddCommentEvent;
import com.fjx.mg.nearbycity.event.CommentEvent;
import com.fjx.mg.nearbycity.event.PraiseListEvent;
import com.fjx.mg.nearbycity.mvp.NearbyCityDetailContract;
import com.fjx.mg.nearbycity.mvp.NearbyCityDetailPresenter;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.widget.viewpager.AutoHeightViewPager;
import com.library.common.base.BaseMvpActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.NetworkUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.common.view.popupwindow.CommonPopupWindow;
import com.library.common.view.popupwindow.Utils;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.models.NearbyCityDetailModel;
import com.library.repository.models.NearbyCityInfoDetailModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.TimConfig;
import com.tencent.qcloud.uikit.business.chat.model.ElemExtModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author    by hanlz
 * Date      on 2019/10/18.
 * Description：同城详情页(适用于我的同城和全部同城)
 */
public class NearbyCityDetailActivity extends BaseMvpActivity<NearbyCityDetailPresenter> implements NearbyCityDetailContract.View, OnRefreshListener {

    private String mCId;
    private int mPage = 1;
    private List<Fragment> fragmentList = new ArrayList<>();

    @BindView(R.id.ivHeaderPic)
    ImageView mIvHeaderPic;
    @BindView(R.id.tvUserName)
    TextView mTvUserName;
    @BindView(R.id.tvCreateTime)
    TextView mTvCreateTime;
    @BindView(R.id.tvContent)
    TextView mTvContent;
    @BindView(R.id.rvDetailsPic)
    RecyclerView mRvDetailsPic;
    @BindView(R.id.tvCommentNum)
    TextView mTvCommentNum;
    @BindView(R.id.tvLikeNum)
    TextView mTvLikeNum;
    @BindView(R.id.tvLookNum)
    TextView mTvLookNum;
    @BindView(R.id.vpContent)
    AutoHeightViewPager mVpContent;
    private NearbyCityVpAdapter mAdapter;
    @BindView(R.id.toolbar_tv_title)
    TextView mTvTitle;
    @BindView(R.id.tvLike)
    TextView mTvLike;
    @BindView(R.id.tvType)
    TextView mTvType;


    private CommonPopupWindow popWindow;
    private EditText comments;
    private String shareUrl, shareTitle, shareDesc, shareImage;
    private boolean mIsLike;

    public static Intent newIntent(Context context, String cId) {
        Intent intent = new Intent(context, NearbyCityDetailActivity.class);
        intent.putExtra(IntentConstants.CID, cId);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_nearby_city_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        if (getIntent() == null) {
            return;
        }
        mTvTitle.setText(getString(R.string.detail));
        mCId = getIntent().getStringExtra(IntentConstants.CID);
        mPresenter.requestDetails(mCId);


        initVpData();
        mAdapter = new NearbyCityVpAdapter(getSupportFragmentManager(), fragmentList);
        mVpContent.setAdapter(mAdapter);
        mVpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    setTextColor(R.color.textColor, R.color.nearby_city_detail_tab_unselected, R.color.nearby_city_detail_tab_unselected);
                } else if (i == 1) {
                    setTextColor(R.color.nearby_city_detail_tab_unselected, R.color.textColor, R.color.nearby_city_detail_tab_unselected);
                }
//                else {
//                    setTextColor(R.color.nearby_city_detail_tab_unselected, R.color.nearby_city_detail_tab_unselected, R.color.textColor);
//
//                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


    }

    private void setTextColor(int commentColor, int likeColor, int lookColor) {
        mTvCommentNum.setTextColor(getResources().getColor(commentColor));
        mTvLikeNum.setTextColor(getResources().getColor(likeColor));
    }

    @OnClick({R.id.tvCommentNum, R.id.tvLikeNum, R.id.toolbar_iv_back, R.id.tvComment, R.id.tvShare, R.id.tvLike, R.id.ivHeaderPic})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tvCommentNum://评论数
                mVpContent.setCurrentItem(0);
                mVpContent.resetHeight(0);
                setTextColor(R.color.textColor, R.color.nearby_city_detail_tab_unselected, R.color.nearby_city_detail_tab_unselected);
                break;
            case R.id.tvLikeNum://点赞数
                mVpContent.resetHeight(1);
                mVpContent.setCurrentItem(1);
                setTextColor(R.color.nearby_city_detail_tab_unselected, R.color.textColor, R.color.nearby_city_detail_tab_unselected);
                break;
            case R.id.toolbar_iv_back://返回
                finish();
                break;
            case R.id.tvComment://评论
                SetWhoCanWatch();
                popupInputMethodWindow();
                break;
            case R.id.tvShare://分享
                if (UserCenter.hasLogin()){
                    showShareDialog();
                }else {
                    new DialogUtil().showAlertDialog(getCurActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserCenter.goLoginActivity();
                        }
                    });
                }

                break;
            case R.id.tvLike://点赞
                if (UserCenter.hasLogin()){
                    if (!mIsLike) {
                        mPresenter.requestPraise("", "", mCId);
                    } else {
                        mPresenter.requestCancelPraise("", "", mCId);
                    }
                }else {
                    new DialogUtil().showAlertDialog(getCurActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserCenter.goLoginActivity();
                        }
                    });
                }

                break;
            case R.id.ivHeaderPic:
                try {
                    String uid = "";
                    if (TimConfig.isRelease) {
                        uid = "MGM".concat(mInfo.getuId());
                    } else {
                        uid = "fjx".concat(mInfo.getuId());
                    }
                    TIMFriend friend = UserCenter.getFriend(uid);
                    if (friend == null) {
                        //非好友
                        view.getContext().startActivity(AddFriendActivity.newInstance(view.getContext(), uid));
                    } else {
                        //好友
                        view.getContext().startActivity(NewImUserDetailActivity.newInstance(view.getContext(), uid));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                break;
            default:
        }

    }

    @SuppressLint("WrongConstant")
    public void SetWhoCanWatch() {//全屏弹出增加照片
        if (popWindow != null && popWindow.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.popup_up_comment, null);
        //测量View的宽高
        Utils.measureWidthAndHeight(upView);
        popWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.popup_up_comment)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.AnimUp)
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        if (layoutResId == R.layout.popup_up_comment) {
                            comments = view.findViewById(R.id.circleEt);
                            ImageView popsendIv = view.findViewById(R.id.sendIv);
                            popsendIv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (popWindow != null) {
                                        popWindow.dismiss();
                                        SoftInputUtil.hideSoftInput(getCurActivity(), comments);
                                        if (UserCenter.hasLogin()) {
                                            mPresenter.addComment(mCId, comments.getText().toString());
                                            if (StringUtil.isNotEmpty(comments.getText().toString()) && new NetworkUtil().isNetworkConnected(getCurContext())) {
                                                EventBus.getDefault().post(new AddCommentEvent(comments.getText().toString(), mCId));
                                            }
                                        }else {
                                            new DialogUtil().showAlertDialog(getCurActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    UserCenter.goLoginActivity();
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }
                    }
                })
                .create();
        popWindow.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
        popWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        comments.requestFocus();//弹出键盘
        SoftInputUtil.showSoftInput(this, comments);
    }

    private void popupInputMethodWindow() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    @Override
    protected NearbyCityDetailPresenter createPresenter() {
        return new NearbyCityDetailPresenter(this);
    }

    private NearbyCityDetailModel mInfo;

    @Override
    public void responseDetails(NearbyCityInfoDetailModel data) {
        final NearbyCityDetailModel info = data.getInfo();
        mInfo = info;
        if (info != null) {
            mPresenter.requestUpdateReadNum(info.getcId());
            if (StringUtil.isNotEmpty(info.getuAvatar())) {
                CommonImageLoader.load(info.getuAvatar()).placeholder(R.drawable.food_default).into(mIvHeaderPic);
            }
            mTvCreateTime.setText(info.getCreateTime());
            mTvUserName.setText(info.getuNick());
            String content = "";
            if (info.getContent().contains("\n")) {
                content = info.getContent();
            } else {
                content = info.getContent().replaceAll("\r", "\n");
            }
            mTvContent.setText(content);
            AskDetailImageAdapter imageAdapter = new AskDetailImageAdapter();//2.图片或视频列表
            imageAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    startActivity(ImageActivity.newInstance(view.getContext(), JsonUtil.moderToString(info.getImages()), position));
                }
            });
            mRvDetailsPic.setAdapter(imageAdapter);
            if (!info.getImages().isEmpty() && info.getImages().size() > 0 && info.getImages() != null) {
                imageAdapter.setNewInstance(info.getImages());
            }
            mTvCommentNum.setText(getString(R.string.comment).concat(" ") + info.getCommentNum());
            mTvLikeNum.setText(getString(R.string.praise).concat(" ") + info.getLikeNum());
            mTvLookNum.setText(getString(R.string.browse).concat(" ") + info.getReadNum());
            mIsLike = info.isLike();
            if (mIsLike) {
                ViewUtil.setDrawableLeft(mTvLike, R.drawable.like_red);
            } else {
                ViewUtil.setDrawableLeft(mTvLike, R.drawable.like_gray);
            }
            mTvType.setText(info.getTypeName());
        }
    }

    @Override
    public void responseFailed(ResponseModel model) {

    }


    @Override
    public void addCommentSuccess() {
        EventBus.getDefault().post(new CommentEvent(mCId));
    }

    @Override
    public void addCommentFailed() {
        EventBus.getDefault().post(new CommentEvent(mCId));
    }

    @Override
    public void responsePraise() {
//        mVpContent.setCurrentItem(1);
        mPresenter.requestDetails(mCId);
        CommonToast.toast(getString(R.string.praise_success));
        EventBus.getDefault().post(new PraiseListEvent("", "", mCId));
    }

    @Override
    public void responseCancelPraise() {
//        mVpContent.setCurrentItem(1);
        CommonToast.toast(getString(R.string.cancelPraise));
        mPresenter.requestDetails(mCId);
        EventBus.getDefault().post(new PraiseListEvent("", "", mCId));
    }


    private void initVpData() {
        fragmentList.add(NearbyCityCommentListFragment.newFragment(mCId, mVpContent));
        fragmentList.add(NearbyCityPariseFragment.newFragment(mVpContent, mCId));
    }

    private void showShareDialog() {
        String inviteCode = UserCenter.getUserInfo().getInviteCode();
        shareUrl = Constant.HOST.concat("invite/cityDetails?id=").concat(mCId).concat("&iv=").concat(inviteCode) + "&l=" + RepositoryFactory.getLocalRepository().getLangugeType();
        shareTitle = mInfo.getTypeName();
        shareDesc = mInfo.getContent();
        if (mInfo.getImages() != null && !mInfo.getImages().isEmpty())
            shareImage = mInfo.getImages().get(0);

        ShareDialog shareDialog = new ShareDialog(getCurContext());
        shareDialog.setShareParams(shareTitle, shareDesc, shareImage, shareUrl, mCId, ElemExtModel.SHARE_CITY, mInfo.getContent(), mInfo.getTypeName(), (ArrayList<String>) mInfo.getImages());
        shareDialog.setShareType(ShareDialog.ShareType.web);
        new XPopup.Builder(getCurContext())
                .asCustom(shareDialog)
                .show();
    }

    @Override
    public void onRefresh(String mCId) {
        mPresenter.requestDetails(mCId);
    }
}
