package com.fjx.mg.house.comment;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.BottomEditPopu;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.ViewUtil;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.data.UserCenter;
import com.library.repository.models.CommentReplyModel;
import com.library.repository.models.NewsCommentModel;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class HouseCommentActivity extends BaseMvpActivity<HouseCommentPresenter> implements HouseCommentContract.View {

    @BindView(R.id.ivUserAvatar)
    ImageView ivUserAvatar;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvFavNum)
    TextView tvFavNum;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.tvCreateDate)
    TextView tvCreateDate;
    @BindView(R.id.tvReplyNum)
    TextView tvReplyNum;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tvReplyCount)
    TextView tvReplyCount;
    @BindView(R.id.tvReply)
    TextView tvReply;
    @BindView(R.id.tvComment)
    TextView tvComment;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;

    private boolean hasEditChange;

    private NewsCommentModel.CommentListBean commentListBean;

    private BasePopupView popupView;
    private String replyUserId, replyId, replyNickName;
    private String commentId;
    private HouseReplyAdapter replyAdapter;


    public static Intent newInstance(Context context, String moddel) {
        Intent intent = new Intent(context, HouseCommentActivity.class);
        intent.putExtra("model", moddel);
        return intent;
    }

    @Override
    protected HouseCommentPresenter createPresenter() {
        return new HouseCommentPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_comment_list;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.reply));
        String model = getIntent().getStringExtra("model");
        commentListBean = JsonUtil.strToModel(model, NewsCommentModel.CommentListBean.class);
        commentId = commentListBean.getComentId();
        showTopicDetail(commentListBean);
        initRefresh();


        tvFavNum.setVisibility(View.GONE);
    }

    private void initRefresh() {
        refreshView.autoRefresh();
        replyAdapter = new HouseReplyAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.setAdapter(replyAdapter);
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getReplyList(commentId, page);
            }
        });

        replyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                replyUserId = replyAdapter.getItem(position).getUserId();
                replyId = replyAdapter.getItem(position).getReplyId();
                replyNickName = replyAdapter.getItem(position).getUserNickName();
                showCommentPop();
            }
        });


        replyAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (UserCenter.hasLogin() && TextUtils.equals(replyAdapter.getItem(position).getUserId(), UserCenter.getUserInfo().getUId()))
                    mPresenter.delete(replyAdapter.getItem(position).getReplyId(), true);
                return true;
            }
        });
    }


    @Override
    public void commentSuccess() {
        //回复评论成功
        hasEditChange = true;
        refreshView.doRefresh();
    }

    @Override
    public void showReplyList(CommentReplyModel data) {
        //显示回复列表数据
        refreshView.noticeAdapterData(replyAdapter, data.getReplyList(), data.isHasNext());
        recycler.invalidateItemDecorations();
        tvReplyNum.setText(getString(R.string.reply).concat("（").concat(data.getReplyNum()).concat(")"));
    }

    @Override
    public void showTopicDetail(NewsCommentModel.CommentListBean model) {
        //显示顶部评论详情
        tvReplyCount.setVisibility(View.GONE);
        GradientDrawableHelper.whit(tvComment).setCornerRadius(50).setColor(R.color.colorGrayBg);
        CommonImageLoader.load(model.getUserAvatar()).circle().placeholder(R.drawable.user_default).into(ivUserAvatar);
        tvUserName.setText(model.getUserNickName());
        tvContent.setText(model.getContent());
        tvCreateDate.setText(model.getCreateTime());
//        tvFavNum.setText(model.getLikeNum());
//        ViewUtil.setDrawableLeft(tvFavNum, model.isLike() ? R.drawable.news_fav1 : R.drawable.news_fav1);
    }


    @Override
    public void deleteReplySuccess() {
        //删除回复成功
        refreshView.autoRefresh();
    }

    @Override
    public void deleteCommentSuccese() {
        //删除评论成功
        hasEditChange = true;
        onBackPressed();

    }


    @OnClick(R.id.tvComment)
    public void clickComment() {
        //点击底部输入框时 清空上一个回复的人的信息，只回复层主
        if (UserCenter.needLogin()) return;
        replyUserId = "";
        replyId = "";
        replyNickName = "";
        showCommentPop();

    }

    private void showCommentPop() {
        BottomEditPopu bottomEditPopu = new BottomEditPopu(getCurContext());
        bottomEditPopu.setNickName(replyNickName);
        bottomEditPopu.setOnReplyListener(new BottomEditPopu.OnReplyListener() {
            @Override
            public void onReply(String content) {
                popupView.dismiss();
                SoftInputUtil.hideSoftInput(HouseCommentActivity.this);
                mPresenter.addReply(commentId, content, replyUserId, replyId);
            }
        });

        popupView = new XPopup.Builder(getCurContext())
                .autoOpenSoftInput(true)
                .asCustom(bottomEditPopu)
                .show();
    }


    @OnLongClick(R.id.llCommentParent)
    public boolean clickDelete() {
        if (UserCenter.hasLogin() && TextUtils.equals(commentListBean.getUserId(), UserCenter.getUserInfo().getUId()))
            mPresenter.delete(commentListBean.getComentId(), false);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (hasEditChange) {
            setResult(1);
        }
        finish();
    }
}
