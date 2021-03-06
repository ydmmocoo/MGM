package com.fjx.mg.food;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.fjx.mg.R;
import com.fjx.mg.dialog.DeliveryTimePop;
import com.fjx.mg.food.adapter.LvGoodsAdapter;
import com.fjx.mg.food.contract.ShoppingInfoContract;
import com.fjx.mg.food.presenter.ShoppingInfoPresenter;
import com.fjx.mg.setting.address.list.AddressListActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.SoftInputUtil;
import com.library.common.view.WrapContentListView;
import com.library.repository.data.UserCenter;
import com.library.repository.models.CreateOrderBean;
import com.library.repository.models.ShoppingInfoBean;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 结算信息页面
 */
public class ShoppingInfoActivity extends BaseMvpActivity<ShoppingInfoPresenter> implements ShoppingInfoContract.View {

    @BindView(R.id.tv_delivery)
    TextView mTvDelivery;
    @BindView(R.id.tv_collect_by_yourself)
    TextView mTvCollectByYourself;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.delivery_method)
    TextView mDeliveryMethod;
    @BindView(R.id.rl_delivery)
    RelativeLayout mRlDelivery;
    @BindView(R.id.tv_store_address)
    TextView mTvStoreAddress;
    @BindView(R.id.tv_distance)
    TextView mTvDistance;
    @BindView(R.id.tv_self_extracting_time)
    TextView mTvSelfExtractingTime;
    @BindView(R.id.tv_reserved_telephone)
    TextView mTvReservedTelephone;
    @BindView(R.id.rl_collect_by_yourself)
    RelativeLayout mRlCollectByYourself;
    @BindView(R.id.tv_store_name)
    TextView mTvStoreName;
    @BindView(R.id.lv_goods)
    WrapContentListView mLvGoods;
    @BindView(R.id.tv_packing_fee)
    TextView mTvPackingFee;
    @BindView(R.id.v_line_six)
    View mVLineSix;
    @BindView(R.id.tv_delivery_fee_text)
    TextView mTvDeliveryFeeText;
    @BindView(R.id.tv_delivery_fee)
    TextView mTvDeliveryFee;
    @BindView(R.id.tv_lucky_red_envelopes_coupons)
    TextView mTvLuckyRedEnvelopesCoupons;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_remark)
    TextView mTvRemark;
    @BindView(R.id.tv_total)
    TextView mTvTotal;
    @BindView(R.id.tv_pay)
    TextView mTvPay;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_delivery_other)
    TextView mTvDeliveryOther;
    @BindView(R.id.tab_bg)
    View mTabBg;
    @BindView(R.id.tv_shop_full_reduction)
    TextView mTvShopFullReduction;
    @BindView(R.id.tv_shop_full_reduction_text)
    TextView mTvShopFullReductionText;
    @BindView(R.id.v_line_eight)
    View mVLineEight;
    @BindView(R.id.tv_first_reduction_text)
    TextView mTvFirstReductionText;
    @BindView(R.id.tv_first_reduction)
    TextView mTvFirstReduction;
    @BindView(R.id.v_line)
    View mVLine;

    private String mId;
    private boolean mIsDelivery = true;

    private LvGoodsAdapter mAdapter;
    private List<ShoppingInfoBean.GoodsListBean> mList;
    private long mTotalPrice;
    private String mPhone;
    private String mCouponId;
    private String mCouponAmount="0";
    private String mAddressId;
    private String mRemark;
    private String mTimeOfDelivery;
    private String mSelfExtractingTime;
    private String mReservedPhone="";
    private String mDeliveryFee;
    private List<String> mTimeList;
    private List<String> mPhoneList=new ArrayList<>();
    private BasePopupView mReservedPhonePop;

    @Override
    protected int layoutId() {
        return R.layout.activity_shopping_info;
    }

    @Override
    protected void initView() {
        super.initView();
        mId = getIntent().getStringExtra("id");
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .transparentStatusBar()
                .init();

        //初始化商品列表
        mAdapter = new LvGoodsAdapter(getCurContext(), mList);
        mLvGoods.setAdapter(mAdapter);

        mReservedPhone= UserCenter.getUserInfo().getPhone();

        mPresenter.getShoppingInfo(mId);
    }

    @OnClick({R.id.iv_back, R.id.tv_delivery, R.id.tv_collect_by_yourself, R.id.v_address, R.id.tv_expected_delivery_time, R.id.tv_self_extracting_time, R.id.tv_reserved_telephone, R.id.tv_store_name, R.id.iv_call, R.id.tv_lucky_red_envelopes_coupons_text, R.id.tv_remark_text, R.id.tv_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_delivery://外卖配送
                mIsDelivery = true;
                mRlDelivery.setVisibility(View.VISIBLE);
                mRlCollectByYourself.setVisibility(View.GONE);
                mTvDelivery.setBackground(ContextCompat.getDrawable(getCurContext(), R.drawable.btn_white_bg));
                mTvDelivery.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                mTvDelivery.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
                mTvCollectByYourself.setBackground(null);
                mTvCollectByYourself.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                mTvCollectByYourself.setTextColor(ContextCompat.getColor(getCurContext(), R.color.white));

                mVLineSix.setVisibility(View.GONE);
                mTvDeliveryFeeText.setVisibility(View.GONE);
                mTvDeliveryFee.setVisibility(View.GONE);

                mVLineSix.setVisibility(View.GONE);
                mTvDeliveryFeeText.setVisibility(View.GONE);
                mTvDeliveryFee.setVisibility(View.GONE);
                mTvTotal.setText(getResources().getString(R.string.goods_price, String.valueOf(mTotalPrice - Integer.parseInt(mCouponAmount))));
                mTvTotalPrice.setText(getResources().getString(R.string.goods_price, String.valueOf(mTotalPrice - Integer.parseInt(mCouponAmount))));
                break;
            case R.id.tv_collect_by_yourself://到店自取
                mIsDelivery = false;
                mRlDelivery.setVisibility(View.GONE);
                mRlCollectByYourself.setVisibility(View.VISIBLE);
                mTvDelivery.setBackground(null);
                mTvDelivery.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                mTvDelivery.setTextColor(ContextCompat.getColor(getCurContext(), R.color.white));
                mTvCollectByYourself.setBackground(ContextCompat.getDrawable(getCurContext(), R.drawable.btn_white_bg));
                mTvCollectByYourself.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                mTvCollectByYourself.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));

                mVLineSix.setVisibility(View.GONE);
                mTvDeliveryFeeText.setVisibility(View.GONE);
                mTvDeliveryFee.setVisibility(View.GONE);
                mTvTotal.setText(getResources().getString(R.string.goods_price, String.valueOf(mTotalPrice - Integer.parseInt(mCouponAmount)-Integer.parseInt(mDeliveryFee))));
                mTvTotalPrice.setText(getResources().getString(R.string.goods_price, String.valueOf(mTotalPrice - Integer.parseInt(mCouponAmount)-Integer.parseInt(mDeliveryFee))));
                break;
            case R.id.v_address://选择地址
                startActivityForResult(AddressListActivity.newInstance(getCurContext()), 2);
                break;
            case R.id.tv_expected_delivery_time://期望送达时间
                if (mTimeList != null)
                    showDialog(getResources().getString(R.string.expected_delivery_time),
                            mTimeList);
                break;
            case R.id.tv_self_extracting_time://自提时间
                if (mTimeList != null)
                    showDialog(getResources().getString(R.string.self_extracting_time),
                            mTimeList);
                break;
            case R.id.tv_reserved_telephone://预留电话
                mReservedPhonePop = new XPopup.Builder(getCurActivity())
                        .isDarkTheme(false)
                        .autoDismiss(false)
                        .asInputConfirm(getResources().getString(R.string.modify_reserved_phone), "", "", mReservedPhone, text -> {
                            if (!TextUtils.isEmpty(text)) {
                                mReservedPhone = text;
                                mTvReservedTelephone.setText(mReservedPhone);
                                SoftInputUtil.hideSoftInput(ShoppingInfoActivity.this);
                                mReservedPhonePop.dismiss();
                            } else {
                                CommonToast.toast(getResources().getString(R.string.hint_input_contact_phone));
                            }
                        })
                        .show();
                break;
            case R.id.tv_store_name://点击店铺名称跳转详情
                Intent intent = new Intent(getCurContext(), StoreDetailActivity.class);
                intent.putExtra("id", mId);
                startActivity(intent);
                finish();
                break;
            case R.id.iv_call://拨打电话
                XXPermissions.with(getCurActivity())
                        .permission(Permission.CALL_PHONE)
                        .request(new OnPermission() {

                            @Override
                            public void hasPermission(List<String> granted, boolean all) {
                                if (all) {
                                    new XPopup.Builder(getCurContext())
                                            .asConfirm(getResources().getString(R.string.Tips), getResources().getString(R.string.hint_confirm_contact),
                                                    getResources().getString(R.string.cancel), getResources().getString(R.string.confirm_short),
                                                    () -> {
                                                        if (mPhoneList.size() > 0) {
                                                            String[] strings = new String[mPhoneList.size()];
                                                            mPhoneList.toArray(strings);
                                                            new XPopup.Builder(getCurActivity())
                                                                    .isDarkTheme(false)
                                                                    .asBottomList(getResources().getString(R.string.business_phone), strings,
                                                                            (position, text) -> {
                                                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                                                Uri data = Uri.parse("tel:" +mPhoneList.get(position));
                                                                                callIntent.setData(data);
                                                                                startActivity(callIntent);
                                                                            })
                                                                    .show();
                                                        } else {

                                                        }
                                                    }, null, false)
                                            .show();
                                } else {
                                }
                            }

                            @Override
                            public void noPermission(List<String> denied, boolean quick) {
                            }
                        });
                break;
            case R.id.tv_lucky_red_envelopes_coupons_text://红包、优惠券
                startActivityForResult(ChooseCouponActivity.newInstance(getCurContext(), String.valueOf(mTotalPrice), mPhone), 1);
                break;
            case R.id.tv_remark_text://备注
                startActivityForResult(RemarksActivity.newInstance(getCurContext()), 3);
                break;
            case R.id.tv_pay://去支付
                if (mIsDelivery) {
                    mPresenter.checkGoods(mId, "1", mAddressId, mTimeOfDelivery, mCouponId, mRemark, "", "");
                } else {
                    mPresenter.checkGoods(mId, "2", "", mSelfExtractingTime, mCouponId, mRemark, "", mReservedPhone);
                }
                break;
        }
    }

    @Override
    protected ShoppingInfoPresenter createPresenter() {
        return new ShoppingInfoPresenter(this);
    }

    @Override
    public void getShoppingInfoSuccess(ShoppingInfoBean data) {
        //判断是否支持自取
        if (!"1".equals(data.getShopInfo().getIsTakeYouself())) {
            mTvDeliveryOther.setVisibility(View.VISIBLE);
            mTvDelivery.setVisibility(View.GONE);
            mTvCollectByYourself.setVisibility(View.GONE);
            mTabBg.setVisibility(View.INVISIBLE);
        }else{
            mTvDeliveryOther.setVisibility(View.GONE);
            mTvDelivery.setVisibility(View.VISIBLE);
            mTvCollectByYourself.setVisibility(View.VISIBLE);
            mTabBg.setVisibility(View.VISIBLE);
        }
        //设置地址
        if (TextUtils.isEmpty(data.getAddressInfo().getAddress())) {
            mTvAddress.setText(getResources().getString(R.string.please_add_address_first));
        } else {
            mAddressId = data.getAddressInfo().getAddressId();
            mPhone = data.getAddressInfo().getPhone();
            mReservedPhone = data.getAddressInfo().getPhone();
            mTvAddress.setText(data.getAddressInfo().getAddress().concat(" ")
                    .concat(data.getAddressInfo().getRoomNo()));
            mTvPhone.setText(data.getAddressInfo().getName().concat("(")
                    .concat(data.getAddressInfo().getSex().equals("1") ? getResources().getString(R.string.mr) : getResources().getString(R.string.miss))
                    .concat(")  ")
                    .concat(mPhone));
        }
        //设置期望送达时间
        mTimeList = data.getSendTime();
        mTimeOfDelivery = data.getSendTime().get(0);
        mSelfExtractingTime = data.getSendTime().get(0);
        mTvTime.setText(data.getSendTime().get(0));
        //设置店铺名
        mTvStoreName.setText(data.getShopInfo().getShopName());
        //设置商品列表
        mList = data.getGoodsList();
        mAdapter.setData(mList);
        //设置包装费
        //mTvPackingFee.setText(data.getShopInfo().get);
        //设置配送费
        mDeliveryFee=data.getShopInfo().getDistributionFee();
        mTvDeliveryFee.setText(getResources().getString(R.string.goods_price, mDeliveryFee));
        //设置店铺满减
        if (TextUtils.isEmpty(data.getFullReduction())) {
            mTvShopFullReduction.setVisibility(View.GONE);
            mTvShopFullReductionText.setVisibility(View.GONE);
            mVLineEight.setVisibility(View.GONE);
        } else {
            mTvShopFullReduction.setText(getResources().getString(R.string.red_envelopes_value,
                    data.getShopInfo().getFullReduction()));
        }
        //设置首单立减
        if (data.getShopInfo().getFirstReduction() == 0) {
            mTvFirstReductionText.setVisibility(View.GONE);
            mTvFirstReduction.setVisibility(View.GONE);
            mVLine.setVisibility(View.GONE);
        } else {
            mTvFirstReduction.setText(getResources().getString(R.string.red_envelopes_value,
                    String.valueOf(data.getShopInfo().getFirstReduction())));
        }
        //设置总金额
        mTotalPrice = data.getTotalPrice();
        mTvTotalPrice.setText(getResources().getString(R.string.goods_price,
                String.valueOf(mTotalPrice)));
        mTvTotal.setText(getResources().getString(R.string.goods_price,
                String.valueOf(mTotalPrice)));

        if (data.getShopInfo().getTels()!=null) {
            for (int i = 0; i < data.getShopInfo().getTels().size(); i++) {
                mPhoneList.add(data.getShopInfo().getTels().get(i).getTel());
            }
        }

        //到店自取信息
        //设置店铺地址
        mTvStoreAddress.setText(data.getShopInfo().getAddress());
        //设置距离
        mTvDistance.setText(getResources().getString(R.string.distance_value,
                data.getShopInfo().getDistance()));
        //设置自提时间
        mTvSelfExtractingTime.setText(mSelfExtractingTime);
        //设置预留电话
        mTvReservedTelephone.setText(mReservedPhone);
    }

    @Override
    public void createOrderSuccess(CreateOrderBean data) {
        Intent intent = new Intent(getCurContext(), OrderPayActivity.class);
        intent.putExtra("order_id", data.getOrderId());
        intent.putExtra("price", String.valueOf(data.getPrice()));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {//红包
            mCouponId = data.getStringExtra("cId");
            mCouponAmount = data.getStringExtra("price");
            if (TextUtils.isEmpty(mCouponId)) {
                mTvTotal.setText(getResources().getString(R.string.goods_price, String.valueOf(mTotalPrice)));
                mTvLuckyRedEnvelopesCoupons.setTextColor(ContextCompat.getColor(getCurContext(),R.color.black));
                mTvLuckyRedEnvelopesCoupons.setText(getResources().getString(R.string.not_selected));
            } else {
                mTvLuckyRedEnvelopesCoupons.setTextColor(ContextCompat.getColor(getCurContext(),R.color.colorAccent));
                mTvLuckyRedEnvelopesCoupons.setText(getResources().getString(R.string.red_envelopes_value,
                        mCouponAmount));
                mTvTotal.setText(getResources().getString(R.string.goods_price, String.valueOf(mTotalPrice - Integer.parseInt(mCouponAmount))));
                mTvTotalPrice.setText(getResources().getString(R.string.goods_price, String.valueOf(mTotalPrice - Integer.parseInt(mCouponAmount))));
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {//地址
            mAddressId = data.getStringExtra("addressId");
            mPhone = data.getStringExtra("phone");
            String address = data.getStringExtra("address");
            String name = data.getStringExtra("name");
            String roomNo = data.getStringExtra("roomNo");
            String sex = data.getStringExtra("sex");
            mTvAddress.setText(address.concat(" ").concat(roomNo));
            mTvPhone.setText(name.concat("(")
                    .concat(sex.equals("1") ? getResources().getString(R.string.mr) : getResources().getString(R.string.miss))
                    .concat(")  ")
                    .concat(mPhone));
        } else if (requestCode == 3 && resultCode == RESULT_OK) {//备注
            mRemark = data.getStringExtra("content");
            if (TextUtils.isEmpty(mRemark)) {
                mTvRemark.setText(getResources().getString(R.string.taste_preference_and_other_requirements));
            } else {
                mTvRemark.setText(mRemark);
            }
        }
    }

    /**
     * 显示选择时间Dialog
     */
    public void showDialog(String title, List<String> list) {
        if (!list.contains(getResources().getString(R.string.snd_immediately))) {
            list.add(0, getResources().getString(R.string.snd_immediately));
        }
        DeliveryTimePop pop = new DeliveryTimePop(getCurContext(), title, list);
        pop.setOnTimeClickListener(time -> {
            if (title.equals(getResources().getString(R.string.self_extracting_time))) {
                mTvSelfExtractingTime.setText(time);
            } else {
                mTvTime.setText(time);
            }
        });
        new XPopup.Builder(getCurContext())
                .dismissOnTouchOutside(false)
                .asCustom(pop)
                .show();
    }

    private void showPhoneDialog(){
        new XPopup.Builder(getCurContext())
                .isDarkTheme(true)
                .asBottomList("", new String[]{"条目1", "条目2", "条目3", "条目4", "条目5"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                            }
                        })
                .show();
    }
}
