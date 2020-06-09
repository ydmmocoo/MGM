package com.fjx.mg.me.comment.adapter;

import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.fjx.mg.image.ImageActivity;
import com.fjx.mg.main.payment.detail.QuestionImageAdapter;
import com.fjx.mg.view.RoundImageView;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.ViewUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.MyReplyListCommentModel;
import com.library.repository.models.UserInfoModel;

public class QuestionCommentAdapter extends BaseAdapter<MyReplyListCommentModel.ReplyListBean> {
    private Activity context;
    private QuestionImageAdapter imageAdapter;

    public QuestionCommentAdapter(Activity context) {
        super(R.layout.item_my_reply_comment);
        this.context = context;

        addChildClickViewIds(R.id.tvBtn,R.id.tvDelete,R.id.tvQuestion);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MyReplyListCommentModel.ReplyListBean item) {
        UserInfoModel infoModel = UserCenter.getUserInfo();

        helper.setText(R.id.tvUserName, infoModel.getUNick());
        helper.setText(R.id.tvreplyTime, item.getReplyTime());


        RecyclerView imageRecycler = helper.getView(R.id.imageRecycler);
        imageAdapter = new QuestionImageAdapter();
        imageRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        imageRecycler.addItemDecoration(new SpacesItemDecoration(0, 0));
        ((SimpleItemAnimator) imageRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        imageRecycler.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Log.e("position:" + position, "DATA:" + imageAdapter.getData());
                String urls = JsonUtil.moderToString(item.getImagesList());
                context.startActivity(ImageActivity.newInstance(context, urls, position));
            }
        });

        StringBuilder sb = new StringBuilder();
        String content = "<font color=\"#e9285f\">" + item.getRewardCount() + "人" + "</font>" + "打赏";
        String content1 = "  共" + "<font color=\"#e9285f\">" + item.getRewardCount() + "AR" + "</font>";
        sb.append(content);
        sb.append(content1);
        helper.setText(R.id.tvRewardCountPrice, Html.fromHtml(sb.toString()));
        helper.setText(R.id.tvContent, item.getContent());
        helper.setText(R.id.tvQuestion, "@ " + item.getQuestion());

        TextView tvBtn = helper.getView(R.id.tvBtn);//底部按钮
        tvBtn.setVisibility(item.getImagesList().size()==0?View.GONE:View.VISIBLE);
        imageAdapter.setNewInstance(item.getOpen() ? item.getImagesList() : null);
        tvBtn.setText(item.getOpen() ? getContext().getString(R.string.fold) : getContext().getString(R.string.expand));


        if (item.getOpen()) {
            ViewUtil.setDrawableBottom(tvBtn, R.drawable.answer_close);
        } else {
            ViewUtil.setDrawableBottom(tvBtn, R.drawable.answer_open);
        }

        RoundImageView ivAvatar = helper.getView(R.id.ivUserAvatar);
        CommonImageLoader.load(infoModel.getUImg()).circle().placeholder(R.drawable.user_default).into(ivAvatar);

    }
}
