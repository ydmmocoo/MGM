package com.fjx.mg.moments.detail;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.addfriend.AddFriendActivity;
import com.fjx.mg.friend.imuser.NewImUserDetailActivity;
import com.fjx.mg.image.ImageActivity;
import com.fjx.mg.main.payment.detail.AskDetailImageAdapter;
import com.fjx.mg.main.yellowpage.detail.YellowPageDetailActivity;
import com.fjx.mg.moments.city.CityMomentsTypeAdapter;
import com.fjx.mg.moments.city.CityMomentsTypeAdapterx;
import com.fjx.mg.moments.city.DetailCommentListView;
import com.fjx.mg.moments.city.DetailPraiseListView;
import com.fjx.mg.nearbycity.NearbyCityDetailActivity;
import com.fjx.mg.news.detail.NewsDetailActivity;
import com.fjx.mg.setting.feedback.FeedBackActivity;
import com.fjx.mg.view.RoundImageView;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.constant.IntentConstants;
import com.library.common.pop.ActionItem;
import com.library.common.pop.SnsPopupWindow;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.ContextManager;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StatusBarManager;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.common.view.popupwindow.CommonPopupWindow;
import com.library.common.view.popupwindow.Utils;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.data.UserCenter;
import com.library.repository.models.ImUserRelaM;
import com.library.repository.models.MomentsInfoModel;
import com.library.repository.models.MomentsReplyListModel;
import com.library.repository.models.PersonalMomentListModel;
import com.luck.picture.lib.PictureSelector;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 同城评论详情页
 */
public class MomentsDetailActivity extends BaseMvpActivity<MomentsDetailPresenter> implements MomentsDetailContract.View, CommonPopupWindow.ViewInterface {

    @BindView(R.id.praiseListView)
    DetailPraiseListView praiseListView;

    @BindView(R.id.commentList)
    DetailCommentListView commentList;

    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    @BindView(R.id.imgPraise)
    ImageView imgPraise;

    @BindView(R.id.imgComment)
    ImageView imgComment;
    @BindView(R.id.ivUserAvatar)
    RoundImageView ivImage;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvcreateTime)
    TextView tvcreateTime;
    @BindView(R.id.tvSex)
    TextView ImgSex;
    @BindView(R.id.recyclerType)
    RecyclerView recyclertype;
    @BindView(R.id.tvContent)
    TextView tvContent;
