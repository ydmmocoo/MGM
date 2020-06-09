package com.fjx.mg.food.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.view.flowlayout.FlowLayout;
import com.fjx.mg.view.flowlayout.TagFlowLayout;
import com.library.repository.models.StoreGoodsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yedeman
 * @date 2020/5/26.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class LvAttributeAdapter extends BaseAdapter {

    private Context mContext;
    private List<StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean> mList;
    private List<String> mIds = new ArrayList<>();
    private List<String> mNames = new ArrayList<>();

    public LvAttributeAdapter(Context context, List<StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean> list) {
        mContext = context;
        mList = list;
        for (int i = 0; i < mList.size(); i++) {
            mIds.add(mList.get(i).getOptList().get(0).getAId());
            mNames.add(mList.get(i).getOptList().get(0).getName());
        }
    }

    public void setData(List<StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_lv_attribute, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTvAttribute.setText(mList.get(position).getName());
        AttributeTagAdapter adapter = new AttributeTagAdapter(mContext, mList.get(position).getOptList());
        holder.mFlAttribute.setAdapter(adapter);
        adapter.setSelectedList(0);
        holder.mFlAttribute.setOnTagClickListener((view, pos, parent1) -> {
            adapter.setSelectedList(pos);
            mIds.set(position, mList.get(position).getOptList().get(pos).getAId());
            mNames.set(position, mList.get(position).getOptList().get(pos).getName());
            return true;
        });
        return convertView;
    }

    public String getIds() {
        String ids = "";
        for (int i=0;i<mIds.size();i++) {
            if (i==0){
                ids=mIds.get(i);
            }else {
                ids=ids+","+mIds.get(i);
            }
        }
        return ids;
    }

    public String getNames() {
        String names = "";
        for (int i=0;i<mNames.size();i++) {
            if (i==0){
                names=mNames.get(i);
            }else {
                names=names+","+mNames.get(i);
            }
        }
        return names;
    }

    public void clearAttr(){

    }

    static class ViewHolder {
        @BindView(R.id.tv_attribute)
        TextView mTvAttribute;
        @BindView(R.id.fl_attribute)
        TagFlowLayout mFlAttribute;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
