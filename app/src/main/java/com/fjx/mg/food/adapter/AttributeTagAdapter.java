package com.fjx.mg.food.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

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
public class AttributeTagAdapter extends TagAdapter<StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean.OptListBean> {

    private Context mContext;

    public AttributeTagAdapter(Context context, List<StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean.OptListBean> datas) {
        super(datas);
        mContext=context;
    }

    @Override
    public View getView(FlowLayout parent, int position, StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean.OptListBean item) {
        TextView tv= (TextView) View.inflate(mContext, R.layout.item_tag_tv,null);
        tv.setText(item.getName());
        return tv;
    }
}
