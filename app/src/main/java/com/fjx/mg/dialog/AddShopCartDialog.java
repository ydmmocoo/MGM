package com.fjx.mg.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.fjx.mg.R;
import com.fjx.mg.food.adapter.LvAttributeAdapter;
import com.fjx.mg.food.adapter.SpecialTagAdapter;
import com.fjx.mg.view.flowlayout.FlowLayout;
import com.fjx.mg.view.flowlayout.TagAdapter;
import com.fjx.mg.view.flowlayout.TagFlowLayout;
import com.library.common.utils.DimensionUtil;
import com.library.repository.models.StoreGoodsBean;
import com.tencent.qcloud.uikit.common.component.picture.listener.OnSelectedListener;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddShopCartDialog extends Dialog {

    @BindView(R.id.tv_name)
    TextView mTvName;
    TextView mTvSpecifications;
    TagFlowLayout mFlSpecifications;
    @BindView(R.id.lv_attribute)
    ListView mLvAttribute;
    @BindView(R.id.tv_price)
    TextView mTvPrice;

    private String mName;
    private LvAttributeAdapter mAdapter;
    private List<StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean> mAttrList;
    private SpecialTagAdapter mSpecialTagAdapter;
    private List<StoreGoodsBean.CateListBean.GoodsListBean.SpecialListBean> mSpecialList;

    private OnSelectedListener mOnSelectedListener;

    private String seId="";
    private String seName="";
    private String aIds="";
    private String aNames="";
    private String price="";

    public AddShopCartDialog(Context context) {
        super(context, R.style.CustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fragment_add_shop_cart);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = (int) (DimensionUtil.getScreenWith()*0.9);
        getWindow().setAttributes(params);

        ButterKnife.bind(this);
        View headView = View.inflate(getContext(), R.layout.item_lv_attribute_header, null);
        mTvSpecifications = headView.findViewById(R.id.tv_specifications);
        mFlSpecifications = headView.findViewById(R.id.fl_specifications);
        if (mSpecialList!=null&&mSpecialList.size()>1) {
            mLvAttribute.addHeaderView(headView);
        }
        seId=mSpecialList.get(0).getSId();
        seName=mSpecialList.get(0).getName();
        price=mSpecialList.get(0).getPrice();

        mTvName.setText(mName);
        mSpecialTagAdapter = new SpecialTagAdapter(getContext(), mSpecialList);
        mFlSpecifications.setAdapter(mSpecialTagAdapter);
        //mSpecialTagAdapter.setSelected(0,mSpecialList.get(0));
        mSpecialTagAdapter.setSelectedList(0);
        mTvPrice.setText(getContext().getResources().getString(R.string.goods_price,
                mSpecialList.get(0).getPrice()));
        mAdapter = new LvAttributeAdapter(getContext(), mAttrList);
        mLvAttribute.setAdapter(mAdapter);

        //规格点击事件
        mFlSpecifications.setOnTagClickListener((view, position, parent) -> {
            mSpecialTagAdapter.setSelectedList(position);
            mTvPrice.setText(getContext().getResources().getString(R.string.goods_price,
                    mSpecialList.get(position).getPrice()));
            seId=mSpecialList.get(position).getSId();
            seName=mSpecialList.get(position).getName();
            price=mSpecialList.get(position).getPrice();
            return true;
        });
    }

    @OnClick({R.id.iv_closed, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_closed://关闭
                dismiss();
                break;
            case R.id.tv_confirm://选好了
                aIds=mAdapter.getIds();
                aNames=mAdapter.getNames();
                mOnSelectedListener.selected(seId,seName,aIds,aNames,price);
                break;
        }
    }

    public void setData(String name,
                        List<StoreGoodsBean.CateListBean.GoodsListBean.SpecialListBean> specialList,
                        List<StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean> list) {
        mName = name;
        mSpecialList = specialList;
        mAttrList = list;
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        mOnSelectedListener = listener;
    }

    public interface OnSelectedListener {
        void selected(String seId, String seName, String aIds, String aNames,String price);
    }
}