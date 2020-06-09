package com.fjx.mg.job.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.DimensionUtil;
import com.library.repository.models.JobModel;

public class JobHuntinAdapter extends BaseAdapter<JobModel> {

    private boolean selfPadding;

    public void setSelfPadding(boolean selfPadding) {
        this.selfPadding = selfPadding;
    }

    public JobHuntinAdapter() {
        super(R.layout.item_job);
    }

    @Override
    protected void convert(BaseViewHolder helper, JobModel item) {
        helper.setText(R.id.tvTitle, item.getTitle());
        helper.setText(R.id.tvMoney, item.getPay());

        String desc = "";
        if (!TextUtils.isEmpty(item.getJobType())) {
            desc = desc.concat(item.getJobType()).concat("|");
        }
        if (!TextUtils.isEmpty(item.getWorkYear())) {
            desc = desc.concat(item.getWorkYear()).concat("|");
        }
        if (!TextUtils.isEmpty(item.getEducation())) {
            desc = desc.concat(item.getEducation()).concat("|");
        }
        if (!TextUtils.isEmpty(desc)) desc = desc.substring(0, desc.length() - 1);

        helper.setText(R.id.tvDesc, desc);
        helper.setText(R.id.tvAddress, item.getCityName());
        helper.setText(R.id.tvDate, item.getCreateTime());


        if (selfPadding) {
            ConstraintLayout cl = helper.getView(R.id.llparent);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) cl.getLayoutParams();
            params.bottomMargin = DimensionUtil.dip2px(10);
        } else {
            ConstraintLayout cl = helper.getView(R.id.llparent);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) cl.getLayoutParams();
            params.bottomMargin = DimensionUtil.dip2px(0);
        }

        ImageView ivStatus = helper.getView(R.id.ivStatus);
        helper.getView(R.id.ivStatus).setVisibility(TextUtils.equals(item.getStatus(), "1") || TextUtils.equals(item.getStatus(), "3") ? View.VISIBLE : View.GONE);
        if (TextUtils.equals(item.getStatus(), "3")) {
            ivStatus.setImageResource(R.drawable.publish_invalid);
        } else {
            ivStatus.setImageResource(R.drawable.ic_closed);
        }
    }
}
