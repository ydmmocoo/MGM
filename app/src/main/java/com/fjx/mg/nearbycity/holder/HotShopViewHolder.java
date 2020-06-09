package com.fjx.mg.nearbycity.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.nearbycity.adapter.HotShopAdapter;
import com.fjx.mg.main.yellowpage.YellowPageActivityV1;
import com.fjx.mg.main.yellowpage.detail.YellowPageDetailActivity;
import com.library.repository.models.NearbyCityCompanyListModel;

import java.util.List;


/**
 * Author    by hanlz
 * Date      on 2019/10/17.
 * Description：
 */
public class HotShopViewHolder extends RecyclerView.ViewHolder implements OnItemClickListener, View.OnClickListener {

    private TextView mTvMoreHotShop;
    private RecyclerView mRvHotShop;
    private HotShopAdapter mAdapter;

    public HotShopViewHolder(@NonNull View itemView) {
        super(itemView);
        mTvMoreHotShop = itemView.findViewById(R.id.tvMoreHotShop);
        mRvHotShop = itemView.findViewById(R.id.rvHotShop);
        mAdapter = new HotShopAdapter();
        mRvHotShop.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mTvMoreHotShop.setOnClickListener(this);
    }


    public void setData(List<NearbyCityCompanyListModel> datas) {
        if (datas.size() > 4) {
            List<NearbyCityCompanyListModel> nearbyCityCompanyListModels = datas.subList(0, 4);
            mAdapter.setList(nearbyCityCompanyListModels);
        } else {
            mAdapter.setList(datas);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        view.getContext().startActivity(YellowPageDetailActivity.newInstance(view.getContext(),
                mAdapter.getItem(position).getcId(), "", true,
                "-1"));
    }

    @Override
    public void onClick(View view) {
        view.getContext().startActivity(YellowPageActivityV1.newInstance(view.getContext()));
    }
}
