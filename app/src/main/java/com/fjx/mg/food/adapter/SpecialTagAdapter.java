package com.fjx.mg.food.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.view.flowlayout.FlowLayout;
import com.fjx.mg.view.flowlayout.TagAdapter;
import com.library.repository.models.StoreGoodsBean;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/27.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class SpecialTagAdapter extends TagAdapter<StoreGoodsBean.CateListBean.GoodsListBean.SpecialListBean> {

    private Context mContext;
    private List<StoreGoodsBean.CateListBean.GoodsListBean.SpecialListBean> mList;

    public SpecialTagAdapter(Context context,List<StoreGoodsBean.CateListBean.GoodsListBean.SpecialListBean> datas) {
        super(datas);
        mContext=context;
        mList=datas;
    }

    @Override
    public View getView(FlowLayout parent, int position, StoreGoodsBean.CateListBean.GoodsListBean.SpecialListBean item) {
        TextView tv= (TextView) View.inflate(mContext, R.layout.item_tag_tv,null);
        tv.setText(item.getName());
        return tv;
    }
}
