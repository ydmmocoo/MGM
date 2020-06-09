package com.fjx.mg.food;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.fjx.mg.R;
import com.fjx.mg.food.adapter.RvShopCartAdapter;
import com.fjx.mg.food.adapter.ShopFullReductionTagAdapter;
import com.fjx.mg.food.contract.StoreDetailContract;
import com.fjx.mg.food.presenter.StoreDetailPresenter;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.utils.AnimUtils;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.view.RoundImageView;
import com.fjx.mg.view.bottomsheet.BottomSheetLayout;
import com.fjx.mg.view.flowlayout.TagFlowLayout;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.library.common.base.BaseMvpActivity;
import com.library.common.pop.DensityUtil;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.view.BannerView;
import com.library.repository.data.UserCenter;
import com.library.repository.models.ShopingCartBean;
import com.library.repository.models.StoreShopInfoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 店铺详情
 */
public class StoreDetailActivity extends BaseMvpActivity<StoreDetailPresenter>
        implements StoreDetailContract.View, RvShopCartAdapter.OnShoppingCartEditListener {

    @BindView(R.id.iv_store_logo)
    RoundImageView mIvStoreLogo;
    @BindView(R.id.tv_store_name)
    TextView mTvStoreName;
    @BindView(R.id.tv_score)
    TextView mTvScore;
    @BindView(R.id.tv_monthly_sales)
    TextView mTvMonthlySales;
    @BindView(R.id.tv_delivery_fee)
    TextView mTvDeliveryFee;
    @BindView(R.id.tv_shop_full_reduction)
    TextView mTvShopFullReduction;
    @BindView(R.id.iv_like)
    ImageView mIvLike;
    @BindView(R.id.tab)
    TabLayout mTab;
    @BindView(R.id.vp)
    ViewPager mVp;
    @BindView(R.id.bottomSheetLayout)
    BottomSheetLayout mBottomSheetLayout;
    @BindView(R.id.iv_cart)
    ImageView mIvCart;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_to_settle)
    TextView mTvToSettle;
    @BindView(R.id.tv_resting)
    TextView mTvResting;
    @BindView(R.id.tv_condition)
    TextView mTvCondition;
    @BindView(R.id.fl_shop_full_reduction)
    TagFlowLayout mFlShopFullReduction;
    @BindView(R.id.banner)
    BannerView mBanner;
    @BindView(R.id.cv_banner)
    CardView mCvBanner;
    @BindView(R.id.tv_bottom_delivery_fee)
    TextView mTvBottomDeliveryFee;
    @BindView(R.id.title_bar)
    Toolbar mTitleBar;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.iv_store_bg)
    ImageView mIvStoreBg;
    @BindView(R.id.container_layout)
    RelativeLayout mContainerLayout;

    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();
    private StoreGoodsFragment mStoreGoodsFragment;
    private StoreEvaluateFragment mStoreEvaluateFragment;
    private StoreMerchantInfoFragment mStoreMerchantInfoFragment;
    private RvShopCartAdapter mAdapter;
    private List<ShopingCartBean.GoodsListBean> mList;
    private View mBottomSheet;
    private String mId;
    private double mDeliveryConditions;
    private double mTotalPrice;
    private StoreShopInfoBean mData;
    private boolean mIsCollect = false;

    @Override
    protected int layoutId() {
        return R.layout.activity_store_detail;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this)
                .titleBar(mTitleBar, false)
                .fitsSystemWindows(false)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .init();
        mId = getIntent().getStringExtra("id");
        mStoreGoodsFragment = new StoreGoodsFragment();
        mStoreEvaluateFragment = new StoreEvaluateFragment();
        mStoreMerchantInfoFragment = new StoreMerchantInfoFragment();
        mFragments.add(mStoreGoodsFragment);
        mFragments.add(mStoreEvaluateFragment);
        mFragments.add(mStoreMerchantInfoFragment);
        mStoreGoodsFragment.setId(mId);
        mStoreEvaluateFragment.setId(mId);

        mTitles.add(getResources().getString(R.string.order));
        mTitles.add(getResources().getString(R.string.evaluate));
        mTitles.add(getResources().getString(R.string.merchant));
        TabFragmentAdapter tabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());
        mVp.setAdapter(tabFragmentAdapter);
        mTab.setupWithViewPager(mVp);

        mPresenter.getShopInfo(mId);

        setListener();
    }

    private void setListener() {
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                /*switch (position) {
                    case 0:
                        if (mIsResting) {
                            mShopCartMain.setVisibility(View.GONE);
                            mTvResting.setVisibility(View.VISIBLE);
                        } else {
                            mShopCartMain.setVisibility(View.VISIBLE);
                            mTvResting.setVisibility(View.GONE);
                        }
                        break;
                    case 1:
                        mShopCartMain.setVisibility(View.GONE);
                        if (mTvResting.getVisibility() == View.VISIBLE) {
                            mTvResting.setVisibility(View.GONE);
                        }
                        break;
                    case 2:
                        mShopCartMain.setVisibility(View.GONE);
                        if (mTvResting.getVisibility() == View.VISIBLE) {
                            mTvResting.setVisibility(View.GONE);
                        }
                        break;
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //监听页面滑动
        mAppbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int height = DensityUtil.dip2px(StoreDetailActivity.this, 184);
            /*if (mIsResting) {
                mShopCartMain.setBackgroundColor(ContextCompat.getColor(getCurContext(), R.color.cart_transparent));
            } else {
                mTvResting.setBackgroundColor(ContextCompat.getColor(getCurContext(), R.color.cart_transparent));
            }*/
            if (verticalOffset == 0) {
                mTitleBar.setBackgroundColor(Color.argb(0, 209, 41, 52));
            } else if (Math.abs(verticalOffset) / height > 0 && Math.abs(verticalOffset) / height < 1) {
                mTitleBar.setBackgroundColor(Color.argb(255 * (Math.abs(verticalOffset) / height), 209, 41, 52));
            } else {
                mTitleBar.setBackgroundColor(Color.argb(255, 209, 41, 52));
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_search, R.id.iv_like, R.id.iv_cart, R.id.tv_to_settle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_search://搜索
                startActivity(GoodsSearchActivity.newInstance(getCurContext()));
                break;
            case R.id.iv_like://收藏
                if (UserCenter.hasLogin()) {
                    if (mIsCollect) {
                        mPresenter.cancelCollect(mId);
                    } else {
                        mPresenter.collect(mId);
                    }
                } else {
                    new DialogUtil().showAlertDialog(getCurContext(), R.string.tips, R.string.not_login_forward_login, (dialog, which) -> {
                        dialog.dismiss();
                        UserCenter.goLoginActivity();
                    });
                }
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

    public void getShopCartData() {
        mPresenter.getShopCartData(mId);
    }

    public void setData(ShopingCartBean data) {
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
    public void getShopInfoSuccess(StoreShopInfoBean data) {
        mData = data;
        //是否收藏过
        mIsCollect = data.getShopInfo().isCollect();
        if (mIsCollect) {
            mIvLike.setImageResource(R.mipmap.icon_collection_selected);
        } else {
            mIvLike.setImageResource(R.mipmap.icon_collection);
        }
        //店铺名称
        mTvStoreName.setText(data.getShopInfo().getShopName());
        //设置店铺logo图片
        CommonImageLoader.load(data.getShopInfo().getShopLogo())
                .placeholder(R.drawable.food_default).into(mIvStoreLogo);
        //设置评分
        mTvScore.setText(data.getShopInfo().getAvgScore());
        //设置月售
        mTvMonthlySales.setText(getResources().getString(R.string.monthly_sales,
                String.valueOf(data.getShopInfo().getSaleNum())));
        //设置配送费
        if ("0".equals(data.getShopInfo().getDistributionFee())) {
            mTvDeliveryFee.setText(getResources().getString(R.string.free_delivery_fee));
            mTvBottomDeliveryFee.setText(getResources().getString(R.string.free_delivery_fee));
        } else {
            mTvDeliveryFee.setText(getResources().getString(R.string.delivery_fee,
                    data.getShopInfo().getDistributionFee()));
            mTvBottomDeliveryFee.setText(getResources().getString(R.string.delivery_fee,
                    data.getShopInfo().getDistributionFee()));
        }
        //设置满减
        List<String> list = new ArrayList<>();
        if ("0".equals(data.getShopInfo().getFirstReduction())) {
            list.add(getResources().getString(R.string.first_reduction,
                    data.getShopInfo().getFirstReduction()));
        }
        for (int i = 0; i < data.getShopInfo().getReductionList().size(); i++) {
            list.add(getResources().getString(R.string.full_reduction,
                    data.getShopInfo().getReductionList().get(i).getFullPrice(),
                    data.getShopInfo().getReductionList().get(i).getPrice()));
        }
        ShopFullReductionTagAdapter adapter = new ShopFullReductionTagAdapter(getCurContext(), list);
        mFlShopFullReduction.setAdapter(adapter);
        if (list.size() == 0) {
            mTvShopFullReduction.setVisibility(View.GONE);
            mFlShopFullReduction.setVisibility(View.GONE);
        }
        //设置是否显示店铺轮播
        if (TextUtils.isEmpty(data.getShopInfo().getDecorationInfo().getAdImg())) {
            mCvBanner.setVisibility(View.GONE);
        } else {
            mCvBanner.setVisibility(View.VISIBLE);
            List<String> imgList = new ArrayList<>();
            imgList.add(data.getShopInfo().getDecorationInfo().getAdImg());
            mBanner.showImages(imgList);
        }
        //设置是否显示招报
        if (!TextUtils.isEmpty(data.getShopInfo().getDecorationInfo().getBgImg())) {
            CommonImageLoader.load(data.getShopInfo().getDecorationInfo().getBgImg())
                    .placeholder(R.mipmap.icon_shop_bg)
                    .error(R.mipmap.icon_shop_bg)
                    .into(mIvStoreBg);
        }
        //获取配送条件
        mDeliveryConditions = Double.parseDouble(data.getShopInfo().getDeliveryPrice());
        //设置商家信息
        mStoreMerchantInfoFragment.setData(mData);
    }

    @Override
    public void getShopCartDataSuccess(ShopingCartBean data) {
        mList = data.getGoodsList();
        if (mBottomSheetLayout.isSheetShowing()) {
            mAdapter.setList(mList);
        }
        setData(data);
        mStoreGoodsFragment.getShopCartData(mList);
    }

    @Override
    public void addShopCartSuccess() {
        getShopCartData();
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
        mStoreGoodsFragment.clearCount();
    }

    @Override
    public void collectSuccess() {
        mIsCollect = true;
        mIvLike.setImageResource(R.mipmap.icon_collection_selected);
    }

    @Override
    public void cancelCollectSuccess() {
        mIsCollect = false;
        mIvLike.setImageResource(R.mipmap.icon_collection);
    }

    @Override
    protected StoreDetailPresenter createPresenter() {
        return new StoreDetailPresenter(this);
    }

    @Override
    public void editShopCart(String gId, String gName, String seId, String seName,
                             String aIds, String aNames, String price, String num, String img) {
        mPresenter.addShopCart(mId, gId, gName, seId, seName, aIds, aNames, price, num, img);
    }

    class TabFragmentAdapter extends FragmentPagerAdapter {

        public TabFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position % mTitles.size());
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
                mAdapter.setList(mList);
            }
        }
    }

    public void showAddShopCartAnim(ImageView ivAdd) {
        AnimUtils.AddToShopAnim(ivAdd, mIvCart, StoreDetailActivity.this, mContainerLayout);
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
