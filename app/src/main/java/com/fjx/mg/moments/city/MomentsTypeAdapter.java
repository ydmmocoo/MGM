package com.fjx.mg.moments.city;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.TypeListModel;
import com.tencent.qcloud.uikit.common.widget.ShadeImageView;

public class MomentsTypeAdapter extends BaseAdapter<TypeListModel.TypeListBean> {

    public MomentsTypeAdapter() {
        super(R.layout.item_moments_type);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final TypeListModel.TypeListBean item) {
        ShadeImageView imgType = helper.getView(R.id.imgType);
        CommonImageLoader.load(item.getImg()).noAnim().placeholder(R.drawable.login_top).into(imgType);
        helper.setText(R.id.tvType, item.getTypeName());
    }
}
