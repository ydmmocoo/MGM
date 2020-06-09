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
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.MyCityCircleListModel;
import com.library.repository.models.UserInfoModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Author    by hanlz
 * Date      on 2019/10/18.
 * Description：我的同城适配器
 */
public class MyNearbyCityAdapter extends BaseAdapter<MyCityCircleListModel> {

    public MyNearbyCityAdapter() {
        super(R.layout.item_nearby_city_item_layout);

        addChildClickViewIds(R.id.tvLike,R.id.ivPop);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyCityCircleListModel item) {
        UserInfoModel infoModel = UserCenter.getUserInfo();
        //用户名
        helper.setText(R.id.tvUserName, infoModel.getUNick());
        //头像
        CircleImageView ivAvatar = helper.getView(R.id.ivHeaderPic);
        CommonImageLoader.load(infoModel.getUImg()).circle().placeholder(R.drawable.food_default).into(ivAvatar);

        helper.setText(R.id.tvCreateTime, item.getCreateTime());
        helper.setText(R.id.tvType, item.getTypeName());
        String content = item.getContent();
        if (content.contains("\n")) {
            content = item.getContent();
        } else {
            content = content.replaceAll("\r", "\n");
        }
        helper.setText(R.id.tvContent, content);
        helper.setText(R.id.tvLook, item.getReadNum() + "");
        helper.setText(R.id.tvLike, item.getLikeNum());
        helper.setText(R.id.tvComment, item.getCommentNum());
        TextView tvLike = helper.getView(R.id.tvLike);
        if (StringUtil.equals("0", item.getLikeNum())) {
            ViewUtil.setDrawableLeft(tvLike, R.drawable.like_gray);
        } else {
            ViewUtil.setDrawableLeft(tvLike, R.drawable.like_red);
        }
        TextView tvIsTop = helper.getView(R.id.tvIsTop);
        tvIsTop.setVisibility(item.getIsTop() ? View.VISIBLE : View.INVISIBLE);
        helper.setVisible(R.id.ivPop, true);

        final List<String> images = item.getImages();
        RecyclerView rvNearbyCityPic = helper.getView(R.id.rvNearbyCityPic);
        AskDetailImageAdapter imageAdapter = new AskDetailImageAdapter();//2.图片或视频列表
        rvNearbyCityPic.setAdapter(imageAdapter);
        rvNearbyCityPic.addItemDecoration(new SpacesItemDecoration(0, 0));
        if (!images.isEmpty() && images.size() > 0 && images != null) {
            //显示图片九宫格
            imageAdapter.setList(images);
        }
        imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                view.getContext().startActivity(ImageActivity.newInstance(view.getContext(), JsonUtil.moderToString(images), position));
            }
        });
    }
}
