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
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.MyCityCircleListModel;
import com.library.repository.models.NearbyCItyGetListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.TotalCityCircleListModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Author    by hanlz
 * Date      on 2019/10/18.
 * Description：
 */
public class TopTypeDetailAdapter extends BaseAdapter<TotalCityCircleListModel> implements View.OnClickListener {

    private boolean isLike;

    public TopTypeDetailAdapter() {
        super(R.layout.item_nearby_city_item_layout);
    }


    @Override
    protected void convert(BaseViewHolder helper, TotalCityCircleListModel item) {
        helper.setText(R.id.tvType, item.getTypeName());
        String content = "";
        if (item.getContent().contains("\n")) {
            content = item.getContent();
        } else {
            content = item.getContent().replaceAll("\r", "\n");
        }
        helper.setText(R.id.tvContent, content);
        if (item.getReadNum() > 0) {
            helper.setText(R.id.tvLook, item.getReadNum() + "");
        }

        if (!"0".equals(item.getLikeNum())) {
            helper.setText(R.id.tvLike, item.getLikeNum());
        }

        if (!"0".equals(item.getCommentNum())) {
            helper.setText(R.id.tvComment, item.getCommentNum());
        }
        TextView tvIsTop = helper.getView(R.id.tvIsTop);
        tvIsTop.setVisibility(item.isTop() ? View.VISIBLE : View.INVISIBLE);
        helper.setText(R.id.tvUserName, item.getuNick());
        helper.setText(R.id.tvCreateTime, item.getCreateTime());
        helper.setText(R.id.tvLook, item.getReadNum() + "");
        helper.setText(R.id.tvComment, item.getCommentNum());
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
        helper.setText(R.id.tvLike, item.getLikeNum());
        helper.getView(R.id.tvLike).setOnClickListener(this);
        isLike = item.isLike();
        if (item.isLike()) {
            ViewUtil.setDrawableLeft(tvLike, R.drawable.like_red);
        } else {
            ViewUtil.setDrawableLeft(tvLike, R.drawable.like_gray);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvLike) {
            TextView tvLike = (TextView) view;
            String num = tvLike.getText().toString();
            TotalCityCircleListModel item = (TotalCityCircleListModel) view.getTag();
            if (!isLike) {
                isLike = true;
                item.setLike(isLike);
                ViewUtil.setDrawableLeft(tvLike, R.drawable.like_red);
                tvLike.setText(StringUtil.add(num, "1"));
            } else {
                isLike = false;
                item.setLike(isLike);
                ViewUtil.setDrawableLeft(tvLike, R.drawable.like_gray);
                tvLike.setText(StringUtil.subtract(num, "1", 0L));
            }
        }
    }
}
