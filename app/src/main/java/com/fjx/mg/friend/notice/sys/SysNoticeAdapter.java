package com.fjx.mg.friend.notice.sys;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.IMNoticeModel;

public class SysNoticeAdapter extends BaseAdapter<IMNoticeModel> {

    public SysNoticeAdapter() {
        super(R.layout.item_im_sys_notice);
    }

    @Override
    protected void convert(BaseViewHolder helper, IMNoticeModel item) {
        helper.setText(R.id.tvTitle, item.getTitle());
        helper.setText(R.id.tvDate, item.getCreateTime());
        helper.setText(R.id.tvContent, item.getRemark());
    }
}