//    @BindView(R.id.recyclerType1)
//    RecyclerView recyclertype1;

    @BindView(R.id.recyclerType2)
    RecyclerView recyclerType2;
    @BindView(R.id.tvAdress)
    TextView tvAdress;
    @BindView(R.id.tvKm)
    TextView tvKm;
    @BindView(R.id.imgMore)
    ImageView imgMore;
    @BindView(R.id.llShareContainer)
    LinearLayout mLlShareContainer;
    @BindView(R.id.ivPic)
    ImageView mIvPic;
    @BindView(R.id.tvContent2)
    TextView mTvContent2;

    private String mid;
    private SnsPopupWindow snsPopupWindow;
    private CityMomentsTypeAdapter typeAdapter;
    //    private CityMomentsTypeAdapterx typeAdapter1;
    private AskDetailImageAdapter imageAdapter;
    private MomentsInfoModel.MomentsInfoBean momentsInfo;
    private Boolean isLike = false;
    private int mPage = 1;
    private String replyId = "";
    private String toUid = "";
    private String userIdfer;
    private CommonPopupWindow popWindow;
    private EditText comments;

    public static Intent newInstance(Context context, String mid) {
        Intent intent = new Intent(context, MomentsDetailActivity.class);
        intent.putExtra(IntentConstants.MID, mid);
        return intent;
    }

    public static Intent newInstance(Context context, String mid, String ext) {
        Intent intent = new Intent(context, MomentsDetailActivity.class);
        intent.putExtra(IntentConstants.MID, mid);
        intent.putExtra(IntentConstants.EXT, ext);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.activity_moments_detail;
    }

    @SuppressLint("WrongConstant")
    public void SetWhoCanWatch() {//全屏弹出增加照片
        if (popWindow != null && popWindow.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.popup_up_comment1, null);
        //测量View的宽高
        Utils.measureWidthAndHeight(upView);
        popWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.popup_up_comment1)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.AnimUp)
                .setViewOnclickListener(this)
                .create();
        popWindow.showAtLocation(this.findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
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
    protected MomentsDetailPresenter createPresenter() {
        return new MomentsDetailPresenter(this);
    }

    @Override
    protected void initView() {
        mid = getIntent().getStringExtra(IntentConstants.MID);
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        ToolBarManager.with(this).setTitle("详情");

        typeAdapter = new CityMomentsTypeAdapter();//标签列表
        recyclertype.setLayoutManager(new LinearLayoutManager(ContextManager.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclertype.setAdapter(typeAdapter);

//        typeAdapter1 = new CityMomentsTypeAdapterx();//分类列表
//        recyclertype1.setLayoutManager(new LinearLayoutManager(ContextManager.getContext(), LinearLayoutManager.HORIZONTAL, false));
//        recyclertype1.setAdapter(typeAdapter1);


        imageAdapter = new AskDetailImageAdapter();//图片列表
        recyclerType2.setLayoutManager(new GridLayoutManager(ContextManager.getContext(), 3));
        recyclerType2.addItemDecoration(new SpacesItemDecoration(0, 0));
        ((SimpleItemAnimator) recyclerType2.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerType2.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                String url = imageAdapter.getData().get(0);
                String reg = "(mp4|flv|avi|rm|rmvb|wmv)";
                Pattern p = Pattern.compile(reg);
                if (p.matcher(url).find()) {
                    PictureSelector.create(MomentsDetailActivity.this).externalPictureVideo(url);// 预览视频
                } else {// 预览图片 可自定长按保存路径
                    startActivity(ImageActivity.newInstance(getCurActivity(), JsonUtil.moderToString(imageAdapter.getData()), position));
                }
            }
        });


        praiseListView.setOnItemClickListener(new DetailPraiseListView.OnItemClickListener() {//点赞列表
            @Override
            public void onClick(int position) {
                mPresenter.findUser(praiseListView.getDatas().get(position).getUserIdfer());
            }
        });
        commentList.setDatas(replyList);
        commentList.setOnItemClickListener(new DetailCommentListView.OnItemClickListener() {
            @Override
            public void onItemClick(int commentPosition) {
                MomentsReplyListModel.ReplyListBean commentItem = commentList.getDatas().get(commentPosition);
                if (commentItem.getUserIdfer().equals(UserCenter.getUserInfo().getIdentifier())) {//弹框删除或复制
                    ShowCommentDialog(commentItem.getReplyId(), commentItem.getContent(), true);
                } else {//回复别人的评论
                    replyId = commentItem.getReplyId();
                    toUid = commentItem.getUserId();
                    SetWhoCanWatch();
                    popupInputMethodWindow();
                }
            }
        });
        commentList.setOnItemLongClickListener(new DetailCommentListView.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int commentPosition) {
                MomentsReplyListModel.ReplyListBean commentItem = commentList.getDatas().get(commentPosition);
                if (commentItem.getUserIdfer().equals(UserCenter.getUserInfo().getIdentifier())) {//弹框删除或复制
                    ShowCommentDialog(commentItem.getReplyId(), commentItem.getContent(), true);
                } else {//回复别人的评论
                    ShowCommentDialog(commentItem.getReplyId(), commentItem.getContent(), userIdfer.equals(UserCenter.getUserInfo().getIdentifier()));
                }

            }
        });
        commentList.setOnItemSpannableClickListener(new DetailCommentListView.OnItemSpannableClickListener() {//跳往个人朋友圈页面
            @Override
            public void onItemSpannableClick(String userIdfer, String userAvatar) {
                mPresenter.findUser(userIdfer);
            }
        });


        refreshView.autoRefresh();
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPage = page;
                mPresenter.MomentsInfo(mid, "", "", page + "");
            }
        });
        mPresenter.MomentsInfo(mid, "", "", "1");

        initShare();
    }

    private void initShare() {
        if (getIntent() != null) {
            String ext = getIntent().getStringExtra(IntentConstants.EXT);
            if (StringUtil.isNotEmpty(ext)) {
                PersonalMomentListModel.MomentsListBean momentsListBean = JsonUtil.strToModel(ext, PersonalMomentListModel.MomentsListBean.class);
                final PersonalMomentListModel.ShareInfoBean shareInfo = momentsListBean.getShareInfo();
                if (TextUtils.equals("0",shareInfo.getShareId())||TextUtils.equals("0",shareInfo.getShareType())){
                    mLlShareContainer.setVisibility(View.GONE);
                }else {
                    mLlShareContainer.setVisibility(View.VISIBLE);
                }
                if (StringUtil.isNotEmpty(shareInfo.getImg())) {
                    CommonImageLoader.load(shareInfo.getImg()).into(mIvPic);
                }
                try {
                    if (StringUtil.isNotEmpty(shareInfo.getTypeName())) {
                        mTvContent2.setText(shareInfo.getTypeName().concat(shareInfo.getDesc()));
                    } else {
                        mTvContent2.setText(shareInfo.getTitle());
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                mLlShareContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (StringUtil.equals("3", shareInfo.getShareType())) {
                            view.getContext().startActivity(NearbyCityDetailActivity.newIntent(view.getContext(), shareInfo.getShareId()));
                        } else if (StringUtil.equals("2", shareInfo.getShareType())) {
                            view.getContext().startActivity(NewsDetailActivity.newInstance(view.getContext(), shareInfo.getShareId()));
                        } else if (StringUtil.equals("1", shareInfo.getShareType())) {
                            view.getContext().startActivity(YellowPageDetailActivity.newInstance(view.getContext(),
                                    shareInfo.getShareId(), "",false,"-1"));
                        }
                    }
                });
            } else {
                mLlShareContainer.setVisibility(View.GONE);
            }
        }
    }

    public void ShowCommentDialog(String replyId, String content, boolean showDele) {
        DetailMomentsCommentDialog dialog = new DetailMomentsCommentDialog(MomentsDetailActivity.this, mPresenter, replyId, content, showDele);
        dialog.show();
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
    public void getChildView(View view, int layoutResId) {

        if (layoutResId == R.layout.popup_up_comment1) {
            comments = view.findViewById(R.id.circleEt);
            view.findViewById(R.id.sendIv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popWindow != null) {
                        mPresenter.addReplyMid(mid, comments.getText().toString(), toUid, replyId);
                        popWindow.dismiss();
                    }
                }
            });

        }
    }

    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener {
        private MomentsInfoModel item;
        private long mLasttime = 0;

        PopupItemClickListener(MomentsInfoModel item) {
            this.item = item;
        }

        @Override
        public void onItemClick(ActionItem actionitem, int position) {
            switch (position) {
                case 0://点赞、取消点赞
                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
                    if (isLike) {
                        mPresenter.CancelPraise(momentsInfo.getMId());//取消点赞
                    } else {
                        mPresenter.Praise(momentsInfo.getMId());//点赞
                    }
                    break;
                case 1://发布评论
                    SetWhoCanWatch();
                    popupInputMethodWindow();
                    break;
                case 2:

                    if (item.getUserInfo().getUserIdfer().equals(UserCenter.getUserInfo().getIdentifier())) {
                        new XPopup.Builder(getCurContext()).asConfirm(getCurContext().getString(R.string.Tips),
                                getCurContext().getString(R.string.delete_confirm_message), new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        mPresenter.MomentsDel(mid);
                                    }
                                }).show();
                    } else {
                        mPresenter.complaintsUser(item.getUserInfo().getUserIdfer());
                    }

                    break;
                default:
                    break;
            }
        }


    }


    @Override
    public void showInfo(MomentsInfoModel item) {
        refreshView.finishLoading();
        momentsInfo = item.getMomentsInfo();
        userIdfer = item.getUserInfo().getUserIdfer();
        List<MomentsInfoModel.PraiseListBean> praiseList = item.getPraiseList();//点赞列表数据

        tvUserName.setText(item.getUserInfo().getUserNickName());//发布用户
        tvcreateTime.setText(momentsInfo.getCreateTime());//发布时间
        tvContent.setText(momentsInfo.getContent());//发布内容
        tvAdress.setText(momentsInfo.getAddress());//发布地址
        tvKm.setText(momentsInfo.getDistanceUnit());//距离km
        CommonImageLoader.load(item.getUserInfo().getUserAvatar()).noAnim().placeholder(R.drawable.default_head).into(ivImage);//头像
        ViewUtil.setDrawableLeft(ImgSex, item.getUserInfo().getSex().equals("1") ? R.drawable.boy_blue : R.drawable.girl_pink);//性别
        ViewUtil.setDrawableBackGround(ImgSex, item.getUserInfo().getSex().equals("1") ? R.drawable.solid_stroke_city_circle_blue1 : R.drawable.solid_stroke_city_circle_red);

        if (praiseList.size() > 0) {//点赞列表最后一位不加 , 号
            praiseList.get(praiseList.size() - 1).setLast(true);
        }
        for (int i = 0; i < praiseList.size(); i++) {//是否有自己点赞
            if (praiseList.get(i).getUserIdfer().contains(UserCenter.getUserInfo().getUId())) {
                CommonImageLoader.load(R.drawable.like_red).noAnim().placeholder(R.drawable.like_red).into(imgPraise);
                isLike = true;
                break;
            } else {
                CommonImageLoader.load(R.drawable.like_gray).noAnim().placeholder(R.drawable.like_gray).into(imgPraise);
                isLike = false;
            }
        }
        if (praiseList.size() == 0) {
            CommonImageLoader.load(R.drawable.like_gray).noAnim().placeholder(R.drawable.like_gray).into(imgPraise);
            isLike = false;
        }

        tvAdress.setVisibility(momentsInfo.getAddress().equals("") ? View.INVISIBLE : View.VISIBLE);//地址显隐
//        tvKm.setVisibility(momentsInfo.getDistanceUnit().startsWith("km") ? View.INVISIBLE : View.VISIBLE);//距离显隐


        List<String> tags = item.getUserInfo().getTags();
        Collections.shuffle(tags);
        typeAdapter.setList(tags.size() > 3 ? tags.subList(0, 3) : tags);//标签列表数据
//        typeAdapter1.setList(item.getTypeList());//分类列表数据
        imageAdapter.setList(momentsInfo.getUrls());//图片列表数据

        snsPopupWindow = new SnsPopupWindow(ContextManager.getContext(), item.getUserInfo().getUserIdfer().equals(UserCenter.getUserInfo().getIdentifier()));
        snsPopupWindow.setmItemClickListener(new PopupItemClickListener(item));


        praiseListView.setDatas(praiseList);
        praiseListView.setVisibility(praiseList.size() > 0 ? View.VISIBLE : View.GONE);//点赞列表显隐

    }

    private List<MomentsReplyListModel.ReplyListBean> replyList = new ArrayList<>();

    @Override
    public void showReplyList(MomentsReplyListModel model) {
        if (mPage == 1) {
            replyList.clear();
        }
        replyList.addAll(model.getReplyList());
        commentList.setVisibility(replyList.size() > 0 ? View.VISIBLE : View.GONE);
        commentList.notifyDataSetChanged();
    }


    @Override
    public void GetNewData() {
        if (popWindow != null) {
            popWindow.dismiss();
        }
        mPage = 1;
        mPresenter.MomentsInfo(mid, "", "", "1");
    }

    @Override
    public void complaintsUser(String identifier) {
        startActivity(FeedBackActivity.newInstance(getCurContext(), identifier, 3));
    }

    @Override
    public void MomentsDelSuccess() {
        finish();
    }

    @Override
    public void loadError() {
        refreshView.finishLoading();
    }

    @OnClick({R.id.imgPraise, R.id.imgComment, R.id.imgMore})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgPraise:
                if (isLike) {
                    mPresenter.CancelPraise(momentsInfo.getMId());//取消点赞
                } else {
                    mPresenter.Praise(momentsInfo.getMId());//点赞
                }
                break;
            case R.id.imgComment://评论
                SetWhoCanWatch();
                popupInputMethodWindow();
                break;
            case R.id.imgMore://更多
                snsPopupWindow.showPopupWindow(view);
                break;
        }
    }


}
