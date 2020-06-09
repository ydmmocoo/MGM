package com.fjx.mg.pay;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.common.paylibrary.AliPayUtil;
import com.common.paylibrary.PayConfig;
import com.common.paylibrary.WXPayUtil;
import com.common.paylibrary.model.AliPayModel;
import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.common.paylibrary.model.WXPayModel;
import com.fjx.mg.R;
import com.fjx.mg.setting.password.pay.ModifyPayPwdActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.fingerprint.FingerprintIdentify;
import com.library.common.fingerprint.base.BaseFingerprint;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.common.view.editdialog.PayFragment;
import com.library.common.view.editdialog.PayPwdView;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.base.DBHelper;
import com.library.repository.db.model.DaoSession;
import com.library.repository.db.model.FingerprintModel;
import com.library.repository.models.BalanceModel;
import com.library.repository.models.ConvertModel;
import com.library.repository.models.GroupRedPacketModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.ThreeScanPayModel;
import com.library.repository.models.TransferLikerModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.List;
import java.util.Map;

public class PayPresenter extends PayContract.Presenter {

    private String accountPayMoney;
    private MaterialDialog dialog;
    private MaterialDialog fingerDialog;
    private FingerprintIdentify mFingerprintIdentify;
    private boolean waitDataLoading;

    private Map<String, Object> payObj;
    private UsagePayMode payMode;

    private Dialog dialog2;

    PayPresenter(PayContract.View view) {
        super(view);
//        showFingerprintDialog(null, null);
    }


    @Override
    void convertMoney(final String type, String price) {
//        mView.showLoading();
        dialog2 = new Dialog(mView.getCurActivity());
        dialog2.setContentView(R.layout.dialog_qr_layout);
        dialog2.getWindow().setGravity(Gravity.CENTER);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.show();
        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RepositoryFactory.getRemoteRepository().convert(type, price)
                .compose(RxScheduler.<ResponseModel<ConvertModel>>toMain())
                .as(mView.<ResponseModel<ConvertModel>>bindAutoDispose())
                .subscribe(new CommonObserver<ConvertModel>() {
                    @Override
                    public void onSuccess(ConvertModel data) {
//                        mView.hideLoading();
                        if (dialog2.isShowing() && dialog2 != null) {
                            dialog2.dismiss();
                        }
                        String CNYPrice = data.getCNYPrice();
                        String servicePrice = data.getServicePrice();
                        String totalPrice = data.getTotalPrice();
                        accountPayMoney = totalPrice;
                        if (!TextUtils.isEmpty(accountPayMoney) && waitDataLoading) {
                            if (payMode == null || payObj == null) return;
                            showPayCheckDialog(payMode, payObj);
                        }

                        mView.convertMoneySuccess(type, CNYPrice, servicePrice, totalPrice);
                    }

                    @Override
                    public void onError(ResponseModel data) {
//                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                        if (dialog2.isShowing() && dialog2 != null) {
                            dialog2.dismiss();
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (dialog2.isShowing() && dialog2 != null) {
                            dialog2.dismiss();
                        }
                    }
                });
    }


    void payGroupRedPacket(int payType, Map<String, Object> object) {

        switch (payType) {
            case Constant.PayType.ACCOUNT:
                showPayCheckDialog(UsagePayMode.im_group_red_packet, object);
                break;
            case Constant.PayType.ZFB:
                groupRedPacketPay2Zfb(object);
                break;
            case Constant.PayType.WX:
                groupRedPacketPay2WX(object);
                break;

        }
    }


