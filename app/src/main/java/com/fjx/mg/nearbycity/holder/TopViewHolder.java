package com.fjx.mg.nearbycity.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.nearbycity.TopTypeDetailActivity;
import com.fjx.mg.nearbycity.adapter.TopAdapter;
import com.library.common.utils.StringUtil;
import com.library.repository.models.NearbyCityConfigModel;
import com.library.repository.models.NearbyCityTypeListModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Author    by hanlz
 * Date      on 2019/10/17.
 * Description：同城顶部详细分类ViewHolder
 */
public class TopViewHolder extends RecyclerView.ViewHolder implements OnItemClickListener {

    private RecyclerView mRvTopType;
    private TopAdapter mAdapter;
    private NearbyCityConfigModel mData;

    public TopViewHolder(@NonNull View itemView) {
        super(itemView);
        mRvTopType = itemView.findViewById(R.id.rvTopType);
        mAdapter = new TopAdapter();
        mRvTopType.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    public void setData(List<NearbyCityTypeListModel> datas, NearbyCityConfigModel mData, String mJson) {
        if (!datas.isEmpty()) {
            mAdapter.setList(datas);
        }
        this.mData = mData;
        try {
            if (StringUtil.isEmpty(mJson)) {
                return;
            }
            JSONObject object = new JSONObject(mJson);
            if (object == null) {
                return;
            }
            JSONObject cityCircleUnReadList = object.optJSONObject("data").optJSONObject("cityCircleUnReadList");
            mAdapter.setUnReadCountData(cityCircleUnReadList);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    public void setData(List<NearbyCityTypeListModel> datas, NearbyCityConfigModel mData) {
        if (!datas.isEmpty()) {
            mAdapter.setList(datas);
        }
        this.mData = mData;
    }
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        NearbyCityTypeListModel model = (NearbyCityTypeListModel) adapter.getItem(position);
        view.getContext().startActivity(TopTypeDetailActivity.newIntent(view.getContext(), model, mData));
        view.findViewById(R.id.tvTips).setVisibility(View.GONE);//点击后隐藏未读数小红点
    }
}
