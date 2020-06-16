package com.fjx.mg.food.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.fjx.mg.view.RatingBar;
import com.fjx.mg.view.WrapContentGridView;
import com.library.common.utils.CommonImageLoader;
import com.library.common.view.materialratingbar.MaterialRatingBar;
import com.library.repository.models.StoreEvaluateBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/25.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class RvEvaluateAdapter extends BaseQuickAdapter<StoreEvaluateBean.EvaluateListBean, BaseViewHolder> {

    public RvEvaluateAdapter(int layoutResId, @Nullable List<StoreEvaluateBean.EvaluateListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, StoreEvaluateBean.EvaluateListBean item) {
        //设置用户头像 iv_avatar
        CommonImageLoader.load(item.getUserImg())
                .placeholder(R.drawable.food_default).into(helper.getView(R.id.iv_avatar));
        //设置用户名字 tv_name
        helper.setText(R.id.tv_name,item.getFromNickName());
        //设置日期 tv_date
        helper.setText(R.id.tv_date,item.getCreateTime());
        //设置评分 ratting_bar
        MaterialRatingBar ratingBar= helper.getView(R.id.ratting_bar);
        //ratingBar.setProgress(Integer.parseInt(item.getGlobalScore()));
        ratingBar.setRating(Integer.parseInt(item.getGlobalScore()));
        //设置评论内容 tv_evaluate_content
        if (TextUtils.isEmpty(item.getContent())){
            helper.setGone(R.id.tv_evaluate_content,true);
        }else {
            helper.setGone(R.id.tv_evaluate_content,false);
            helper.setText(R.id.tv_evaluate_content, item.getContent());
        }
        //设置商品图片 gv_pic
        WrapContentGridView gv=helper.getView(R.id.gv_pic);
        if (item.getImgs().size()>0) {
            gv.setVisibility(View.VISIBLE);
            GvImageAdapter adapter = new GvImageAdapter(getContext(), item.getImgs());
            gv.setAdapter(adapter);
        }else {
            gv.setVisibility(View.GONE);
        }
        //设置商品名
        if (item.getGoodsList()!=null&&item.getGoodsList().size()>0) {
            helper.setText(R.id.tv_tag, item.getGoodsList().get(0).getGName());
        }
    }
}
