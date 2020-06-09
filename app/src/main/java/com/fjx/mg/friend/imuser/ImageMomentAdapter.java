package com.fjx.mg.friend.imuser;

import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.MomentsCityCircleImagesModel;

/**
 * Author    by hanlz
 * Date      on 2020/3/9.
 * Description：
 */
public class ImageMomentAdapter extends BaseAdapter<MomentsCityCircleImagesModel> {
    public ImageMomentAdapter() {
        super(R.layout.item_image);
    }

    @Override
    protected void convert(BaseViewHolder helper, MomentsCityCircleImagesModel item) {
        ImageView ivImage = helper.getView(R.id.ivImage);
        CommonImageLoader.load(item.getImgUrl()).noAnim().placeholder(R.drawable.food_default).into(ivImage);
    }
}
