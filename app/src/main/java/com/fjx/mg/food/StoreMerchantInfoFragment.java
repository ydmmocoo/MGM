package com.fjx.mg.food;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.food.adapter.RvRealPicAdapter;
import com.library.common.base.BaseFragment;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.StoreShopInfoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author yedeman
 * @date 2020/5/25.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class StoreMerchantInfoFragment extends BaseFragment {

    @BindView(R.id.tv_category)
    TextView mTvCategory;
    @BindView(R.id.tv_merchant_address)
    TextView mTvMerchantAddress;
    @BindView(R.id.tv_merchant_phone)
    TextView mTvMerchantPhone;
    @BindView(R.id.tv_business_hours)
    TextView mTvBusinessHours;
    @BindView(R.id.rv_real_pic)
    RecyclerView mRvRealPic;

    private RvRealPicAdapter mAdapter;
    private List<String> mPicList;
    private  StoreShopInfoBean mData;
    private ArrayList<String> mList = new ArrayList<>();

    @Override
    protected int layoutId() {
        return R.layout.fragment_store_merchant_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //初始化店铺真实图片RecyclerView
        LinearLayoutManager manager=new LinearLayoutManager(getCurContext());
        manager.setOrientation(RecyclerView.HORIZONTAL);
        mRvRealPic.setLayoutManager(manager);
        mAdapter=new RvRealPicAdapter(R.layout.item_rv_real_pic,mPicList);
        mRvRealPic.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置类别名称
        mTvCategory.setText(mData.getShopInfo().getTypeName());
        //设置商家地址
        mTvMerchantAddress.setText(mData.getShopInfo().getAddress());
        //设置商家电话
        if (mData.getShopInfo().getTels()!=null&&mData.getShopInfo().getTels().size()!=0) {
            mTvMerchantPhone.setText(mData.getShopInfo().getTel());
        }
        //设置营业时间
        if (mData.getShopInfo().getIsAll().equals("1")){
            mTvBusinessHours.setText(getResources().getString(R.string.open_24_hours_a_day));
        }else {
            if (mData.getShopInfo().getOpenTimeList().size() > 0){
                String openTime="";
                for (int i=0;i<mData.getShopInfo().getOpenTimeList().size();i++){
                    if (i==0){
                        openTime=mData.getShopInfo().getOpenTimeList().get(i).getOpenTime();
                    }else {
                        if (i%2==0) {
                            openTime = openTime.concat(" ")
                                    .concat(mData.getShopInfo().getOpenTimeList().get(i).getOpenTime());
                        }else {
                            openTime = openTime.concat("-")
                                    .concat(mData.getShopInfo().getOpenTimeList().get(i).getOpenTime());
                        }
                    }
                }
                mTvBusinessHours.setText(openTime);
            }else {
                mTvBusinessHours.setText(getResources().getString(R.string.unsetting));
            }
        }
        //设置实景图片
        if (mData.getShopInfo().getShopImgs().size() > 0) {
            mPicList=mData.getShopInfo().getShopImgs();
            mAdapter.setList(mPicList);
        } else {
            mRvRealPic.setVisibility(View.GONE);
        }
        //设置商家资质
        mList.clear();
        for (int i = 0; i < mData.getShopInfo().getShopCertImg().size(); i++) {
            mList.add(mData.getShopInfo().getShopCertImg().get(i));
        }
    }

    @OnClick(R.id.tv_business_qualification)
    public void onViewClicked() {//商家资质
        Intent intent = new Intent(getCurContext(), BusinessQualificationActivity.class);
        intent.putStringArrayListExtra("img", mList);
        startActivity(intent);
    }

    public void setData(StoreShopInfoBean data) {
        mData=data;
    }
}
