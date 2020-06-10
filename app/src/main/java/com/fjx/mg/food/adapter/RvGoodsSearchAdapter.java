package com.fjx.mg.food.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.fjx.mg.dialog.AddShopCartDialog;
import com.fjx.mg.view.RoundImageView;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.DimensionUtil;
import com.library.repository.models.GoodsSearchBean;
import com.library.repository.models.StoreGoodsBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author yedeman
 * @date 2020/6/8.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class RvGoodsSearchAdapter extends BaseQuickAdapter<GoodsSearchBean.GoodsListBean, BaseViewHolder> {

    private OnAddShoppingCartListener mListener;

    public RvGoodsSearchAdapter(int layoutResId, @Nullable List<GoodsSearchBean.GoodsListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, GoodsSearchBean.GoodsListBean item) {
        //设置商品图片
        RoundImageView ivPic=helper.getView(R.id.iv_pic);
        ivPic.setRectAdius(DimensionUtil.dip2px(5));
        CommonImageLoader.load(item.getGImg())
                .placeholder(R.drawable.food_default).into(ivPic);
        //设置商品名称
        helper.setText(R.id.tv_name,item.getGName());
        //设置商品价格
        helper.setText(R.id.tv_price,getContext().getResources().getString(R.string.goods_price,item.getPrice()));
        //设置是否显示起
        if (item.getSpecialCount()<=1) {
            helper.setVisible(R.id.tv_price_minimum,false);
        }else {
            helper.setVisible(R.id.tv_price_minimum,true);
        }
        //设置月售
        helper.setText(R.id.tv_monthly_sales,getContext().getResources().getString(R.string.monthly_sales,String.valueOf(item.getSaleCount())));
        //设置商品数量
        ImageView ivLess=helper.getView(R.id.iv_less);
        ImageView ivPlus=helper.getView(R.id.iv_plus);
        TextView tvCount=helper.getView(R.id.tv_count);
        if (item.getCount() > 0) {
            ivLess.setVisibility(View.VISIBLE);
            tvCount.setVisibility(View.VISIBLE);
            tvCount.setText(String.valueOf(item.getCount()));
        } else {
            ivLess.setVisibility(View.INVISIBLE);
            tvCount.setVisibility(View.INVISIBLE);
        }

        //添加商品
        ivPlus.setOnClickListener(v -> {
            if (item.getSpecialList().size() <=1
                    && item.getAttrList().size() == 0) {
                mListener.plusOne(item.getGId(), item.getGName(), item.getSpecialList().get(0).getSId(), item.getSpecialList().get(0).getName(),
                        "", "", item.getPrice(), "1", item.getGImg(),ivPlus);
            } else {
                AddShopCartDialog dialog = new AddShopCartDialog(getContext());
                dialog.setData(item.getGName(), getSpecialList(item.getSpecialList()), getAttrList(item.getAttrList()));
                dialog.setOnSelectedListener((seId, seName, aIds, aNames, price) -> {
                    mListener.plusOne(item.getGId(), item.getGName(), seId, seName,
                            aIds, aNames, price, "1", item.getGImg(),ivPlus);
                    dialog.dismiss();
                });
                dialog.show();
            }
        });
        //移除商品
        ivLess.setOnClickListener(v -> {
            boolean hasAttr=false;
            if (item.getSpecialList().size() > 0 || item.getAttrList().size() > 0) {
                if (item.getCount() > 1) {
                    CommonToast.toast(getContext().getResources().getString(R.string.goods_less_tips));
                    return;
                }else {
                    hasAttr=true;
                }
            }
            item.setCount(0);
            mListener.lessOne(item.getGId(), item.getGName(), "", "",
                    "", "", item.getPrice(), "-1", item.getGImg(),hasAttr);
        });
    }

    public void setOnAddShoppingCartListener(OnAddShoppingCartListener listener) {
        mListener = listener;
    }

    public interface OnAddShoppingCartListener {

        void plusOne(String id, String gName, String seId, String seName,
                     String aIds, String aNames, String price, String num, String img,ImageView ivAdd);

        void lessOne(String id, String gName, String seId, String seName,
                     String aIds, String aNames, String price, String num, String img,boolean hasAttr);

    }

    private List<StoreGoodsBean.CateListBean.GoodsListBean.SpecialListBean> getSpecialList(
            List<GoodsSearchBean.GoodsListBean.SpecialListBean> specialList){
        List<StoreGoodsBean.CateListBean.GoodsListBean.SpecialListBean> list=new ArrayList<>();
        for (int i=0;i<specialList.size();i++){
            StoreGoodsBean.CateListBean.GoodsListBean.SpecialListBean bean=new StoreGoodsBean.CateListBean.GoodsListBean.SpecialListBean();
            bean.setSId(specialList.get(i).getSId());
            bean.setPrice(specialList.get(i).getPrice());
            bean.setName(specialList.get(i).getName());
            bean.setCurrentNum(specialList.get(i).getCurrentNum());
            list.add(bean);
        }
        return list;
    }

    private List<StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean> getAttrList(
            List<GoodsSearchBean.GoodsListBean.AttrListBean> attrList){
        List<StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean> list=new ArrayList<>();
        for (int i=0;i<attrList.size();i++){
            StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean bean=new StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean();
            bean.setName(attrList.get(i).getName());
            bean.setGamId(attrList.get(i).getGamId());
            List<StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean.OptListBean> optList=new ArrayList<>();
            for (int j=0;j<attrList.get(i).getOptList().size();j++){
                StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean.OptListBean optListBean
                        =new StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean.OptListBean();
                optListBean.setName(attrList.get(i).getOptList().get(j).getName());
                optListBean.setAId(attrList.get(i).getOptList().get(j).getAId());
                optList.add(optListBean);
            }
            bean.setOptList(optList);
            list.add(bean);
        }
        return list;
    }
}
