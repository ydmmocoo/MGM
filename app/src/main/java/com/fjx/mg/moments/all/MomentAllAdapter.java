package com.fjx.mg.moments.all;


import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.StringUtil;
import com.library.repository.models.PersonalMomentListModel;

import java.util.List;
import java.util.regex.Pattern;

public class MomentAllAdapter extends BaseAdapter<PersonalMomentListModel.MomentsListBean> {


    public MomentAllAdapter() {
        super(R.layout.item_moments_all);
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonalMomentListModel.MomentsListBean item) {

        View view2 = helper.getView(R.id.view2);
        View view3 = helper.getView(R.id.view3);
        View view4 = helper.getView(R.id.view4);

        ImageView img1_1 = helper.getView(R.id.img1_1);

        ImageView tv_duration = helper.getView(R.id.tv_duration);
        String reg = "(mp4|flv|avi|rm|rmvb|wmv)";
        Pattern p = Pattern.compile(reg);


        ImageView img2_1 = helper.getView(R.id.img2_1);
        ImageView img2_2 = helper.getView(R.id.img2_2);

        ImageView img3_1 = helper.getView(R.id.img3_1);
        ImageView img3_2 = helper.getView(R.id.img3_2);
        ImageView img3_3 = helper.getView(R.id.img3_3);

        ImageView img4_1 = helper.getView(R.id.img4_1);
        ImageView img4_2 = helper.getView(R.id.img4_2);
        ImageView img4_3 = helper.getView(R.id.img4_3);
        ImageView img4_4 = helper.getView(R.id.img4_4);

        helper.setText(R.id.tvTime, item.getCreateTime());//时间
        TextView tvTime = helper.getView(R.id.tvTime);//时间
        String createTime = item.getCreateTime();
        if (createTime.endsWith(getContext().getString(R.string.month))) {
            int length = createTime.length();
            String start = createTime.substring(0, length - 3);
            String end = createTime.substring(length - 3, length);
            Log.e(start, end);
//            String str3 = "<strong> <font color='#333333'><big>" + start + "</small></font>" + end;
//            tvTime.setText(Html.fromHtml(str3));
            tvTime.setText(start);
            helper.setText(R.id.tvMonth,end);
        } else {
//            String str3 = "<font color='#333333'><big>" + createTime + "</big></font>";
//            tvTime.setText(Html.fromHtml(str3));
            tvTime.setText(createTime);
        }


        List<String> urls = item.getUrls();
        int size = urls.size();
        TextView tvContent = helper.getView(R.id.tvContent);

        tvContent.setText(item.getContent());
        if (size != 0) {
            tvContent.setMaxLines(4);
        } else {
            tvContent.setMaxLines(2);
        }


        img1_1.setVisibility(size == 1 ? View.VISIBLE : View.GONE);
        view2.setVisibility(size == 2 ? View.VISIBLE : View.GONE);
        view3.setVisibility(size == 3 ? View.VISIBLE : View.GONE);
        view4.setVisibility(size > 3 ? View.VISIBLE : View.GONE);
        RelativeLayout rlShared = helper.getView(R.id.rlShared);
        boolean isShow = StringUtil.isNotEmpty(item.getShareInfo().getTitle()) || StringUtil.isNotEmpty(item.getShareInfo().getImg()) || StringUtil.isNotEmpty(item.getShareInfo().getDesc());
        rlShared.setVisibility(size == 0 && isShow ? View.VISIBLE : View.GONE);
        LinearLayout llNoShared = helper.getView(R.id.llNoShared);
        llNoShared.setVisibility(size == 0 ? View.GONE : View.VISIBLE);
        TextView tvText = helper.getView(R.id.tvText);
        tvText.setMaxLines(2);
        tvText.setEllipsize(TextUtils.TruncateAt.END);
        switch (size) {
            case 0:
                if (StringUtil.isNotEmpty(item.getContent())) {
                    helper.setText(R.id.tvText, item.getContent());
                }
                helper.setVisible(R.id.tvText, true);
                share(item, helper);
                break;
            case 1:
                tv_duration.setVisibility(p.matcher(urls.get(0)).find() ? View.VISIBLE : View.GONE);
                CommonImageLoader.load(urls.get(0)).placeholder(R.drawable.food_default).into(img1_1);
                if (StringUtil.isNotEmpty(item.getContent())) {
                    helper.setText(R.id.tvContent, item.getContent());//内容
                }
                break;
            case 2:
                CommonImageLoader.load(urls.get(0)).placeholder(R.drawable.food_default).into(img2_1);
                CommonImageLoader.load(urls.get(1)).placeholder(R.drawable.food_default).into(img2_2);
                if (StringUtil.isNotEmpty(item.getContent())) {
                    helper.setText(R.id.tvContent, item.getContent());//内容
                }
                break;
            case 3:
                CommonImageLoader.load(urls.get(0)).placeholder(R.drawable.food_default).into(img3_1);
                CommonImageLoader.load(urls.get(1)).placeholder(R.drawable.food_default).into(img3_2);
                CommonImageLoader.load(urls.get(2)).placeholder(R.drawable.food_default).into(img3_3);
                if (StringUtil.isNotEmpty(item.getContent())) {
                    helper.setText(R.id.tvContent, item.getContent());//内容
                }
                break;
            default:
                CommonImageLoader.load(urls.get(0)).placeholder(R.drawable.food_default).into(img4_1);
                CommonImageLoader.load(urls.get(1)).placeholder(R.drawable.food_default).into(img4_2);
                CommonImageLoader.load(urls.get(2)).placeholder(R.drawable.food_default).into(img4_3);
                CommonImageLoader.load(urls.get(3)).placeholder(R.drawable.food_default).into(img4_4);
                if (StringUtil.isNotEmpty(item.getContent())) {
                    helper.setText(R.id.tvContent, item.getContent());//内容
                }
                break;
        }

    }

    private void share(PersonalMomentListModel.MomentsListBean item, BaseViewHolder helper) {
        ImageView ivShared = helper.getView(R.id.ivShared);
        PersonalMomentListModel.ShareInfoBean shareInfo = item.getShareInfo();
        if (StringUtil.isNotEmpty(shareInfo.getTitle()) || StringUtil.isNotEmpty(shareInfo.getImg()) || StringUtil.isNotEmpty(shareInfo.getDesc())) {
            if (StringUtil.isNotEmpty(shareInfo.getImg())) {
                CommonImageLoader.load(shareInfo.getImg()).placeholder(R.drawable.food_default).into(ivShared);
                ivShared.setVisibility(View.VISIBLE);
            } else {
                ivShared.setVisibility(View.GONE);
            }
            if (StringUtil.isNotEmpty(shareInfo.getTypeName()) && StringUtil.isNotEmpty(shareInfo.getDesc())) {
                helper.setText(R.id.tvShareTitle, shareInfo.getTypeName().concat(shareInfo.getDesc()));
            } else {
                helper.setText(R.id.tvShareTitle, shareInfo.getDesc());//内容
            }
            TextView tvShareContent = helper.getView(R.id.tvShareContent);
            if (StringUtil.isNotEmpty(item.getContent())) {
                helper.setText(R.id.tvShareContent, item.getContent());
                tvShareContent.setVisibility(View.VISIBLE);
            } else {
                tvShareContent.setVisibility(View.GONE);
            }
        }
    }

}