    @Override
    void transferAli(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().transferByAli(map)
                .compose(RxScheduler.<ResponseModel<AliPayModel>>toMain())
                .as(mView.<ResponseModel<AliPayModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AliPayModel>() {
                    @Override
                    public void onSuccess(AliPayModel data) {
                        mView.hideLoading();
                        //.im_transfer或者im_redpacket都一样，属于同一个接口，下同
                        AliPayUtil.with(mView.getCurActivity()).doPay(UsagePayMode.im_transfer, data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void transferWX(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().transferByWx(map)
                .compose(RxScheduler.<ResponseModel<WXPayModel>>toMain())
                .as(mView.<ResponseModel<WXPayModel>>bindAutoDispose())
                .subscribe(new CommonObserver<WXPayModel>() {
                    @Override
                    public void onSuccess(WXPayModel data) {
                        mView.hideLoading();
                        WXPayUtil.newInstance().doPay(UsagePayMode.im_transfer, data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void transferByBalance(Map<String, Object> map) {
        /*mView.showLoading();
        RepositoryFactory.getRemotePayRepository().transferByBalance(map)
                .compose(RxScheduler.<ResponseModel<TransBalanceResultM>>toMain())
                .as(mView.<ResponseModel<TransBalanceResultM>>bindAutoDispose())
                .subscribe(new CommonObserver<TransBalanceResultM>() {
                    @Override
                    public void onSuccess(TransBalanceResultM data) {
                        mView.hideLoading();
                        //转账或者余额
                        PayExtModel extModel = new PayExtModel(UsagePayMode.im_transfer, data.getTransferId());
                        PayConfig.sendPayReceiver(extModel.toString());

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });*/

    }


    @Override
    void finalTransferPay(int payType, Map<String, Object> object) {

        switch (payType) {
            case Constant.PayType.ACCOUNT:
//                transferByBalance(object);
                showPayCheckDialog(UsagePayMode.im_transfer, object);
                break;
            case Constant.PayType.ZFB:
                transferAli(object);
                break;
            case Constant.PayType.WX:
                transferWX(object);
                break;
            case Constant.PayType.BANKCARD:

                break;
        }
    }

    @Override
    void phoneWxRecharge(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().phoneChargeByWx(map)
                .compose(RxScheduler.<ResponseModel<WXPayModel>>toMain())
                .as(mView.<ResponseModel<WXPayModel>>bindAutoDispose())
                .subscribe(new CommonObserver<WXPayModel>() {
                    @Override
                    public void onSuccess(WXPayModel data) {
                        mView.hideLoading();
                        //phone_recharge或者data_recharge都一样，属于同一个接口，下同
                        WXPayUtil.newInstance().doPay(UsagePayMode.phone_recharge, data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void phoneZFBRecharge(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().phoneChargeByAli(map)
                .compose(RxScheduler.<ResponseModel<String>>toMain())
                .as(mView.<ResponseModel<String>>bindAutoDispose())
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onSuccess(String data) {
                        mView.hideLoading();

                        AliPayModel aliPayModel = new AliPayModel();
                        aliPayModel.setUrl(data);
                        AliPayUtil.with(mView.getCurActivity()).doPay(UsagePayMode.phone_recharge, aliPayModel);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void phoneBalanceRecharge(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().phoneCharge(map)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
//                        CommonToast.toast("充值成功");
//                        mView.getCurActivity().finish();
                        //转账或者余额
                        PayExtModel extModel = new PayExtModel(UsagePayMode.phone_recharge);
                        PayConfig.sendPayReceiver(extModel.toString());

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });

    }

    @Override
    void finalPhoneRechargePay(int payType, Map<String, Object> object) {
        switch (payType) {
            case Constant.PayType.ACCOUNT:
                showPayCheckDialog(UsagePayMode.phone_recharge, object);
                break;
            case Constant.PayType.ZFB:
                phoneZFBRecharge(object);
                break;
            case Constant.PayType.WX:
                phoneWxRecharge(object);
                break;
            case Constant.PayType.BANKCARD:

                break;
        }
    }

    @Override
    void transferDirecAli(final Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().transferDirecByAli(map)
                .compose(RxScheduler.<ResponseModel<AliPayModel>>toMain())
                .as(mView.<ResponseModel<AliPayModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AliPayModel>() {
                    @Override
                    public void onSuccess(AliPayModel data) {
                        mView.hideLoading();
                        AliPayUtil.with(mView.getCurActivity()).doPay(UsagePayMode.im_transfer, data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                        saveTransferInfo2Linked(map);
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void transferDirecWX(final Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().transferDirecByWx(map)
                .compose(RxScheduler.<ResponseModel<WXPayModel>>toMain())
                .as(mView.<ResponseModel<WXPayModel>>bindAutoDispose())
                .subscribe(new CommonObserver<WXPayModel>() {
                    @Override
                    public void onSuccess(WXPayModel data) {
                        mView.hideLoading();
                        WXPayUtil.newInstance().doPay(UsagePayMode.im_transfer, data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                        saveTransferInfo2Linked(map);
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void transferDirecByBalance(final Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().transferDirecByBalance(map)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        //转账或者余额
                        PayExtModel extModel = new PayExtModel(UsagePayMode.im_transfer);
                        PayConfig.sendPayReceiver(extModel.toString());
                        //支付成功删除存储本地转账信息
                        String unique = (String) map.get("outOrderId");
                        DaoSession daoSession = DBHelper.getInstance().getDaoSession();
                        List<TransferLikerModel> transferLikerModels = daoSession.loadAll(TransferLikerModel.class);
                        for (TransferLikerModel model : transferLikerModels) {
                            if (TextUtils.equals(unique, model.getUnique())) {
                                daoSession.delete(model);
                            }
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    /**
     * 平台转账
     *
     * @param payType
     * @param object
     */
    @Override
    void finalTransferDirecPay(int payType, Map<String, Object> object) {
        switch (payType) {
            case Constant.PayType.ACCOUNT:
//                transferDirecByBalance(object);
                showPayCheckDialog(UsagePayMode.mg_transfer, object);
                break;
            case Constant.PayType.ZFB:
                transferDirecAli(object);
                break;
            case Constant.PayType.WX:
                transferDirecWX(object);
                break;
            case Constant.PayType.BANKCARD:

                break;
        }
    }

    public void saveTransferInfo2Linked(Map<String, Object> object) {
        String receiverId = (String) object.get("receiverId");
        String amount = (String) object.get("amount");
        String instruction = (String) object.get("instruction");
        String type = (String) object.get("type");
        String unique = (String) object.get("outOrderId");
        DaoSession daoSession = DBHelper.getInstance().getDaoSession();
        TransferLikerModel transferLikerModel = new TransferLikerModel();
        transferLikerModel.setUnique(unique);
        transferLikerModel.setAmount(amount);
        transferLikerModel.setInstruction(instruction);
        transferLikerModel.setType(type);
        transferLikerModel.setImUserId(receiverId);
        daoSession.insertOrReplace(transferLikerModel);
    }

    /**
     * 商铺展示二维码给用户支付
     *
     * @param payType
     * @param object
     */
    void payAgentShop(int payType, Map<String, Object> object) {

        final String payCode = (String) object.get("payCode");
        final String accout = (String) object.get("amount");
        switch (payType) {
            case Constant.PayType.ACCOUNT:
                showPayCheckDialog(UsagePayMode.agent_shop, object);
                break;
            case Constant.PayType.ZFB:
                mView.showLoading();

                RepositoryFactory.getRemoteAccountRepository().shopScanByAli(payCode, accout)
                        .compose(RxScheduler.<ResponseModel<String>>toMain())
                        .as(mView.<ResponseModel<String>>bindAutoDispose())
                        .subscribe(new CommonObserver<String>() {
                            @Override
                            public void onSuccess(String data) {
                                mView.hideLoading();
                                AliPayModel payModel = new AliPayModel();
                                payModel.setUrl(data);
                                AliPayUtil.with(mView.getCurActivity()).doPay(UsagePayMode.agent_shop, payModel);
                            }

                            @Override
                            public void onError(ResponseModel data) {
                                mView.hideLoading();
                                CommonToast.toast(data.getMsg());
                            }

                            @Override
                            public void onNetError(ResponseModel data) {
                                mView.hideLoading();
                                CommonToast.toast(data.getMsg());
                            }
                        });
                break;
            case Constant.PayType.WX:
                mView.showLoading();
                RepositoryFactory.getRemoteAccountRepository().shopScanByWx(payCode, accout)
                        .compose(RxScheduler.<ResponseModel<WXPayModel>>toMain())
                        .as(mView.<ResponseModel<WXPayModel>>bindAutoDispose())
                        .subscribe(new CommonObserver<WXPayModel>() {
                            @Override
                            public void onSuccess(WXPayModel data) {
                                mView.hideLoading();
                                WXPayUtil.newInstance().doPay(UsagePayMode.im_transfer, data);
                            }

                            @Override
                            public void onError(ResponseModel data) {
                                mView.hideLoading();
                                CommonToast.toast(data.getMsg());
                            }

                            @Override
                            public void onNetError(ResponseModel data) {
                                mView.hideLoading();
                                CommonToast.toast(data.getMsg());
                            }
                        });
                break;
            case Constant.PayType.BANKCARD:

                break;
        }
    }


    @Override
    void ewnRechargeAli(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().EWNChargeByAli(map)
                .compose(RxScheduler.<ResponseModel<String>>toMain())
                .as(mView.<ResponseModel<String>>bindAutoDispose())
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onSuccess(String data) {
                        mView.hideLoading();

                        AliPayModel aliPayModel = new AliPayModel();
                        aliPayModel.setUrl(data);
                        AliPayUtil.with(mView.getCurActivity()).doPay(UsagePayMode.net_recharge, aliPayModel);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void ewnRechargeWX(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().EWNCChargeByWx(map)
                .compose(RxScheduler.<ResponseModel<WXPayModel>>toMain())
                .as(mView.<ResponseModel<WXPayModel>>bindAutoDispose())
                .subscribe(new CommonObserver<WXPayModel>() {
                    @Override
                    public void onSuccess(WXPayModel data) {
                        mView.hideLoading();
                        //使用水电网的任意类型就行，属于同一个接口，下同
                        WXPayUtil.newInstance().doPay(UsagePayMode.net_recharge, data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void ewnRechargeBalance(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().EWNCChargeBalance(map)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        //转账或者余额
                        PayExtModel extModel = new PayExtModel(UsagePayMode.net_recharge);
                        PayConfig.sendPayReceiver(extModel.toString());
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void finalEWNnRechargePay(int payType, Map<String, Object> object) {
        switch (payType) {
            case Constant.PayType.ACCOUNT:
//                ewnRechargeBalance(object);
                showPayCheckDialog(UsagePayMode.net_recharge, object);
                break;
            case Constant.PayType.ZFB:
                ewnRechargeAli(object);
                break;
            case Constant.PayType.WX:
                ewnRechargeWX(object);
                break;
            case Constant.PayType.BANKCARD:

                break;
        }
    }

    @Override
    void balanceRechargeAli(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().chargeBalanceByAli(map)
                .compose(RxScheduler.<ResponseModel<String>>toMain())
                .as(mView.<ResponseModel<String>>bindAutoDispose())
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onSuccess(String data) {
                        mView.hideLoading();

                        AliPayModel aliPayModel = new AliPayModel();
                        aliPayModel.setUrl(data);
                        AliPayUtil.with(mView.getCurActivity()).doPay(UsagePayMode.recharge_balance, aliPayModel);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void balanceRechargeWX(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().chargeBalanceByWx(map)
                .compose(RxScheduler.<ResponseModel<WXPayModel>>toMain())
                .as(mView.<ResponseModel<WXPayModel>>bindAutoDispose())
                .subscribe(new CommonObserver<WXPayModel>() {
                    @Override
                    public void onSuccess(WXPayModel data) {
                        mView.hideLoading();
                        //使用水电网的任意类型就行，属于同一个接口，下同
                        WXPayUtil.newInstance().doPay(UsagePayMode.net_recharge, data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void finalbalanceRechargePay(int payType, Map<String, Object> object) {
        switch (payType) {
            case Constant.PayType.ZFB:
                balanceRechargeAli(object);
                break;
            case Constant.PayType.WX:
                balanceRechargeWX(object);
                break;
            case Constant.PayType.BANKCARD:

                break;
        }
    }

    @Override
    void checkPayPasswoed(String password, final PayFragment fragment, final UsagePayMode payMode, final Map<String, Object> object) {
        if (mView == null) return;
        mView.createAndShowDialog();
        RepositoryFactory.getRemotePayRepository().checkPayPassword(password)
                .compose(RxScheduler.<ResponseModel<String>>toMain())
                .as(mView.<ResponseModel<String>>bindAutoDispose())
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onSuccess(String data) {
                        mView.destoryAndDismissDialog();
                        fragment.dismiss();

                        payBalance(payMode, object);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }
                });

    }

    /**
     * 余额支付
     *
     * @param payMode
     * @param object
     */
    private void payBalance(UsagePayMode payMode, Map<String, Object> object) {
        switch (payMode) {
            case mg_transfer:
                saveTransferInfo2Linked(object);
                transferDirecByBalance(object);
                break;
            case im_redpacket:
            case im_transfer:
                //聊天转账发红包
                transferByBalance(object);
                break;

            case phone_recharge:
            case data_recharge:
                //手机流量充值
                phoneBalanceRecharge(object);
                break;


            case net_recharge:
            case water_recharge:
            case elect_recharge:
                //水电网费充值
                ewnRechargeBalance(object);
                break;

            case levle_recharge:
                upgradeByBalance(object);
                break;
            case agent_shop:
                userPayShop(object);
                break;
            case three_scan_pay:
                String amount = (String) object.get("amount");
                String payCode = (String) object.get("payCode");
                String type = (String) object.get("type");
                String outOrderId = (String) object.get("orderId");
                RepositoryFactory.getRemotePayRepository().scanByBalance(amount, outOrderId, payCode, type)
                        .compose(RxScheduler.<ResponseModel<ThreeScanPayModel>>toMain())
                        .as(mView.<ResponseModel<ThreeScanPayModel>>bindAutoDispose())
                        .subscribe(new CommonObserver<ThreeScanPayModel>() {
                            @Override
                            public void onSuccess(ThreeScanPayModel url) {//成功跳转的地址
//                                CommonToast.toast(mView.getCurActivity().getString(R.string.hint_pay_resultx));
                                PayExtModel extModel = new PayExtModel(UsagePayMode.three_scan_pay);
                                PayConfig.sendPayReceiver(extModel.toString());
                            }

                            @Override
                            public void onError(ResponseModel data) {
                                CommonToast.toast(data.getMsg());
                            }

                            @Override
                            public void onNetError(ResponseModel data) {

                            }
                        });
                break;
            case im_group_red_packet://群红包
                userPayGroupRedPacket(object);
                break;
            default:
        }
    }

    /*---------------------im群红包start------------------------------*/
    void userPayGroupRedPacket(Map<String, Object> map) {//余额支付
        mView.showLoading();
        final String amount = (String) map.get("amount");
        final String type = (String) map.get("type");
        final String num = (String) map.get("num");
        final String remark = (String) map.get("remark");
        final String groupId = (String) map.get(IntentConstants.GROUP_ID);
        RepositoryFactory.getRemotePayRepository().sendRedEnvelopeByBalance(type, amount, num, remark, groupId)
                .compose(RxScheduler.<ResponseModel<GroupRedPacketModel>>toMain())
                .as(mView.<ResponseModel<GroupRedPacketModel>>bindAutoDispose())
                .subscribe(new CommonObserver<GroupRedPacketModel>() {
                    @Override
                    public void onSuccess(GroupRedPacketModel data) {
                        mView.hideLoading();
                        PayExtModel extModel = new PayExtModel(UsagePayMode.im_group_red_packet);
                        extModel.setMessage(data.getrId());
                        PayConfig.sendPayReceiver(extModel.toString());
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });

    }

    void groupRedPacketPay2Zfb(Map<String, Object> map) {
        mView.showLoading();
        final String amount = (String) map.get("amount");
        final String type = (String) map.get("type");
        final String num = (String) map.get("num");
        final String remark = (String) map.get("remark");
        final String groupId = (String) map.get(IntentConstants.GROUP_ID);
        RepositoryFactory.getRemotePayRepository().sendRedEnvelopeByAli(type, amount, num, remark, groupId)
                .compose(RxScheduler.<ResponseModel<GroupRedPacketModel>>toMain())
                .as(mView.<ResponseModel<GroupRedPacketModel>>bindAutoDispose())
                .subscribe(new CommonObserver<GroupRedPacketModel>() {
                    @Override
                    public void onSuccess(GroupRedPacketModel data) {
                        mView.hideLoading();
                        AliPayModel aliPayModel = new AliPayModel();
                        aliPayModel.setUrl(data.getUrl());
                        aliPayModel.setTransferId(data.getrId());
                        AliPayUtil.with(mView.getCurActivity()).doPay(UsagePayMode.im_group_red_packet, aliPayModel);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    void groupRedPacketPay2WX(Map<String, Object> map) {
        mView.showLoading();
        final String amount = (String) map.get("amount");
        final String type = (String) map.get("type");
        final String num = (String) map.get("num");
        final String remark = (String) map.get("remark");
        final String groupId = (String) map.get(IntentConstants.GROUP_ID);
        RepositoryFactory.getRemotePayRepository().sendRedEnvelopeByWx(type, amount, num, remark, groupId)
                .compose(RxScheduler.<ResponseModel<WXPayModel>>toMain())
                .as(mView.<ResponseModel<WXPayModel>>bindAutoDispose())
                .subscribe(new CommonObserver<WXPayModel>() {
                    @Override
                    public void onSuccess(WXPayModel data) {
                        mView.hideLoading();
                        WXPayUtil.newInstance().doPayGroup(UsagePayMode.im_group_red_packet, data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    /*---------------------im群红包end--------------------------------*/
    void userPayShop(Map<String, Object> map) {
        mView.showLoading();
        final String payCode = (String) map.get("payCode");
        final String amount = (String) map.get("amount");
        RepositoryFactory.getRemoteAccountRepository().
                shopScanByBlance(payCode, amount)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        PayExtModel extModel = new PayExtModel(UsagePayMode.agent_shop);
                        PayConfig.sendPayReceiver(extModel.toString());
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void upgradeByAli(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().upgradeByAli(map)
                .compose(RxScheduler.<ResponseModel<String>>toMain())
                .as(mView.<ResponseModel<String>>bindAutoDispose())
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onSuccess(String data) {
                        mView.hideLoading();
                        AliPayModel aliPayModel = new AliPayModel();
                        aliPayModel.setUrl(data);
                        AliPayUtil.with(mView.getCurActivity()).doPay(UsagePayMode.levle_recharge, aliPayModel);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void upgradeByWx(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().upgradeByWx(map)
                .compose(RxScheduler.<ResponseModel<WXPayModel>>toMain())
                .as(mView.<ResponseModel<WXPayModel>>bindAutoDispose())
                .subscribe(new CommonObserver<WXPayModel>() {
                    @Override
                    public void onSuccess(WXPayModel data) {
                        mView.hideLoading();
                        WXPayUtil.newInstance().doPay(UsagePayMode.levle_recharge, data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });

    }

    @Override
    void upgradeByBalance(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().upgradeByBalance(map)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        PayExtModel extModel = new PayExtModel(UsagePayMode.levle_recharge);
                        PayConfig.sendPayReceiver(extModel.toString());
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void finalUpgradePay(int payType, Map<String, Object> object) {
        switch (payType) {
            case Constant.PayType.ZFB:
                upgradeByAli(object);
                break;
            case Constant.PayType.WX:
                upgradeByWx(object);
                break;

            case Constant.PayType.ACCOUNT:
//                ewnRechargeBalance(object);
                showPayCheckDialog(UsagePayMode.levle_recharge, object);
            case Constant.PayType.BANKCARD:

                break;
        }
    }

    @Override
    void getUserBalance() {
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().getUserBalance()
                .compose(RxScheduler.<ResponseModel<BalanceModel>>toMain())
                .as(mView.<ResponseModel<BalanceModel>>bindAutoDispose())
                .subscribe(new CommonObserver<BalanceModel>() {
                    @Override
                    public void onSuccess(BalanceModel data) {
                        String balance = data.getBalance();
                        if (TextUtils.isEmpty(balance)) balance = "0";
                        mView.getBalanceSuccess(balance);
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });

    }


    private void showPayCheckDialog(final UsagePayMode payMode, final Map<String, Object> object) {

        if (TextUtils.isEmpty(accountPayMoney)) {
            String type = String.valueOf((double) object.get("type"));
            //账户余额空了
            RepositoryFactory.getRemoteRepository().convert(type, (String) object.get("amount"))
                    .compose(RxScheduler.<ResponseModel<ConvertModel>>toMain())
                    .as(mView.<ResponseModel<ConvertModel>>bindAutoDispose())
                    .subscribe(new CommonObserver<ConvertModel>() {
                        @Override
                        public void onSuccess(ConvertModel data) {
                            String totalPrice = data.getTotalPrice();
                            accountPayMoney = totalPrice;
                            boolean isFingerEnable = isFingerEnable();
                            if (isFingerEnable) {
                                PayPresenter.this.payMode = payMode;
                                PayPresenter.this.payObj = object;
                                showFingerprintDialog(payMode, object);
                            } else {
                                showPayPasswordDiaog(payMode, object);
                            }
                        }

                        @Override
                        public void onError(ResponseModel data) {
//                        mView.hideLoading();
                            CommonToast.toast(data.getMsg());
                            if (dialog2.isShowing() && dialog2 != null) {
                                dialog2.dismiss();
                            }
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            CommonToast.toast(data.getMsg());
                            if (dialog2.isShowing() && dialog2 != null) {
                                dialog2.dismiss();
                            }
                        }
                    });
        } else {
            boolean isFingerEnable = isFingerEnable();
            if (isFingerEnable) {
                this.payMode = payMode;
                this.payObj = object;
                showFingerprintDialog(payMode, object);
            } else {
                showPayPasswordDiaog(payMode, object);
            }
        }

    }

    private boolean isFingerEnable() {
        UserInfoModel infoModel = UserCenter.getUserInfo();
        if (infoModel == null) return false;
        String uid = infoModel.getUId();
        FingerprintModel model = DBDaoFactory.getFingerprintDao().queryModel(uid);
        if (model == null) return false;
        return model.getFingerEnable();
    }

    private void showPayPasswordDiaog(final UsagePayMode payMode, final Map<String, Object> object) {
        int isSet = UserCenter.getUserInfo().getIsSetPayPsw();
        if (isSet != 1) {
//            CommonToast.toast(mView.getCurContext().getString(R.string.no_set_pay_password));
            showSetPayPwdDialog();
            return;
        }
        String price = String.valueOf(object.get("price"));
        Bundle bundle = new Bundle();
        if (TextUtils.isEmpty(accountPayMoney)) {
//            CommonToast.toast(mView.getCurContext().getString(R.string.loading));
            waitDataLoading = true;
            return;
        }
        bundle.putString(PayFragment.EXTRA_CONTENT, mView.getCurActivity().getString(R.string.pay).concat("：") + accountPayMoney);
        final PayFragment fragment = new PayFragment();
        fragment.setArguments(bundle);
        fragment.setPaySuccessCallBack(new PayPwdView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                String payPwd = StringUtil.getPassword(result);
                checkPayPasswoed(payPwd, fragment, payMode, object);
            }
        });
        fragment.show(mView.fragmentManager(), "Pay");
        waitDataLoading = false;
    }


    /**
     * 指纹识别认证
     */
    private void showFingerprintDialog(final UsagePayMode payMode, final Map<String, Object> object) {
        fingerDialog = new MaterialDialog.Builder(mView.getCurActivity())
                .customView(R.layout.dialog_finger, true)
                .backgroundColor(ContextCompat.getColor(mView.getCurContext(), R.color.trans))
                .build();
        fingerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = fingerDialog.getCustomView();
        if (view == null) return;
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        TextView tvPassword = view.findViewById(R.id.tvPassword);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        tvPrice.setText(accountPayMoney);
        if (TextUtils.isEmpty(accountPayMoney)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.loading));
            waitDataLoading = true;

            return;
        }
        tvPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerDialog.dismiss();
                showPayPasswordDiaog(payMode, object);
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerDialog.dismiss();
            }
        });

        initFingerpring(payMode, object);
        fingerDialog.show();
        waitDataLoading = false;
    }


    private void showSetPayPwdDialog() {

        dialog = new MaterialDialog.Builder(mView.getCurActivity())
                .title(R.string.attention)
                .content(R.string.hint_pay_password)
                .cancelable(false)
                .positiveText(R.string.go_set)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mView.getCurContext().startActivity(ModifyPayPwdActivity.newInstance(mView.getCurContext()));
                        dialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .negativeText(R.string.cancel).build();

        dialog.show();
    }


    private void initFingerpring(final UsagePayMode payMode, final Map<String, Object> object) {
        mFingerprintIdentify = new FingerprintIdentify(mView.getCurContext().getApplicationContext());
        mFingerprintIdentify.setSupportAndroidL(true);
        mFingerprintIdentify.setExceptionListener(new BaseFingerprint.ExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                Log.d("FingerprintIdentify", exception.getLocalizedMessage());
            }
        });
        mFingerprintIdentify.init();
        mFingerprintIdentify.startIdentify(10, new BaseFingerprint.IdentifyListener() {
            @Override
            public void onSucceed() {
                // 验证成功，自动结束指纹识别
                CommonToast.toast(mView.getCurContext().getString(R.string.auth_access));
                if (fingerDialog != null) fingerDialog.dismiss();
                fingerDialog.dismiss();
                payBalance(payMode, object);

            }

            @Override
            public void onNotMatch(int availableTimes) {
                // 指纹不匹配，并返回可用剩余次数并自动继续验证
                CommonToast.toast(mView.getCurContext().getString(R.string.auth_no_access));
            }

            @Override
            public void onFailed(boolean isDeviceLocked) {
                // 错误次数达到上限或者API报错停止了验证，自动结束指纹识别
                CommonToast.toast(mView.getCurContext().getString(R.string.busy_try_later));
                showPayPasswordDiaog(payMode, object);
            }

            @Override
            public void onStartFailedByDeviceLocked() {
                // 第一次调用startIdentify失败，因为设备被暂时锁定
                CommonToast.toast(mView.getCurContext().getString(R.string.busy_try_later));
                showPayPasswordDiaog(payMode, object);
            }
        });
    }

    public void userThreeScanPay(int payType, final Map<String, Object> map) {
        String amount = (String) map.get("amount");
        String payCode = (String) map.get("payCode");
        String type = (String) map.get("type");
        String outOrderId = (String) map.get("orderId");
        switch (payType) {
            case Constant.PayType.ACCOUNT:
                showPayCheckDialog(UsagePayMode.three_scan_pay, map);
                break;
            case Constant.PayType.ZFB:
                mView.showLoading();
                RepositoryFactory.getRemotePayRepository().scanByAli(amount, outOrderId, payCode, type)
                        .compose(RxScheduler.<ResponseModel<String>>toMain())
                        .as(mView.<ResponseModel<String>>bindAutoDispose())
                        .subscribe(new CommonObserver<String>() {
                            @Override
                            public void onSuccess(String url) {//成功跳转的地址
                                AliPayModel aliPayModel = new AliPayModel();
                                aliPayModel.setUrl(url);
                                AliPayUtil.with(mView.getCurActivity()).doPay(UsagePayMode.three_scan_pay, aliPayModel);
                                mView.hideLoading();
                            }

                            @Override
                            public void onError(ResponseModel data) {
                                CommonToast.toast(data.getMsg());
                                mView.hideLoading();
                            }

                            @Override
                            public void onNetError(ResponseModel data) {
                                CommonToast.toast(data.getMsg());
                                mView.hideLoading();
                            }
                        });
                break;
            case Constant.PayType.WX:
                mView.showLoading();
                RepositoryFactory.getRemotePayRepository()
                        .scanByWx(amount, outOrderId, payCode, type)
                        .compose(RxScheduler.<ResponseModel<WXPayModel>>toMain())
                        .as(mView.<ResponseModel<WXPayModel>>bindAutoDispose())
                        .subscribe(new CommonObserver<WXPayModel>() {
                            @Override
                            public void onSuccess(WXPayModel data) {
                                mView.hideLoading();
                                WXPayUtil.newInstance().doPay(UsagePayMode.three_scan_pay, data);
                            }

                            @Override
                            public void onError(ResponseModel data) {
                                mView.hideLoading();
                            }

                            @Override
                            public void onNetError(ResponseModel data) {
                                mView.hideLoading();
                            }
                        });
                break;
        }
    }


}
