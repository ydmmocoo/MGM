package com.fjx.mg.news.detail;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.BuildConfig;
import com.fjx.mg.R;
import com.fjx.mg.friend.imuser.NewImUserDetailActivity;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.ViewUtil;
import com.library.repository.models.NewsCommentModel;

public class CommentAdapter extends BaseAdapter<NewsCommentModel.CommentListBean> {
    private OnCLickPraiseListenr praiseListenr;
    private boolean hideFavNum;

    public void setHideFavNum(boolean hideFavNum) {
        this.hideFavNum = hideFavNum;
    }

    public void setPraiseListenr(OnCLickPraiseListenr praiseListenr) {
        this.praiseListenr = praiseListenr;
    }

    public CommentAdapter() {
        super(R.layout.item_comment);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final NewsCommentModel.CommentListBean item) {
        ImageView imageView = helper.getView(R.id.ivUserAvatar);
        CommonImageLoader.load(item.getUserAvatar()).circle().placeholder(R.drawable.user_default).into(imageView);
        helper.setText(R.id.tvUserName, item.getUserNickName());
        helper.setText(R.id.tvContent, item.getContent());
        helper.setText(R.id.tvCreateDate, item.getCreateTime());

        if (hideFavNum) {
            helper.getView(R.id.tvFavNum).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.tvFavNum).setVisibility(View.VISIBLE);
            helper.setText(R.id.tvFavNum, item.getLikeNum());
        }

        TextView tvReply = helper.getView(R.id.tvReplyCount);
        GradientDrawableHelper.whit(tvReply).setColor(R.color.textColorYellow3).setCornerRadius(50);
        tvReply.setText(item.getReplyNum().concat(getContext().getString(R.string.reply_num)));

        final TextView tvFavNum = helper.getView(R.id.tvFavNum);
        ViewUtil.setDrawableLeft(tvFavNum, item.isLike() ? R.drawable.news_fav1 : R.drawable.news_fav0);
        tvFavNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                praiseListenr.onClickPraise(helper.getLayoutPosition(), item,tvFavNum);
            }
        });
        //点击头像跳转个人资料
        imageView.setOnClickListener(v -> {
            String id="";
            if (BuildConfig.DEBUG){//测试环境
                id="fjx"+item.getUserId();
            }else {
                id="MGM"+item.getUserId();
            }
            Intent intent=NewImUserDetailActivity.newInstance(getContext(), id);
            getContext().startActivity(intent);
        });
    }

    public interface OnCLickPraiseListenr {
        void onClickPraise(int position, NewsCommentModel.CommentListBean item,TextView tvFavNum);
    }
}
