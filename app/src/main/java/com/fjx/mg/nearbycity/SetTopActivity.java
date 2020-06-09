package com.fjx.mg.nearbycity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.fjx.mg.R;
import com.fjx.mg.nearbycity.pay.NearbyCityPayActivity;
import com.fjx.mg.utils.WindowManagerUtils;
import com.library.common.base.BaseActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.SetTopDetailInfoModel;
import com.library.repository.models.SetTopInfoModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Author    by hanlz
 * Date      on 2019/10/20.
 * Description：修改置顶
 */
public class SetTopActivity extends BaseActivity implements TextWatcher {


    @BindView(R.id.tvContent)
    TextView mTvContent;
    @BindView(R.id.tvValidity)
    TextView mTvValidity;
    @BindView(R.id.tvCurrentSetTop)
    TextView mTvCurrentSetTop;

    @BindView(R.id.tvSubtractPrice)
    TextView mTvSubtractPrice;
    @BindView(R.id.etPrice)
    EditText mEtPrice;
    @BindView(R.id.tvAddPrice)
    TextView mTvAddPrice;

    @BindView(R.id.tvSubtractDays)
    TextView mTvSubtractDays;
    @BindView(R.id.etDay)
    EditText mEtDay;
    @BindView(R.id.tvAddDays)
    TextView mTvAddDays;


    @BindView(R.id.tvRule)
    TextView mTvRule;
    @BindView(R.id.tvPayMoney)
    TextView mTvPayMoney;
    @BindView(R.id.btnPublisherNearbyCity)
    Button mBtnPublisherNearbyCity;
    @BindView(R.id.llRoot)
    LinearLayout mLlRoot;
    @BindView(R.id.tvEmpty)
    TextView mTvEmpty;

    @BindView(R.id.mRootView)
    NestedScrollView mRootView;
    @BindView(R.id.mLlRootView)
    LinearLayout mLlRootView;


    private String mCId;
    private SetTopInfoModel mInfo;

    public static Intent newIntent(Context context, String cId) {
        Intent intent = new Intent(context, SetTopActivity.class);
        intent.putExtra(IntentConstants.CID, cId);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_set_top;
    }

    /**
     * 初始化监听的方法
     */
    private void initListener() {
        showInputManager(mEtDay);
        showInputManager(mEtPrice);
    }

