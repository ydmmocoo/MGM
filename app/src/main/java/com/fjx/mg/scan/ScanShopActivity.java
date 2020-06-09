package com.fjx.mg.scan;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.pay.PayActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.models.AgentInfoModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author    by hanlz
 * Date      on 2019/11/8.
 * Description：
 */
public class ScanShopActivity extends BaseMvpActivity<ScanShopPresenter> implements ScanShopContract.View {

    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.tvTranMoney)
    TextView tvTranMoney;

    private AgentInfoModel mAgentInfoModel;

    public static Intent newIntent(Context context, String userInfo, String payCode, String price) {
        Intent intent = new Intent(context, ScanShopActivity.class);
        intent.putExtra(IntentConstants.USER_INFO, userInfo);
        intent.putExtra(IntentConstants.PAY_CODE, payCode);
        intent.putExtra(IntentConstants.PRICE, price);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.atc_scan_shop;
    }

    @Override
    protected void initView() {
        super.initView();
        if (getIntent() != null) {
            if (StringUtil.isNotEmpty(getIntent().getStringExtra(IntentConstants.PRICE))){
                etMoney.setText(getIntent().getStringExtra(IntentConstants.PRICE));
                etMoney.setFocusable(false);//当店铺设置固定金额时，设置为不可编辑
            }
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ToolBarManager.with(this).setTitle(getString(R.string.transfer));
        GradientDrawableHelper.whit(tvTranMoney).setColor(R.color.colorAccent).setCornerRadius(50);
        if (getIntent() == null) {
            return;
        }
        String userInfo = getIntent().getStringExtra(IntentConstants.USER_INFO);
        if (StringUtil.isEmpty(userInfo)) {
            return;
        }
        mAgentInfoModel = JsonUtil.strToModel(userInfo, AgentInfoModel.class);
        tvPhone.setText(StringUtil.phoneText(mAgentInfoModel.getPhone()));
        CommonImageLoader.load(mAgentInfoModel.getuImg()).round().placeholder(R.drawable.food_default).into(ivAvatar);
        tvName.setText(mAgentInfoModel.getuNick());
    }

    @OnClick(R.id.tvTranMoney)
    public void onViewClicked() {
        //点击转账
        String mMoney = etMoney.getText().toString();
        String mRemark = etRemark.getText().toString();

        if (TextUtils.isEmpty(mMoney)) {
            CommonToast.toast(getString(R.string.hint_input_transfer));
            return;
        }

        float e = Float.parseFloat(mMoney);
        if (e < Constant.limitAmount) {
            CommonToast.toast(getString(R.string.limit_amount));
            return;
        }
        mPresenter.checkPrice(mMoney);
    }

    @Override
    protected ScanShopPresenter createPresenter() {
        return new ScanShopPresenter(this);
    }

    @Override
    public void checkSuccess() {
        //点击转账
        String mMoney = etMoney.getText().toString();
        String mRemark = etRemark.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("receiverId", mAgentInfoModel.getuId());
        map.put("amount", mMoney);
        map.put("instruction", mRemark);
        map.put("type", "1");
        map.put("payCode", getIntent().getStringExtra(IntentConstants.PAY_CODE));
        String ext = JsonUtil.moderToString(new PayExtModel(UsagePayMode.agent_shop, map));
        startActivityForResult(PayActivity.newInstance(getCurContext(), ext), 123);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPresenter.requestSendAgent("", "", getIntent().getStringExtra(IntentConstants.PAY_CODE), "3");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShopPayEvent(ShopPaySuccessEvent event) {
        mPresenter.requestSendAgent(etMoney.getText().toString(), "", getIntent().getStringExtra(IntentConstants.PAY_CODE), "1");
        finish();
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
