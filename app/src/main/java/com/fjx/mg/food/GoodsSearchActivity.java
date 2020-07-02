package com.fjx.mg.food;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.food.adapter.RvGoodsSearchAdapter;
import com.fjx.mg.food.adapter.RvShopCartAdapter;
import com.fjx.mg.food.contract.GoodsSearchContract;
import com.fjx.mg.food.presenter.GoodsSearchPresenter;
import com.fjx.mg.utils.AnimUtils;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.view.bottomsheet.BottomSheetLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.SoftInputUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.GoodsSearchBean;
import com.library.repository.models.ShopingCartBean;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsSearchActivity extends BaseMvpActivity<GoodsSearchPresenter>
        implements GoodsSearchContract.View, RvGoodsSearchAdapter.OnAddShoppingCartListener,
        RvShopCartAdapter.OnShoppingCartEditListener {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.bottomSheetLayout)
    BottomSheetLayout mBottomSheetLayout;
    @BindView(R.id.iv_cart)
    ImageView mIvCart;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_condition)
    TextView mTvCondition;
    @BindView(R.id.tv_bottom_delivery_fee)
    TextView mTvBottomDeliveryFee;
    @BindView(R.id.container_layout)
    RelativeLayout mContainerLayout;

    private RvGoodsSearchAdapter mAdapter;
    private List<GoodsSearchBean.GoodsListBean> mList = new ArrayList<>();
    private String mId;
    private int mPage = 1;
    private String mContent;

    private RvShopCartAdapter mCartAdapter;
    private List<ShopingCartBean.GoodsListBean> mShopCartList;
    private View mBottomSheet;
    private double mDeliveryConditions;
    private double mTotalPrice;

    public static Intent newInstance(Context context, String id, double deliveryConditions) {
        Intent intent = new Intent(context, GoodsSearchActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("delivery_conditions", deliveryConditions);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_goods_search;
    }

    @Override
    protected void initView() {
        mId = getIntent().getStringExtra("id");
        mDeliveryConditions = getIntent().getDoubleExtra("delivery_conditions", 0);
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .init();
        //初始化RecyclerView
        LinearLayoutManager manager = new LinearLayoutManager(getCurContext());
        mRvContent.setLayoutManager(manager);
        mAdapter = new RvGoodsSearchAdapter(R.layout.item_store_goods, mList);
        mAdapter.setOnAddShoppingCartListener(this);
        mRvContent.setAdapter(mAdapter);

        //下拉刷新，上拉加载更多
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPage++;
                mPresenter.getGoodsList(mId, mContent, mPage);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                mPresenter.getGoodsList(mId, mContent, mPage);
            }
        });

        mEtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if ((actionId == EditorInfo.IME_ACTION_SEARCH)) {//如果是搜索按钮
                mPage = 1;
                mContent=mEtSearch.getText().toString();
                mPresenter.getGoodsList(mId, mContent, mPage);
                SoftInputUtil.hideSoftInput(GoodsSearchActivity.this);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getShopCartData(mId);
    }

    @OnClick({R.id.iv_back, R.id.tv_search, R.id.iv_cart, R.id.tv_to_settle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_search://搜索
                mContent = mEtSearch.getText().toString();
                mPresenter.getGoodsList(mId, mContent, mPage);
                SoftInputUtil.hideSoftInput(getCurActivity());
                break;
            case R.id.iv_cart://购物车
                showBottomSheet();
                break;
            case R.id.tv_to_settle://去结算
                if (UserCenter.hasLogin()) {
                    if (mTvCount.getVisibility() == View.GONE) {
                        CommonToast.toast(getResources().getString(R.string.no_purchase));
                        return;
                    }
                    Intent intent = new Intent(getCurContext(), ShoppingInfoActivity.class);
                    intent.putExtra("id", mId);
                    startActivity(intent);
                } else {
                    new DialogUtil().showAlertDialog(getCurContext(), R.string.tips, R.string.not_login_forward_login, (dialog, which) -> {
                        dialog.dismiss();
                        UserCenter.goLoginActivity();
                    });
                }
                break;
        }
    }

    @Override
    public void getGoodsListSuccess(List<GoodsSearchBean.GoodsListBean> data, boolean hasNext) {
        if (hasNext) {
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.setEnableLoadMore(false);
        }
        if (mPage == 1) {
            mList = data;
            mRefreshLayout.finishRefresh();
        } else {
            mList.addAll(data);
            mRefreshLayout.finishLoadMore();
        }

        if (mShopCartList != null) {
            for (int i = 0; i < mList.size(); i++) {
                int count = 0;
                for (int j = 0; j < mShopCartList.size(); j++) {
                    if (mShopCartList.get(j).getGId().equals(mList.get(i).getGId())) {
                        count = count + Integer.parseInt(mShopCartList.get(j).getNum());
                        mList.get(i).setCount(count);
                    }
                }
            }
        }
        mAdapter.setList(mList);
    }

    @Override
    public void getShopCartDataSuccess(ShopingCartBean data) {
        mShopCartList = data.getGoodsList();
        for (int i = 0; i < mList.size(); i++) {
            int count = 0;
            for (int j = 0; j < mShopCartList.size(); j++) {
                if (mShopCartList.get(j).getGId().equals(mList.get(i).getGId())) {
                    count = count + Integer.parseInt(mShopCartList.get(j).getNum());
                    mList.get(i).setCount(count);
                }
            }
        }
        mAdapter.setList(mList);
        setData(data);
    }

    private void setData(ShopingCartBean data) {
        if (data != null) {
            if (data.getTotalNum() > 0) {
                mTvCount.setVisibility(View.VISIBLE);
                mTvCount.setText(String.valueOf(data.getTotalNum()));
            } else {
                mTvCount.setVisibility(View.GONE);
            }
            mTvPrice.setText(getResources().getString(R.string.goods_price,
                    String.valueOf(data.getTotalPrice())));
            mTotalPrice = data.getTotalPrice();
            if (mDeliveryConditions <= mTotalPrice) {
                mTvCondition.setText(getResources().getString(R.string.meet_delivery_conditions));
            } else {
                mTvCondition.setText(getResources().getString(R.string.delivery_conditions,
                        String.valueOf(mDeliveryConditions - mTotalPrice)));
            }
        } else {
            mTvCount.setVisibility(View.GONE);
            mTvPrice.setText(getResources().getString(R.string.no_purchase));
            mTvCondition.setText(getResources().getString(R.string.delivery_conditions,
                    String.valueOf(mDeliveryConditions)));
        }
    }

    @Override
    public void addShopCartSuccess(ImageView ivAdd) {
        if (ivAdd != null) {
            showAddShopCartAnim(ivAdd);
        }
        mPresenter.getShopCartData(mId);
    }

    @Override
    public void clearShopCartSuccess() {
        mTvCount.setVisibility(View.GONE);
        mShopCartList.clear();
        mTvPrice.setText(getResources().getString(R.string.no_purchase));
        mTvCondition.setText(getResources().getString(R.string.delivery_conditions,
                String.valueOf(mDeliveryConditions)));
        if (mBottomSheetLayout.isSheetShowing()) {
            mCartAdapter.setList(mShopCartList);
            mBottomSheetLayout.dismissSheet();
        }
        if (mList!=null) {
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setCount(0);
            }
        }
        mAdapter.setList(mList);
    }

    @Override
    protected GoodsSearchPresenter createPresenter() {
        return new GoodsSearchPresenter(this);
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
    }

    @Override
    public void editShopCart(String gId, String gName, String seId, String seName,
                             String aIds, String aNames, String price, String num, String img) {
        mPresenter.addShopCart(mId, gId, gName, seId, seName, aIds, aNames, price, num, img, null);
    }

    private View createBottomSheetView() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_shop_cart_bottom_sheet, (ViewGroup) getWindow().getDecorView(), false);
        RecyclerView rvGoods = view.findViewById(R.id.rv_goods);
        rvGoods.setLayoutManager(new LinearLayoutManager(this));
        TextView tvClear = view.findViewById(R.id.tv_clear);

        mCartAdapter = new RvShopCartAdapter(R.layout.item_shop_cart, mShopCartList);
        mCartAdapter.setOnShoppingCartEditListener(this);
        rvGoods.setAdapter(mCartAdapter);
        //清空购物车
        tvClear.setOnClickListener(v -> mPresenter.clearShopCart(mId));

        return view;
    }

    private void showBottomSheet() {
        if (mBottomSheet == null) {
            mBottomSheet = createBottomSheetView();
        }
        if (mBottomSheetLayout.isSheetShowing()) {
            mBottomSheetLayout.dismissSheet();
        } else {
            if (mShopCartList.size() != 0) {
                mBottomSheetLayout.showWithSheetView(mBottomSheet);
                mCartAdapter.setList(mShopCartList);
            }
        }
    }

    public void showAddShopCartAnim(ImageView ivAdd) {
        AnimUtils.AddToShopAnim(ivAdd, mIvCart, GoodsSearchActivity.this, mContainerLayout);
        showCartAnimation();
    }

    private void showCartAnimation() {
        ObjectAnimator scaleAnimatorX = new ObjectAnimator().ofFloat(mIvCart, "scaleX", 1.0f, 1.2f, 1.0f);
        ObjectAnimator scaleAnimatorY = new ObjectAnimator().ofFloat(mIvCart, "scaleY", 1.0f, 1.2f, 1.0f);
        scaleAnimatorX.setInterpolator(new AccelerateInterpolator());
        scaleAnimatorY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleAnimatorX).with(scaleAnimatorY);
        animatorSet.setDuration(800);
        animatorSet.start();
    }
}