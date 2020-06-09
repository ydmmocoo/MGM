package com.fjx.mg.main.payment.detail;

import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.fjx.mg.view.RoundImageView;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.ViewUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.QuestionReplyModel;
import com.library.repository.models.UserInfoModel;

public class AnswerListAdapter extends BaseAdapter<QuestionReplyModel.ReplyListBean> {
    private OnCLickPraiseListenr praiseListenr;
    private OnCLickOpenListenr openListenr;
    private Activity context;
    private QuestionImageAdapter imageAdapter;

    void setPraiseListenr(OnCLickPraiseListenr praiseListenr) {
        this.praiseListenr = praiseListenr;
    }

    void OnCLickOpenListenr(OnCLickOpenListenr openListenr) {
        this.openListenr = openListenr;
    }

    AnswerListAdapter(Activity context) {
        super(R.layout.item_answer);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final QuestionReplyModel.ReplyListBean item) {
        RoundImageView imageView = helper.getView(R.id.ivUserAvatar);
        RecyclerView imageRecycler = helper.getView(R.id.imageRecycler);
        ImageView Imacept = helper.getView(R.id.Imacept);
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

        CommonImageLoader.load(item.getUserAvatar()).circle().placeholder(R.drawable.user_default).into(imageView);

        TextView tvUserName = helper.getView(R.id.tvUserName);
        tvUserName.setText(item.getUserNickName());
        Imacept.setVisibility(item.getIsAccpet().equals("1") ? View.VISIBLE : View.GONE);
        helper.setText(R.id.tvCreateDate, item.getReplyTime());
        helper.setText(R.id.tvContent, item.getContent());

        String content = "有" + "<font color=\"#e9285f\">" + item.getRewardCount() + "人" + "</font>" + "打赏";
        helper.setText(R.id.tvNum, Html.fromHtml(content));

        TextView tvFavNum = helper.getView(R.id.tvFavNum);//右上角按钮
        if (item.getStatus() == 1) {
            tvFavNum.setText(item.getMine() ? context.getString(R.string.accept) : context.getString(R.string.reward));
        } else {
            tvFavNum.setText(context.getString(R.string.reward));
        }
        TextView tvBtn = helper.getView(R.id.tvBtn);//底部按钮
        tvBtn.setVisibility(item.getImagesList().size() == 0 ? View.GONE : View.VISIBLE);
        imageAdapter.setList(item.getOpen() ? item.getImagesList() : null);
        tvBtn.setText(item.getOpen() ? context.getString(R.string.fold) : context.getString(R.string.expand));


        UserInfoModel userInfo = UserCenter.getUserInfo();
        final String question_uid = item.getUid();
        final String user_uId = userInfo.getUId();//预留如果该条回复是本人回复，去掉赞赏的按钮,而且支持长按删除。
        tvFavNum.setVisibility(question_uid.equals(user_uId) ? View.GONE : View.VISIBLE);
//        if (question_uid.equals(user_uId)) {
//            tvFavNum.setText("删除");
//            tvFavNum.setText("删除");
//            ViewUtil.setDrawableLeft(tvFavNum, -1);
//        }
        if (item.getOpen()) {
            ViewUtil.setDrawableBottom(tvBtn, R.drawable.answer_close);
        } else {
            ViewUtil.setDrawableBottom(tvBtn, R.drawable.answer_open);
        }

        tvFavNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                praiseListenr.onClickPraise(helper.getLayoutPosition(), item, question_uid.equals(user_uId));
            }
        });
        tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openListenr.onClickOpen(helper.getLayoutPosition(), item);
            }
        });
    }

    public interface OnCLickPraiseListenr {
        void onClickPraise(int position, QuestionReplyModel.ReplyListBean item, Boolean me);
    }

    public interface OnCLickOpenListenr {
        void onClickOpen(int position, QuestionReplyModel.ReplyListBean item);
    }
}
