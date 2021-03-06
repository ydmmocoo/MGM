package com.fjx.mg.news.comment;

import androidx.core.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.repository.models.CommentReplyModel;

public class NewsReplyAdapter extends BaseAdapter<CommentReplyModel.ReplyListBean> {
    private OnCLickPraiseListenr praiseListenr;

    public NewsReplyAdapter() {
        super(R.layout.item_comment);
    }

    public void setPraiseListenr(OnCLickPraiseListenr praiseListenr) {
        this.praiseListenr = praiseListenr;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CommentReplyModel.ReplyListBean item) {
        ImageView imageView = helper.getView(R.id.ivUserAvatar);
        CommonImageLoader.load(item.getUserAvatar()).circle().placeholder(R.drawable.user_default).into(imageView);
        helper.setText(R.id.tvUserName, item.getUserNickName());

        String content = "";
        if (TextUtils.isEmpty(item.getToReplyCont())) {
            content = item.getContent();
            helper.setText(R.id.tvContent, content);
        } else {
            content = item.getContent().concat("//@").concat(item.getToUserNick()).concat("：").concat(item.getToReplyCont());
            int start = item.getContent().length() + 2;
            int end = start + item.getToUserNick().length() + 1;
            SpannableStringBuilder span = StringUtil.setTextHightLines(content, start, end, ContextCompat.getColor(helper.itemView.getContext(), R.color.textColorBlue));
            helper.setText(R.id.tvContent, span);
        }

        helper.setText(R.id.tvCreateDate, item.getCreateTime());
        helper.setText(R.id.tvFavNum, item.getLikeNum());
        TextView tvReply = helper.getView(R.id.tvReplyCount);

        tvReply.setVisibility(View.GONE);

        TextView tvFavNum = helper.getView(R.id.tvFavNum);
        ViewUtil.setDrawableLeft(tvFavNum, item.isLike() ? R.drawable.news_fav1 : R.drawable.news_fav0);

        tvFavNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                praiseListenr.onClickPraise(helper.getLayoutPosition(), item);
            }
        });

    }

    public interface OnCLickPraiseListenr {
        void onClickPraise(int position, CommentReplyModel.ReplyListBean item);
    }
}
