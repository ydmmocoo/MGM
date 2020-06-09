package com.fjx.mg.nearbycity.adapter;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.fjx.mg.image.ImageActivity;
import com.fjx.mg.main.payment.detail.AskDetailImageAdapter;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.ViewUtil;
import com.library.repository.models.TotalCityCircleListModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author    by hanlz
 * Date      on 2019/10/17.
 * Description：全部同城列表适配器
 */
public class NearbyCityAdapter extends BaseAdapter<TotalCityCircleListModel> {


    public NearbyCityAdapter() {
        super(R.layout.item_nearby_city_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, final TotalCityCircleListModel item) {
        helper.setText(R.id.tvUserName, item.getuNick());
        helper.setText(R.id.tvCreateTime, item.getCreateTime());
        helper.setText(R.id.tvLook, item.getReadNum() + "");
        helper.setText(R.id.tvComment, item.getCommentNum());
        helper.setText(R.id.tvType, item.getTypeName());
//        helper.setText(R.id.tvLike, item.getLikeNum());
        String content = "";
        if (item.getContent().contains("\n")) {
            content = item.getContent();
        } else {
            content = item.getContent().replaceAll("\r", "\n");
        }
        helper.setText(R.id.tvContent, content);
        final List<String> images = item.getImages();
        RecyclerView rvNearbyCityPic = helper.getView(R.id.rvNearbyCityPic);
        AskDetailImageAdapter imageAdapter = new AskDetailImageAdapter();//2.图片或视频列表
        rvNearbyCityPic.setAdapter(imageAdapter);
        rvNearbyCityPic.addItemDecoration(new SpacesItemDecoration(0, 0));
        if (item.getThumImages().size() > 0 && item.getThumImages() != null) {
            //显示图片九宫格
            imageAdapter.setList(item.getThumImages());
        }
        imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                view.getContext().startActivity(ImageActivity.newInstance(view.getContext(), JsonUtil.moderToString(images), position));
            }
        });
        CircleImageView imageView = helper.getView(R.id.ivHeaderPic);
        CommonImageLoader.load(item.getuAvatar()).placeholder(R.drawable.food_default).into(imageView);
        TextView tvLike = helper.getView(R.id.tvLike);
        tvLike.setText(item.getLikeNum());
        if (item.isLike()) {
            ViewUtil.setDrawableLeft(tvLike, R.drawable.like_red);
        } else {
            ViewUtil.setDrawableLeft(tvLike, R.drawable.like_gray);
        }
        addChildClickViewIds(R.id.tvLike,R.id.ivHeaderPic);
    }


}
