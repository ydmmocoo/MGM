package com.fjx.mg.moments.tag;

import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.ViewUtil;
import com.library.repository.models.TagColor;
import com.library.repository.models.TagModel;

import java.util.ArrayList;
import java.util.Random;

public class TagAdapter extends BaseAdapter<TagModel.TagsBean> {
    public TagAdapter() {
        super(R.layout.item_tag_type);
    }

    @Override
    protected void convert(BaseViewHolder helper, TagModel.TagsBean item) {

        ArrayList<TagColor> list = new ArrayList<>();
        Random random = new Random();
        int i = random.nextInt(6);
        list.add(new TagColor(R.color.colorRed, R.drawable.friends_add_tag_defualt_bg, R.drawable.solid_stroke_tag_color_red1));
        list.add(new TagColor(R.color.colorGreen, R.drawable.solid_stroke_tag_color_green, R.drawable.solid_stroke_tag_color_green1));
        list.add(new TagColor(R.color.my_colors, R.drawable.solid_stroke_tag_color_my_colors, R.drawable.solid_stroke_tag_color_my_colors1));
        list.add(new TagColor(R.color.textColorYellow, R.drawable.solid_stroke_tag_color_my_yellow, R.drawable.solid_stroke_tag_color_my_yellow1));
        list.add(new TagColor(R.color.bule_0049, R.drawable.solid_stroke_tag_color_blue, R.drawable.solid_stroke_tag_color_blue1));
        list.add(new TagColor(R.color.textColorYellow4, R.drawable.solid_stroke_tag_color_yellow, R.drawable.solid_stroke_tag_color_yellow1));
        TextView type = helper.getView(R.id.tvType);
        type.setText(item.getName());
        ViewUtil.setDrawableBackGround(type, item.getSelected() ? list.get(i).getBack1() : list.get(0).getBack());
        ViewUtil.setTextColor(type, item.getSelected() ? R.color.white : R.color.friends_add_tag_default);
    }
}
