package com.fjx.mg.moments.city.type;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.fjx.mg.main.payment.detail.AskDetailImageAdapter;
import com.fjx.mg.moments.city.CityMomentsTypeAdapter;
import com.fjx.mg.moments.city.CityMomentsTypeAdapterx;
import com.fjx.mg.moments.city.CommentListView;
import com.fjx.mg.moments.city.PraiseListView;
import com.fjx.mg.view.RoundImageView;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.pop.ActionItem;
import com.library.common.pop.SnsPopupWindow;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.ContextManager;
import com.library.common.utils.ViewUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.CityCircleListModel;
import com.library.repository.models.MomentsListBean;

import java.util.Collections;
import java.util.List;

public class TypeMomentsAdapter extends BaseAdapter<MomentsListBean> {

    private boolean isPraised = false;
    private CityMomentsTypePresenter mPresenter;
    private SnsPopupWindow snsPopupWindow;

    void setCirclePresenter(CityMomentsTypePresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    public TypeMomentsAdapter() {
        super(R.layout.item_city_circle);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MomentsListBean item) {


        MomentsListBean.MomentsInfoBean momentsInfo = item.getMomentsInfo();
        final List<MomentsListBean.PraiseListBean> praiseList = item.getPraiseList();
        final List<MomentsListBean.ListBean> list = item.getReplyList().getList();
        List<String> typeList = item.getTypeList();
        View circle3 = helper.getView(R.id.circle3);//点赞显隐
        View circle4 = helper.getView(R.id.circle4);//评论显隐
        View llCommentParent = helper.getView(R.id.llCommentParentimg);//评论显隐
        circle3.setVisibility(praiseList.size() > 0 ? View.VISIBLE : View.GONE);
        circle4.setVisibility(list.size() > 0 ? View.VISIBLE : View.GONE);
        llCommentParent.setVisibility(momentsInfo.getUrls().size() > 0 ? View.VISIBLE : View.GONE);

        if (praiseList.size() > 0) {//最后一个点赞不加  ,   号
            praiseList.get(praiseList.size() - 1).setLast(true);
        }

        ImageView imgPraise = helper.getView(R.id.imgPraise);//是否已点赞
        for (int i = 0; i < praiseList.size(); i++) {
            if (praiseList.get(i).getUserIdfer().equals(UserCenter.getUserInfo().getIdentifier())) {
                CommonImageLoader.load(R.drawable.like_red).noAnim().placeholder(R.drawable.like_red).into(imgPraise);
                isPraised = true;
                break;
            } else {
                CommonImageLoader.load(R.drawable.like_gray).noAnim().placeholder(R.drawable.like_gray).into(imgPraise);
                isPraised = false;
            }
        }
        if (praiseList.size() == 0) {
            CommonImageLoader.load(R.drawable.like_gray).noAnim().placeholder(R.drawable.like_gray).into(imgPraise);
            isPraised = false;
        }

        helper.setText(R.id.tvUserName, item.getUserInfo().getUserNickName());//发布用户
        helper.setText(R.id.tvcreateTime, momentsInfo.getCreateTime());//发布时间
        helper.setText(R.id.tvContent, momentsInfo.getContent());//内容

        RoundImageView ivImage = helper.getView(R.id.ivUserAvatar);//头像
        CommonImageLoader.load(item.getUserInfo().getUserAvatar()).noAnim().placeholder(R.drawable.default_head).into(ivImage);

        TextView ImgSex = helper.getView(R.id.tvSex);//性别
        ViewUtil.setDrawableLeft(ImgSex, item.getUserInfo().getSex().equals("1") ? R.drawable.boy_blue : R.drawable.girl_pink);
        ViewUtil.setDrawableBackGround(ImgSex, item.getUserInfo().getSex().equals("1") ? R.drawable.solid_stroke_city_circle_blue1 : R.drawable.solid_stroke_city_circle_red);

        TextView tvAdress = helper.getView(R.id.tvAdress);//定位地址
        tvAdress.setVisibility(momentsInfo.getAddress().equals("") ? View.INVISIBLE : View.VISIBLE);
        tvAdress.setText(momentsInfo.getAddress());

        TextView tvKm = helper.getView(R.id.tvKm);//距离
        tvKm.setVisibility(momentsInfo.getDistanceUnit().startsWith("km") ? View.INVISIBLE : View.VISIBLE);
        tvKm.setText(momentsInfo.getDistanceUnit());


        CityMomentsTypeAdapter tagAdapter = new CityMomentsTypeAdapter();//1.标签列表
        RecyclerView tagRecycler = helper.getView(R.id.recyclerType);
        tagRecycler.setLayoutManager(new LinearLayoutManager(ContextManager.getContext(), LinearLayoutManager.HORIZONTAL, false));
        tagRecycler.setAdapter(tagAdapter);
        List<String> tags = item.getUserInfo().getTags();
        Collections.shuffle(tags);
        tagAdapter.setList(tags.size() > 3 ? tags.subList(0, 3) : tags);//标签列表数据

        AskDetailImageAdapter imageAdapter = new AskDetailImageAdapter();//2.图片或视频列表
        RecyclerView imgRecycler = helper.getView(R.id.recyclerType2);
        imgRecycler.setLayoutManager(new GridLayoutManager(ContextManager.getContext(), 3));
        imgRecycler.addItemDecoration(new SpacesItemDecoration(0, 0));
        ((SimpleItemAnimator) imgRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        imgRecycler.setAdapter(imageAdapter);
        imageAdapter.setList(momentsInfo.getUrls());


        CityMomentsTypeAdapterx typeAdapter = new CityMomentsTypeAdapterx();//3.类别列表
        RecyclerView typeRecycler = helper.getView(R.id.recyclerType1);
        typeRecycler.setLayoutManager(new LinearLayoutManager(ContextManager.getContext(), LinearLayoutManager.HORIZONTAL, false));
        typeRecycler.setAdapter(typeAdapter);
        typeAdapter.setList(typeList);

        PraiseListView praiseListView = helper.getView(R.id.praiseListView);//点赞列表
        praiseListView.setDatas(praiseList);

        CommentListView commentList = helper.getView(R.id.commentList);//评论列表
        TextView tvShowMore = helper.getView(R.id.tvShowMore);//查看更多
        tvShowMore.setVisibility(list.size() > 3 ? View.VISIBLE : View.GONE);
        commentList.setDatas(list.size() > 3 ? list.subList(0, 3) : list);


        imageAdapter.setOnItemClickListener(new OnItemClickListener() {//图片或视频列表点击
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPresenter.ShowDetail(position, item.getMomentsInfo().getUrls());
            }
        });

        praiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener() {//点赞列表
            @Override
            public void onClick(int position) {
                mPresenter.ToUserInfo(praiseList.get(position).getUserIdfer(), praiseList.get(position).getUserAvatar());
            }
        });
        commentList.setOnItemClickListener(new CommentListView.OnItemClickListener() {
            @Override
            public void onItemClick(int commentPosition) {
                MomentsListBean.ListBean commentItem = list.get(commentPosition);
                if (commentItem.getUserIdfer().equals(UserCenter.getUserInfo().getIdentifier())) {//弹框删除或复制
                    mPresenter.ShowCommentDialog(commentItem.getReplyId(), commentItem.getContent(), true);
                } else {//回复别人的评论
                    mPresenter.BodyVisible(item.getMomentsInfo().getMId(), commentItem.getReplyId(),commentItem.getUserId());
                }
            }
        });
        commentList.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int commentPosition) {
                MomentsListBean.ListBean commentItem = list.get(commentPosition);
                if (commentItem.getUserIdfer().equals(UserCenter.getUserInfo().getIdentifier())) {//弹框删除或复制
                    mPresenter.ShowCommentDialog(commentItem.getReplyId(), commentItem.getContent(), true);
                } else {//回复别人的评论
                    mPresenter.ShowCommentDialog(commentItem.getReplyId(), commentItem.getContent(), item.getUserInfo().getUserIdfer().equals(UserCenter.getUserInfo().getIdentifier()));
                }

            }
        });
        tvShowMore.setOnClickListener(new View.OnClickListener() {//查看更多
            @Override
            public void onClick(View v) {
                mPresenter.toDetail(item.getMomentsInfo().getMId());
            }
        });
        commentList.setOnItemSpannableClickListener(new CommentListView.OnItemSpannableClickListener() {//跳往个人朋友圈页面
            @Override
            public void onItemSpannableClick(String userIdfer, String userAvatar) {
                mPresenter.ToUserInfo(userIdfer, userAvatar);
            }
        });

        imgPraise.setOnClickListener(new View.OnClickListener() {//点赞图标的点击事件
            @Override
            public void onClick(View v) {
                mPresenter.toggleCommentPraise(item.getMomentsInfo().getMId(), isPraised);
            }
        });
        helper.getView(R.id.imgComment).setOnClickListener(new View.OnClickListener() {//评论图标的点击事件
            @Override
            public void onClick(View v) {
                mPresenter.BodyVisible(item.getMomentsInfo().getMId(), "","");
            }
        });
        helper.getView(R.id.imgMore).setOnClickListener(new View.OnClickListener() {//更多图标的点击事件
            @Override
            public void onClick(View v) {
                snsPopupWindow = new SnsPopupWindow(ContextManager.getContext(), item.getUserInfo().getUserIdfer().equals(UserCenter.getUserInfo().getIdentifier()));//更多弹窗
                snsPopupWindow.setmItemClickListener(new PopupItemClickListener(item));
                snsPopupWindow.showPopupWindow(v);
            }
        });
        helper.getView(R.id.ivUserAvatar).setOnClickListener(new View.OnClickListener() {//头像图标的点击事件
            @Override
            public void onClick(View v) {
                mPresenter.ToUserInfo(item.getUserInfo().getUserIdfer(), item.getUserInfo().getUserAvatar());
            }
        });

    }


    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener {
        private long mLasttime = 0;
        private MomentsListBean item;

        PopupItemClickListener(MomentsListBean item) {
            this.item = item;
        }

        @Override
        public void onItemClick(ActionItem actionitem, int position) {
            switch (position) {
                case 0://点赞、取消点赞
                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
                    mPresenter.toggleCommentPraise(item.getMomentsInfo().getMId(), isPraised);
                    break;
                case 1://发布评论
                    mPresenter.BodyVisible(item.getMomentsInfo().getMId(), "","");
                    break;
                case 2:
                    if (item.getUserInfo().getUserIdfer().equals(UserCenter.getUserInfo().getIdentifier())) {
                        mPresenter.MomentsDel(item.getMomentsInfo().getMId());
                    } else {
                        mPresenter.complaintsUser(item.getUserInfo().getUserIdfer());
                    }
                    break;
                default:
                    break;
            }
        }


    }
}
