package com.fjx.mg.food;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.food.adapter.LvStoreGoodsAdapter;
import com.fjx.mg.food.adapter.RvTypeAdapter;
import com.fjx.mg.food.contract.StoreGoodsContract;
import com.fjx.mg.food.model.bean.StoreGoodsGroupBean;
import com.fjx.mg.food.presenter.StoreGoodsPresenter;
import com.library.common.base.BaseMvpFragment;
import com.library.common.view.stickylistheaders.StickyListHeadersListView;
import com.library.repository.models.ShopingCartBean;
import com.library.repository.models.StoreGoodsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author yedeman
 * @date 2020/5/25.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class StoreGoodsFragment extends BaseMvpFragment<StoreGoodsPresenter>
        implements StoreGoodsContract.View, LvStoreGoodsAdapter.OnAddShoppingCartListener {

    @BindView(R.id.rv_type)
    RecyclerView mRvType;
    @BindView(R.id.lv_content)
    StickyListHeadersListView mLvContent;

    private RvTypeAdapter mTypeAdapter;
    private List<StoreGoodsGroupBean> mGroupList = new ArrayList<>();
    private LvStoreGoodsAdapter mGoodsAdapter;
    private List<StoreGoodsBean.CateListBean.GoodsListBean> mGoodsList = new ArrayList<>();
    private String mId;
    private List<ShopingCartBean.GoodsListBean> mShopCartList;
    private StoreDetailActivity mActivity;
    private boolean mClickType=false;

    @Override
    protected int layoutId() {
        return R.layout.fragment_store_goods;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (StoreDetailActivity) getActivity();
        //初始化分类
        LinearLayoutManager manager = new LinearLayoutManager(getCurContext());
        mRvType.setLayoutManager(manager);
        mTypeAdapter = new RvTypeAdapter(R.layout.item_rv_goods_type, mGroupList);
        mTypeAdapter.setHasStableIds(true);
        mRvType.setAdapter(mTypeAdapter);
        //初始化商品列表
        mGoodsAdapter = new LvStoreGoodsAdapter((StoreDetailActivity) getActivity(), mGoodsList);
        mLvContent.setAdapter(mGoodsAdapter);
        mGoodsAdapter.setOnAddShoppingCartListener(this);
        View footerView = View.inflate(getActivity(), R.layout.item_store_goods_footer, null);
        mLvContent.addFooterView(footerView);

        setListener();

        mPresenter.getGoodsData(mId);
    }

    @Override
    public void onResume() {
        super.onResume();

        mActivity.getShopCartData();
    }

    private void setListener() {
        //商品Item点击事件
        mLvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getCurContext(), GoodsDetailActivity.class);
                intent.putExtra("id", mId);
                intent.putExtra("goods_id", mGoodsList.get(position).getGId());
                startActivity(intent);
            }
        });
        //左边分类点击
        mTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            int goodsPos=getSelectedPosition(mGroupList.get(position).getGroupId());
            mClickType=true;
            mTypeAdapter.mSelectTypeId = mGroupList.get(position).getGroupId();
            mTypeAdapter.notifyDataSetChanged();
            mLvContent.setSelection(goodsPos);
        });
        //右边商品滑动监听
        mLvContent.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mGoodsList.size() == 0) {
                    return;
                }
                StoreGoodsBean.CateListBean.GoodsListBean item = mGoodsList.get(firstVisibleItem);
                if (!mClickType) {
                    if (mTypeAdapter.mSelectTypeId != item.getGroupId()) {
                        mTypeAdapter.mSelectTypeId = item.getGroupId();
                        mTypeAdapter.notifyDataSetChanged();
                        mRvType.smoothScrollToPosition(getSelectedGroupPosition(item.getGroupId()));
                    }
                }else {
                    mClickType=false;
                }
            }
        });
    }

    @Override
    public void getGoodsDataSuccess(List<StoreGoodsBean.CateListBean> data) {
        mGroupList = mPresenter.getGroupList(data);
        mTypeAdapter.setList(mGroupList);
        mActivity.getShopCartData();
        mGoodsList = mPresenter.getGoodsList(data);
        mGoodsAdapter.setData(mGoodsList);
    }

    @Override
    public void addShopCartSuccess(ImageView ivAdd) {
        if (ivAdd != null) {
            mActivity.showAddShopCartAnim(ivAdd);
        }
        mActivity.getShopCartData();
    }

    @Override
    protected StoreGoodsPresenter createPresenter() {
        return new StoreGoodsPresenter(this);
    }

    public void getShopCartData(List<ShopingCartBean.GoodsListBean> list) {
        mShopCartList = list;
        for (int i=0;i<mGoodsList.size();i++){
            mGoodsList.get(i).setCount(0);
        }
        for (int i = 0; i < mGoodsList.size(); i++) {
            if ("0".equals(mGoodsList.get(i).getCateId())){
                continue;
            }
            int count = 0;
            for (int j = 0; j < mShopCartList.size(); j++) {
                if (mShopCartList.get(j).getGId().equals(mGoodsList.get(i).getGId())) {
                    count = count + Integer.parseInt(mShopCartList.get(j).getNum());
                    mGoodsList.get(i).setCount(count);
                }
            }
        }
        for (int i = 0; i < mGroupList.size(); i++) {
            if ("0".equals(mGroupList.get(i).getCateId())){
                continue;
            }
            long count = 0;
            for (int j = 0; j < mGoodsList.size(); j++) {
                if (mGoodsList.get(j).getGroupId() == mGroupList.get(i).getGroupId()) {
                    count = count + mGoodsList.get(j).getCount();
                    mGroupList.get(i).setCount(count);
                }
            }
        }
        mTypeAdapter.setList(mGroupList);
        mGoodsAdapter.setData(mGoodsList);
    }

    public void clearCount(){
        for (int i = 0; i < mGoodsList.size(); i++) {
            mGoodsList.get(i).setCount(0);
        }
        for (int i = 0; i < mGroupList.size(); i++) {
            mGroupList.get(i).setCount(0);
        }
        mTypeAdapter.setList(mGroupList);
        mGoodsAdapter.setData(mGoodsList);
    }

    //根据类别id获取分类的Position 用于滚动左侧的类别列表
    public int getSelectedGroupPosition(long typeId) {
        for (int i = 0; i < mGroupList.size(); i++) {
            if (typeId == mGroupList.get(i).getGroupId()) {
                return i;
            }
        }
        return 0;
    }

    private int getSelectedPosition(long typeId) {
        int position = 0;
        for (int i = 0; i < mGoodsList.size(); i++) {
            if (mGoodsList.get(i).getGroupId() == typeId) {
                position = i;
                break;
            }
        }
        return position;
    }

    @Override
    public void plusOne(String id, String gName, String seId, String seName, String aIds, String aNames,
                        String price, String num, String img, ImageView ivAdd) {
        mPresenter.addShopCart(mId, id, gName, seId, seName, aIds, aNames, price, num, img, ivAdd);
    }

    @Override
    public void lessOne(String id, String gName, String seId, String seName, String aIds, String aNames,
                        String price, String num, String img, boolean hasAttr) {
        if (hasAttr) {
            for (int i = 0; i < mShopCartList.size(); i++) {
                if (id.equals(mShopCartList.get(i).getGId())) {
                    seId = mShopCartList.get(i).getSeId();
                    seName = mShopCartList.get(i).getSeName();
                    aIds = mShopCartList.get(i).getAIds();
                    aNames = mShopCartList.get(i).getANames();
                    price = mShopCartList.get(i).getPirce();
                }
            }
        }
        mPresenter.addShopCart(mId, id, gName, seId, seName, aIds, aNames, price, num, img, null);

        for (int i = 0; i < mGroupList.size(); i++) {
            long count = 0;
            for (int j = 0; j < mGoodsList.size(); j++) {
                if (mGoodsList.get(j).getGId().equals(id)){
                    mGoodsList.get(j).setCount(0);
                }
                if (mGoodsList.get(j).getGroupId() == mGroupList.get(i).getGroupId()) {
                    count = count + mGoodsList.get(j).getCount();
                    mGroupList.get(i).setCount(count);
                }
            }
        }
        mTypeAdapter.setList(mGroupList);
    }

    public void setId(String id) {
        mId = id;
    }
}
