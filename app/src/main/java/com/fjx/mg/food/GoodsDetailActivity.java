package com.fjx.mg.food;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.dialog.AddShopCartDialog;
import com.fjx.mg.food.adapter.RvEvaluateAdapter;
import com.fjx.mg.food.adapter.RvShopCartAdapter;
import com.fjx.mg.food.contract.GoodsDetailContract;
import com.fjx.mg.food.presenter.GoodsDetailPresenter;
import com.fjx.mg.utils.AnimUtils;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.view.bottomsheet.BottomSheetLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.DimensionUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.GoodsDetailBean;
import com.library.repository.models.ShopingCartBean;
import com.library.repository.models.StoreEvaluateBean;
import com.library.repository.models.StoreGoodsBean;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsDetailActivity extends BaseMvpActivity<GoodsDetailPresenter>
        implements GoodsDetailContract.View, RvShopCartAdapter.OnShoppingCartEditListener {

    @BindView(R.id.iv_goods_pic)
    ImageView mIvGoodsPic;
    @BindView(R.id.tv_goods_name)
    TextView mTvGoodsName;
    @BindView(R.id.tv_monthly_sales)
    TextView mTvMonthlySales;
    @BindView(R.id.tv_goods_price)
    TextView mTvGoodsPrice;
    @BindView(R.id.tv_goods_count)
    TextView mTvGoodsCount;
    @BindView(R.id.iv_less)
    ImageView mIvLess;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.bottomSheetLayout)
    BottomSheetLayout mBottomSheetLayout;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_condition)
    TextView mTvCondition;
    @BindView(R.id.tv_bottom_delivery_fee)
    TextView mTvBottomDeliveryFee;
    TextView mTvContent;
    TextView mTvViewAll;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rl_main)
    RelativeLayout mRlMain;
    @BindView(R.id.iv_cart)
    ImageView mIvCart;
    @BindView(R.id.iv_plus)
    ImageView mIvPlus;

    private GoodsDetailBean mData;
    private RvEvaluateAdapter mEvaluateAdapter;
    private List<StoreEvaluateBean.EvaluateListBean> mEvaluateList;
    private boolean mHasNext;
    private int mPage = 1;

    private String mId;
    private String mGoodsId;
    private View mBottomSheet;
    private RvShopCartAdapter mAdapter;
    private List<ShopingCartBean.GoodsListBean> mList;

    private double mTotalPrice;
    private double mDeliveryConditions = 0;

    @Override
    protected int layoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .init();
        mId = getIntent().getStringExtra("id");
        mGoodsId = getIntent().getStringExtra("goods_id");

        mRefreshLayout.setEnableRefresh(false);

        //初始化RecyclerView
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRvContent.setLayoutManager(manager);
        mEvaluateAdapter = new RvEvaluateAdapter(R.layout.item_rv_evaluate, mEvaluateList);
        View headerView = View.inflate(this, R.layout.item_rv_goods_detail_header, null);
        mTvContent = headerView.findViewById(R.id.tv_content);
        mEvaluateAdapter.addHeaderView(headerView);
        View footerView = View.inflate(this, R.layout.item_rv_goods_detail_footer, null);
        mTvViewAll = footerView.findViewById(R.id.tv_view_all);
        mEvaluateAdapter.addFooterView(footerView);
        mRvContent.setAdapter(mEvaluateAdapter);

        //起送条件
        mTvCondition.setText(getResources().getString(R.string.delivery_conditions,
                "0"));

        //监听RecyclerView滑动,设置图片显示高度
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mIvGoodsPic.getLayoutParams();
        mRvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int mmRvScrollY = 0; // 列表滑动距离

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                mmRvScrollY += dy;
                if (DimensionUtil.dip2px(250) - mmRvScrollY > DimensionUtil.dip2px(69)) {
                    layoutParams.height = DimensionUtil.dip2px(250) - mmRvScrollY;
                } else {
                    layoutParams.height = DimensionUtil.dip2px(69);
                }
                mIvGoodsPic.setLayoutParams(layoutParams);
            }
        });
    }

    @OnClick({R.id.iv_closed, R.id.iv_plus, R.id.iv_less, R.id.iv_cart, R.id.tv_to_settle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_closed://返回
                finish();
                break;
            case R.id.iv_plus://增加商品
                if (!UserCenter.hasLogin()) {
                    new DialogUtil().showAlertDialog(getCurContext(), R.string.tips, R.string.not_login_forward_login, (dialog, which) -> {
                        dialog.dismiss();
                        UserCenter.goLoginActivity();
                    });
                    return;
                }
                if (mData.getGoodInfo().getSpecialList().size() <= 1
                        && mData.getGoodInfo().getAttrList().size() ==0) {
                    mPresenter.addShopCart(mId, mGoodsId, mData.getGoodInfo().getName(),
                            mData.getGoodInfo().getSpecialList().get(0).getSId(), mData.getGoodInfo().getSpecialList().get(0).getName(), "", "", mData.getGoodInfo().getPrice(), "1", mData.getGoodInfo().getImg());
                } else {
                    List<StoreGoodsBean.CateListBean.GoodsListBean.SpecialListBean> specialList = new ArrayList<>();
                    List<StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean> attrList = new ArrayList<>();
                    for (int i = 0; i < mData.getGoodInfo().getSpecialList().size(); i++) {
                        StoreGoodsBean.CateListBean.GoodsListBean.SpecialListBean bean = new StoreGoodsBean.CateListBean.GoodsListBean.SpecialListBean();
                        bean.setCurrentNum(mData.getGoodInfo().getSpecialList().get(i).getCurrentNum());
                        bean.setName(mData.getGoodInfo().getSpecialList().get(i).getName());
                        bean.setPrice(mData.getGoodInfo().getSpecialList().get(i).getPrice());
                        bean.setSId(mData.getGoodInfo().getSpecialList().get(i).getSId());
                        specialList.add(bean);
                    }
                    for (int i = 0; i < mData.getGoodInfo().getAttrList().size(); i++) {
                        StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean bean = new StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean();
                        bean.setGamId(mData.getGoodInfo().getAttrList().get(i).getGamId());
                        bean.setName(mData.getGoodInfo().getAttrList().get(i).getName());
                        List<StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean.OptListBean> optList = new ArrayList<>();
                        for (int j = 0; j < mData.getGoodInfo().getAttrList().get(i).getOptList().size(); j++) {
                            StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean.OptListBean optListBean = new StoreGoodsBean.CateListBean.GoodsListBean.AttrListBean.OptListBean();
                            optListBean.setAId(mData.getGoodInfo().getAttrList().get(i).getOptList().get(j).getAId());
                            optListBean.setName(mData.getGoodInfo().getAttrList().get(i).getOptList().get(j).getName());
                            optList.add(optListBean);
                        }
                        bean.setOptList(optList);
                        attrList.add(bean);
                    }
                    AddShopCartDialog dialog = new AddShopCartDialog(getCurContext());
                    dialog.setData(mData.getGoodInfo().getName(), specialList, attrList);
                    dialog.setOnSelectedListener((seId, seName, aIds, aNames, price) -> {
                        mPresenter.addShopCart(mId, mGoodsId, mData.getGoodInfo().getName(),
                                seId, seName, aIds, aNames, mData.getGoodInfo().getPrice(), "1", mData.getGoodInfo().getImg());
                        dialog.dismiss();
                    });
                    dialog.show();
                }
                break;
            case R.id.iv_less://减少商品
                if (!UserCenter.hasLogin()) {
                    new DialogUtil().showAlertDialog(getCurContext(), R.string.tips, R.string.not_login_forward_login, (dialog, which) -> {
                        dialog.dismiss();
                        UserCenter.goLoginActivity();
                    });
                    return;
                }

                if (mData.getGoodInfo().getCount()==1){
                    mIvLess.setVisibility(View.INVISIBLE);
                    mTvGoodsCount.setVisibility(View.INVISIBLE);
                }
                boolean hasAttr = false;
                if (mData.getGoodInfo().getSpecialList().size() > 1 || mData.getGoodInfo().getAttrList().size() > 0) {
                    if (mData.getGoodInfo().getCount() > 1) {
                        CommonToast.toast(getResources().getString(R.string.goods_less_tips));
                        return;
                    } else {
                        hasAttr = true;
                    }
                }

                String seId = "";
                String seName = "";
                String aIds = "";
                String aNames = "";
                String price = mData.getGoodInfo().getPrice();
                if (hasAttr) {
                    for (int i = 0; i < mList.size(); i++) {
                        if (mGoodsId.equals(mList.get(i).getGId())) {
                            seId = mList.get(i).getSeId();
                            seName = mList.get(i).getSeName();
                            aIds = mList.get(i).getAIds();
                            aNames = mList.get(i).getANames();
                            price = mList.get(i).getPirce();
                        }
                    }
                }else {
                    seId = mData.getGoodInfo().getSpecialList().get(0).getSId();
                    seName = mData.getGoodInfo().getSpecialList().get(0).getName();
                }
                mPresenter.addShopCart(mId, mGoodsId, mData.getGoodInfo().getName(), seId, seName, aIds, aNames, price, "-1", mData.getGoodInfo().getImg());
                break;
            case R.id.iv_cart://购物车
                showBottomSheet();
                break;
            case R.id.tv_to_settle://去结算
                if (!UserCenter.hasLogin()) {
                    new DialogUtil().showAlertDialog(getCurContext(), R.string.tips, R.string.not_login_forward_login, (dialog, which) -> {
                        dialog.dismiss();
                        UserCenter.goLoginActivity();
                    });
                    return;
                }
                Intent intent = new Intent(getCurContext(), ShoppingInfoActivity.class);
                intent.putExtra("id", mId);
                startActivity(intent);
                break;
        }
    }

    private View createBottomSheetView() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_shop_cart_bottom_sheet, (ViewGroup) getWindow().getDecorView(), false);
        RecyclerView rvGoods = view.findViewById(R.id.rv_goods);
        rvGoods.setLayoutManager(new LinearLayoutManager(this));
        TextView tvClear = view.findViewById(R.id.tv_clear);

        mAdapter = new RvShopCartAdapter(R.layout.item_shop_cart, mList);
        mAdapter.setOnShoppingCartEditListener(this);
        rvGoods.setAdapter(mAdapter);
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
            if (mList.size() != 0) {
                mBottomSheetLayout.showWithSheetView(mBottomSheet);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getGoodsInfo(mGoodsId);
        mPresenter.getEvaluateList(mId, "", mPage);
    }

    @Override
    public void getGoodsInfoSuccess(GoodsDetailBean data) {
        mData = data;
        if (UserCenter.hasLogin()) {
            mPresenter.getShopCartData(mId);
        }
        if (mData.getGoodInfo()!=null) {
            //设置商品图片
            CommonImageLoader.load(mData.getGoodInfo().getImg())
                    .placeholder(R.drawable.food_default).into(mIvGoodsPic);
            //设置商品名称
            mTvGoodsName.setText(mData.getGoodInfo().getName());
            //设置商品价格
            mTvGoodsPrice.setText(getResources().getString(R.string.goods_price, mData.getGoodInfo().getPrice()));
            //设置月售
            mTvMonthlySales.setText(getResources().getString(R.string.monthly_sales, String.valueOf(mData.getGoodInfo().getSaleCount())));
            //设置商品详情
            mTvContent.setText(mData.getGoodInfo().getDesc());
        }else {
            mPresenter.getGoodsInfo(mGoodsId);
            mPresenter.getEvaluateList(mId, "", mPage);
        }
    }

    @Override
    public void getShopCartDataSuccess(ShopingCartBean data) {
        mList = data.getGoodsList();
        if (mAdapter != null) {
            mAdapter.setList(mList);
        }
        setData(data);
        int count = 0;
        for (int i = 0; i < mList.size(); i++) {
            if (mGoodsId.equals(mList.get(i).getGId())) {
                count = count + Integer.parseInt(mList.get(i).getNum());
            }
        }
        if (mData!=null&&mData.getGoodInfo()!=null) {
            mData.getGoodInfo().setCount(count);
        }
        if (count > 0) {
            mTvGoodsCount.setVisibility(View.VISIBLE);
            mTvGoodsCount.setText(String.valueOf(count));
            mIvLess.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void addShopCartSuccess(boolean isAdd) {
        if (isAdd) {
            showAddShopCartAnim();
            showCartAnimation();
        }
        mPresenter.getShopCartData(mId);
    }

    @Override
    public void clearShopCartSuccess() {
        mTvCount.setVisibility(View.GONE);
        mList.clear();
        mTvPrice.setText(getResources().getString(R.string.no_purchase));
        mTvCondition.setText(getResources().getString(R.string.delivery_conditions,
                String.valueOf(mDeliveryConditions)));
        if (mBottomSheetLayout.isSheetShowing()) {
            mAdapter.setList(mList);
            mBottomSheetLayout.dismissSheet();
        }
        mData.getGoodInfo().setCount(0);
        mTvGoodsCount.setVisibility(View.INVISIBLE);
        mTvGoodsCount.setText("0");
        mIvLess.setVisibility(View.INVISIBLE);
    }

    @Override
    public void getEvaluateListSuccess(StoreEvaluateBean data) {
        mHasNext = data.isHasNext();
        if (mHasNext) {
            mRefreshLayout.setEnableLoadMore(false);
            mRefreshLayout.setNoMoreData(true);
        }
        if (mPage == 1) {
            mEvaluateList = data.getEvaluateList();
        } else {
            mEvaluateList.addAll(data.getEvaluateList());
        }
        mEvaluateAdapter.setList(mEvaluateList);
    }

    @Override
    protected GoodsDetailPresenter createPresenter() {
        return new GoodsDetailPresenter(this);
    }

    public void setData(ShopingCartBean data) {
        if (data != null) {
            if (data.getTotalNum()==0){
                mTvCount.setVisibility(View.GONE);
            }else {
                mTvCount.setVisibility(View.VISIBLE);
                mTvCount.setText(String.valueOf(data.getTotalNum()));
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
    public void editShopCart(String gId, String gName, String seId, String seName, String aIds,
                             String aNames, String price, String num, String img) {
        mPresenter.addShopCart(mId, gId, gName, seId, seName, aIds, aNames, price, num, img);
    }

    public void showAddShopCartAnim() {
        AnimUtils.AddToShopAnim(mIvPlus, mIvCart, GoodsDetailActivity.this, mRlMain);
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