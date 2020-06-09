package com.fjx.mg.me.score;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.BalanceDetailModel;
import com.library.repository.models.ScoreListModel;

public class ScoreAdapter extends BaseAdapter<ScoreListModel.ScoreBean> {

    public ScoreAdapter() {
        super(R.layout.item_balance_detail);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreListModel.ScoreBean item) {
        helper.setText(R.id.tvTitle, item.getType());
        helper.setText(R.id.tvPrice, item.getScore());
        helper.setText(R.id.tvDate, item.getCreateTime());


    }
}