    /**
     * @param editText
     */
    private void showInputManager(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        /** 目前测试来看，还是挺准的
         * 原理：OnGlobalLayoutListener 每次布局变化时都会调用
         * 界面view 显示消失都会调用，软键盘显示与消失时都调用
         * */
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);
        InputMethodManager inputManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

    }

    ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener = new ViewTreeObserver
            .OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            //判断窗口可见区域大小
            Rect r = new Rect();
            // getWindowVisibleDisplayFrame()会返回窗口的可见区域高度
            getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
            int heightDifference = WindowManagerUtils.getScreenHight() - (r.bottom);
            boolean isKeyboardShowing = heightDifference > WindowManagerUtils.getScreenHight() / 3;
            if (isKeyboardShowing) {
                // bottomView 需要跟随软键盘移动的布局
                // setDuration(0) 默认300, 设置 0 ，表示动画执行时间为0，没有过程，只有动画结果了
                mLlRootView.animate().translationY(-heightDifference).setDuration(0).start();
            } else {
                mLlRootView.animate().translationY(0).start();
            }
        }
    };


    @Override
    protected void initView() {
        super.initView();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (getIntent() == null) {
            return;
        }
        initListener();
        mCId = getIntent().getStringExtra(IntentConstants.CID);
        requestInfo(mCId);

        mEtDay.addTextChangedListener(this);

        mEtPrice.addTextChangedListener(this);
    }

    private void requestInfo(String cId) {
        showLoading();
        RepositoryFactory.getRemoteNearbyCitysApi()
                .requestInfo(cId)
                .compose(RxScheduler.<ResponseModel<SetTopDetailInfoModel>>toMain())
                .as(this.<ResponseModel<SetTopDetailInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<SetTopDetailInfoModel>() {
                    @Override
                    public void onSuccess(SetTopDetailInfoModel data) {
                        hideLoading();
                        mLlRoot.setVisibility(View.VISIBLE);
                        mTvEmpty.setVisibility(View.GONE);
                        String content = "";
                        if (data.getInfo().getContent().contains("\n")) {
                            content = data.getInfo().getContent();
                        } else {
                            content = data.getInfo().getContent().replaceAll("\r", "\n");
                        }
                        mTvContent.setText(content);
                        mTvValidity.setText(data.getInfo().getExp());
                        mTvCurrentSetTop.setText(data.getInfo().getRemainTopTime().concat("(").concat(data.getInfo().getTopPerPirce()).concat("AR").concat("/天)"));
                        mInfo = data.getInfo();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        hideLoading();
                        mLlRoot.setVisibility(View.GONE);
                        mLlRoot.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        hideLoading();
                        mLlRoot.setVisibility(View.GONE);
                        mLlRoot.setVisibility(View.VISIBLE);
                    }
                });
    }

    @OnClick({R.id.tvAddDays, R.id.tvSubtractDays, R.id.tvAddPrice,
            R.id.tvSubtractPrice, R.id.tvEmpty, R.id.btnPublisherNearbyCity, R.id.toolbar_iv_back
           })
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tvAddDays://增加天数
                mEtDay.setText(StringUtil.add(mEtDay.getText().toString(), "1"));
                calculatePayMoney();
                if (!"0".equals(mEtDay.getText().toString())) {
                    ViewUtil.setDrawableLeft(mTvSubtractDays, R.drawable.nearby_city_subtract_black);
                }
                break;
            case R.id.tvSubtractDays://减少天数
                mEtDay.setText(StringUtil.subtract(mEtDay.getText().toString(), "1", 0l));
                calculatePayMoney();
                if ("0".equals(mEtDay.getText().toString())) {
                    ViewUtil.setDrawableLeft(mTvSubtractDays, R.drawable.nearby_city_subtract_gray);
                }
                break;
            case R.id.tvAddPrice://增加价格
                mEtPrice.setText(StringUtil.add(mEtPrice.getText().toString(), "1"));
                calculatePayMoney();
                if (!"0".equals(mEtPrice.getText().toString())) {
                    ViewUtil.setDrawableLeft(mTvSubtractPrice, R.drawable.nearby_city_subtract_black);
                }
                break;
            case R.id.tvSubtractPrice://减少价格
                mEtPrice.setText(StringUtil.subtract(mEtPrice.getText().toString(), "1", 0l));
                calculatePayMoney();
                if ("0".equals(mEtPrice.getText().toString())) {
                    ViewUtil.setDrawableLeft(mTvSubtractPrice, R.drawable.nearby_city_subtract_gray);
                }
                break;
            case R.id.tvEmpty://空页面点击刷新
                requestInfo(mCId);
                break;
            case R.id.btnPublisherNearbyCity://发布
                if (StringUtil.equals("0", mTvPayMoney.getText().toString())) {
                    CommonToast.toast(R.string.publisher_falied_ar_isempty);
                    return;
                }
                Map<String, Object> map = new HashMap<>();
                map.put(IntentConstants.NERBY_CITY_CID, mCId);
                String days = StringUtil.add(mEtDay.getText().toString(), mInfo.getTopDays());
                map.put(IntentConstants.NERBY_CITY_TOPDAYS, days);
                map.put(IntentConstants.NERBY_CITY_PERPRICE, StringUtil.add(mEtPrice.getText().toString(), mInfo.getTopPerPirce()));
                map.put(IntentConstants.NERBY_CITY_TOLPRICE, mTvPayMoney.getText().toString());
                String ext = JsonUtil.moderToString(new PayExtModel(UsagePayMode.nearby_city_set_top_pay, map));
                startActivityForResult(NearbyCityPayActivity.newInstance(getCurContext(), ext), 666);
                break;
            case R.id.toolbar_iv_back:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() > 7) {
            return;
        }
        calculatePayMoney();
        if (!"0".equals(mEtDay.getText().toString()) && mEtDay.getText().toString().length() != 0) {
            ViewUtil.setDrawableLeft(mTvSubtractDays, R.drawable.nearby_city_subtract_black);
        } else {
            ViewUtil.setDrawableLeft(mTvSubtractDays, R.drawable.nearby_city_subtract_gray);
        }

        if (!"0".equals(mEtPrice.getText().toString()) && mEtPrice.getText().toString().length() != 0) {
            ViewUtil.setDrawableLeft(mTvSubtractPrice, R.drawable.nearby_city_subtract_black);
        } else {
            ViewUtil.setDrawableLeft(mTvSubtractPrice, R.drawable.nearby_city_subtract_gray);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 666) {
            if (resultCode == 111) {
                //支付成功
                finish();
            }
        }
    }

    /**
     * 计算实付金额
     * <p>
     * 实付=剩余天数*价格变动+天数变动*(原置顶价格+价格变动）
     * </p>
     * <p>
     * remainDays     剩余天数
     * price       价格变动
     * days        天数变动
     * topPrePrice 原置顶价格
     */
    private void calculatePayMoney() {
        String remainDays = String.valueOf(mInfo.getRemainDays());
        String price = mEtPrice.getText().toString();
        String days = mEtDay.getText().toString();
        String topPrePrice = mInfo.getTopPerPirce();
        mTvRule.setText(remainDays.concat("*").concat(price).concat("+").concat(days).concat("*").concat("(").concat(topPrePrice).concat("+").concat(price).concat(")"));
        mTvPayMoney.setText(StringUtil.add(StringUtil.multiply(remainDays, price), StringUtil.multiply(days, StringUtil.add(topPrePrice, price))));
    }

    /**
     * 使滚动条滚动至指定位置（垂直滚动）
     *
     * @param scrollView 要滚动的ScrollView
     * @param to         滚动到的位置
     */
    protected void scrollVertical(final ScrollView scrollView, final int to) {
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, to);
            }
        }, 100);
    }

    /**
     * 使ScrollView滚动至底部，显示Submit按钮
     *
     * @param scrollView 要滚动的scrollView
     */
    protected void scrollToShowSubmitBtn(final ScrollView scrollView) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollVertical(scrollView, scrollView.getHeight());
            }
        }, 100);
    }
}
