package com.fjx.mg.friend.notice.detail;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.transfer.TransferMoneyActivity;
import com.fjx.mg.me.transfer.MeTransferActivityx;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.db.base.DBHelper;
import com.library.repository.db.model.DaoSession;
import com.library.repository.models.IMNoticeModel;
import com.library.repository.models.OtherUserModel;
import com.library.repository.models.PhoneHistoryModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import butterknife.BindView;
import butterknife.OnClick;

public class IMNoticeDetailActivity extends BaseMvpActivity<IMNoticeDetailPresenter> implements IMNoticeDetailContract.View {

    public static final int TYPE_PAY_NOTICE = 1;
    public static final int TYPE_BILL_DETAIL = 2;
    public static final int TYPE_BALANCE_DETAIL = 3;


    @BindView(R.id.tvHint1)
    TextView tvHint1;
    @BindView(R.id.tvPersonType)
    TextView tvPersonType;
    @BindView(R.id.tvHint2)
    TextView tvHint2;
    @BindView(R.id.tvRemark)
    TextView tvRemark;
    @BindView(R.id.tvHint3)
    TextView tvHint3;
    @BindView(R.id.tvPayDate)
    TextView tvPayDate;
    @BindView(R.id.tvHint4)
    TextView tvHint4;
    @BindView(R.id.tvReceiveDate)
    TextView tvReceiveDate;
    @BindView(R.id.tvHint5)
    TextView tvHint5;
    @BindView(R.id.tvPayType)
    TextView tvPayType;
    @BindView(R.id.tvHint6)
    TextView tvHint6;
    @BindView(R.id.llHint6)
    LinearLayout llHint6;
    @BindView(R.id.tvUserRemark)
    TextView tvUserRemark;
    @BindView(R.id.tvPriceAl)
    TextView tvPriceAl;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.tvOrderCode)
    TextView tvOrderCode;
    @BindView(R.id.llPayDate)
    LinearLayout llPayDate;
    @BindView(R.id.llReceiveDate)
    LinearLayout llReceiveDate;

    @BindView(R.id.llReceiver)
    LinearLayout llReceiver;
    @BindView(R.id.llPayType)
    LinearLayout llPayType;
    @BindView(R.id.llBalance)
    LinearLayout llBalance;
    private int type;
    @BindView(R.id.tvAgain)
    TextView mTvAgain;


    private IMNoticeModel item;

    public static Intent newInstance(Context context, String id, int type) {
        Intent intent = new Intent(context, IMNoticeDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_im_notice_detail;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.bill_detail));
        type = getIntent().getIntExtra("type", TYPE_BILL_DETAIL);
        String id = getIntent().getStringExtra("id");

        if (type == TYPE_BILL_DETAIL) {
            mPresenter.getBillDetail(id);
        } else if (type == TYPE_PAY_NOTICE) {
            mPresenter.getPayNoticeDetail(id);
        } else if (type == TYPE_BALANCE_DETAIL) {
            llPayType.setVisibility(View.GONE);
            llBalance.setVisibility(View.VISIBLE);
            mPresenter.balanceDetail(id);
        }
    }

    private void showDetailOld(String type) {//之前写法，不用了
        tvOrderCode.setText(item.getOrderId());
        if (item.getPrice().contains("AR")) {
            tvPriceAl.setText(item.getPrice());
        } else {
            tvPriceAl.setText(item.getPrice() + "AR");
        }

        tvPrice.setText(item.getCnyPrice());
        tvRemark.setText(item.getRemark());
        tvUserRemark.setText(item.getUserRemark());
        tvPayType.setText(item.getPayType());
        tvPayDate.setText(item.getCreateTime());
        if (!TextUtils.isEmpty(item.getRemainBalance()))
            tvBalance.setText(item.getRemainBalance());

        if (TextUtils.isEmpty(item.getReciveTime())) {
            llReceiveDate.setVisibility(View.GONE);
        } else {
            llReceiveDate.setVisibility(View.VISIBLE);
            tvReceiveDate.setText(item.getCreateTime());
        }

        if (TextUtils.equals(item.getType(), "14")) {
            tvHint1.setText(getString(R.string.refund_party));
            tvHint5.setText(getString(R.string.refund_type));
            tvPersonType.setText(item.getFrom());

        } else {
            tvPersonType.setText(item.getTo());
            tvHint1.setText(getString(R.string.receiver));
            tvHint5.setText(getString(R.string.pay_type));
            if (TextUtils.equals("9", item.getType())) {
                //收款
                tvHint3.setText(getString(R.string.receive_date));
            } else {
                tvHint3.setText(getString(R.string.pay_date));
            }
        }

        if (TextUtils.isEmpty(tvPersonType.getText().toString())) {
            llReceiver.setVisibility(View.GONE);
        } else {
            llReceiver.setVisibility(View.VISIBLE);
        }
    }

    private void showDetail(String typeNun) {
        if (TextUtils.equals("7", typeNun) && type == TYPE_BILL_DETAIL) {
            //转账显示再来一笔按钮
            GradientDrawableHelper.whit(mTvAgain).setColor(R.color.colorAccent).setCornerRadius(50);
            mTvAgain.setVisibility(View.VISIBLE);
        }
        tvOrderCode.setText(item.getOrderId());
        if (item.getPrice().contains("AR")) {
            tvPriceAl.setText(item.getPrice());
        } else {
            tvPriceAl.setText(item.getPrice() + "AR");
        }
        tvPayDate.setText(item.getCreateTime());
        tvPrice.setText(item.getCnyPrice());
        tvHint2.setText(R.string.remark);
        tvRemark.setText(!TextUtils.isEmpty(item.getRemark()) ? item.getRemark() : "无");
        if (StringUtil.isNotEmpty(item.getUserRemark())) {
            llHint6.setVisibility(View.VISIBLE);
        } else {
            llHint6.setVisibility(View.GONE);
        }
        tvHint6.setText(R.string.explain);
        tvUserRemark.setText(item.getUserRemark());
        tvPayType.setText(item.getPayType());
        if (!TextUtils.isEmpty(item.getRemainBalance()))
            tvBalance.setText(item.getRemainBalance());
        //根据不同地方的帐单重新赋值
        if (type == TYPE_BILL_DETAIL) {

        } else if (type == TYPE_PAY_NOTICE) {

        } else if (type == TYPE_BALANCE_DETAIL) {
            tvRemark.setText(item.getTypeName());
        }

        //判断如果到帐时间小于发送时间，则隐藏到账时间
        if (item.getReciveTime().compareTo(item.getSendTime()) <= 0) {
            llReceiveDate.setVisibility(View.GONE);
        }
        if (StringUtil.isNotEmpty(item.getTo())) {
            llReceiver.setVisibility(View.VISIBLE);
            tvPersonType.setText(item.getTo());
        } else {
            llReceiver.setVisibility(View.GONE);
        }
        switch (Integer.parseInt(typeNun)) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 5:
            case 6:
            case 11:
            case 12:
            case 13:
                //收款方
                tvHint1.setText(getString(R.string.receiver));
                tvPersonType.setText(item.getTo());
                tvPayDate.setText(item.getCreateTime());
                tvHint3.setText(getString(R.string.pay_date));
                //领取时间不显示
                llReceiveDate.setVisibility(View.GONE);
                break;

            case 4:  //电费
                //收款方项不显示
                llReceiver.setVisibility(View.GONE);
                //领取时间不显示
                llReceiveDate.setVisibility(View.GONE);

                tvHint3.setText(getString(R.string.pay_date));
                tvPayDate.setText(item.getCreateTime());
                break;
            case 9:      //收款
            case 10:    //收红包
            case 24:
                //付款方
                tvHint1.setText(getString(R.string.payer));
                tvPersonType.setText(item.getFrom());
                tvHint3.setText(getString(R.string.pay_date));
                tvHint4.setText(getString(R.string.receive_date));
                tvPayDate.setText(item.getSendTime());
                tvReceiveDate.setText(item.getReciveTime());
                break;
            case 33:
                tvHint1.setText(getString(R.string.sales_network));
                tvPersonType.setText(item.getFrom());
                tvHint3.setText(getString(R.string.pay_date));
                tvHint4.setText(getString(R.string.receive_date));
                tvPayDate.setText(item.getCreateTime());
                tvReceiveDate.setText(item.getReciveTime());
                break;
            case 7:     //转帐
            case 8:     //红包
                //收款方
                tvHint1.setText(getString(R.string.receiver));
                tvPersonType.setText(item.getTo());
                tvHint3.setText(getString(R.string.pay_date));
                tvHint4.setText(getString(R.string.receive_date));
                tvPayDate.setText(item.getSendTime());
                tvReceiveDate.setText(item.getReciveTime());
                break;
            case 31:
                tvReceiveDate.setText(item.getReciveTime());
                break;
            default:
                break;
        }
    }

    private String getHintText(String type) {
        switch (type) {
            case "1"://充值
            case "2"://话费
            case "3"://流量
            case "4"://电费
            case "5"://水费
            case "6"://网费
            case "7"://转账
            case "8"://红包
            case "9"://收款
            case "10"://收红包
            case "11"://外卖
            case "12"://机票
            case "13"://酒店
                return getString(R.string.receiver);
        }
        return "";
    }

    @Override
    protected IMNoticeDetailPresenter createPresenter() {
        return new IMNoticeDetailPresenter(this);
    }


    @Override
    public void showBillDetail(IMNoticeModel bean) {
        item = bean;
        showDetail(item.getType());
    }


    @OnClick(R.id.tvOrderCode)
    public void onViewClicked() {
        String orderId = tvOrderCode.getText().toString();
        if (TextUtils.isEmpty(orderId)) return;
        StringUtil.copyClip(orderId);
        CommonToast.toast(getString(R.string.copy_success));
    }

    @OnClick({R.id.tvAgain})
    public void tranferAgain() {
        getUserInfo(item.getToUid());
    }

    private void getUserInfo(String userId) {
        showLoading();
        RepositoryFactory.getRemoteAccountRepository().getUserInfo("", userId, "")
                .compose(RxScheduler.<ResponseModel<OtherUserModel>>toMain())
                .as(this.<ResponseModel<OtherUserModel>>bindAutoDispose())
                .subscribe(new CommonObserver<OtherUserModel>() {
                    @Override
                    public void onSuccess(OtherUserModel data) {
                        hideLoading();
                        //存储数据到数据库 便于后面模糊搜索和其他历史记录展示
                        final DaoSession daoSession = DBHelper.getInstance().getDaoSession();
                        PhoneHistoryModel phoneHistoryModel = new PhoneHistoryModel();
                        phoneHistoryModel.setType("1");
                        phoneHistoryModel.setPhone(data.getPhone());
                        phoneHistoryModel.setFaceIcon(data.getAvatar());
                        phoneHistoryModel.setImUserId(data.getIdentifier());
                        phoneHistoryModel.setNickname(TextUtils.isEmpty(data.getUserNick()) ? data.getUserName() : data.getUserNick());
                        daoSession.insertOrReplace(phoneHistoryModel);

                        startActivityForResult(MeTransferActivityx.newInstance(getCurContext(), JsonUtil.moderToString(data)), 1);
                    }

                    @Override
                    public void onUserFailed(ResponseModel data) {
                        super.onUserFailed(data);
                        hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 111) {
            setResult(resultCode);
            finish();
        }
    }
}
