package com.fjx.mg.pay;

import androidx.fragment.app.FragmentManager;

import com.common.paylibrary.model.UsagePayMode;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.common.view.editdialog.PayFragment;

import java.util.Map;


public interface PayContract {

    interface View extends BaseView {
        FragmentManager fragmentManager();

        /**
         * 金钱换算
         *
         * @param type 类型：1为人民币转换，2为服务费及人民币换算
         */
        void convertMoneySuccess(String type, String price, String servicePrice, String totalPrice);


        /**
         * 支付宝红包转账成功
         */
        void transferSuccess(Object obj);

        void getBalanceSuccess(String balance);

        double getBalance();



    }

    abstract class Presenter extends BasePresenter<PayContract.View> {

        Presenter(PayContract.View view) {
            super(view);
        }

        abstract void convertMoney(String type, String price);


        /**
         * 聊天：支付宝转账或者发红包
         *
         * @param map
         */
        abstract void transferAli(Map<String, Object> map);

        /**
         * 聊天：微信转账或者发红包
         *
         * @param map
         */
        abstract void transferWX(Map<String, Object> map);

        /**
         * 聊天：余额转账或者发红包
         *
         * @param map
         */
        abstract void transferByBalance(Map<String, Object> map);


        /**
         * 统一方法，聊天相关发起转账或者红包支付操纵
         *
         * @param payType
         */
        abstract void finalTransferPay(int payType, Map<String, Object> map);

        /*=========================================分割线================================================*/
        /*=========================================分割线================================================*/
        /*=========================================分割线================================================*/


        /**
         * 手机流量充值--微信
         *
         * @param map
         */
        abstract void phoneWxRecharge(Map<String, Object> map);

        /**
         * 手机流量充值--支付宝
         *
         * @param map
         */
        abstract void phoneZFBRecharge(Map<String, Object> map);

        /**
         * 手机流量充值--余额
         *
         * @param map
         */
        abstract void phoneBalanceRecharge(Map<String, Object> map);

        /**
         * 统一方法，聊天相关发起转账或者红包支付操纵
         *
         * @param payType
         */
        abstract void finalPhoneRechargePay(int payType, Map<String, Object> map);


        /*=========================================分割线================================================*/
        /*=========================================分割线================================================*/
        /*=========================================分割线================================================*/


        /**
         * 支付宝转账或者发红包
         *
         * @param map
         */
        abstract void transferDirecAli(Map<String, Object> map);

        /**
         * 微信转账或者发红包
         *
         * @param map
         */
        abstract void transferDirecWX(Map<String, Object> map);

        /**
         * 余额转账或者发红包
         *
         * @param map
         */
        abstract void transferDirecByBalance(Map<String, Object> map);


        /**
         * 统一方法，发起直接转账
         *
         * @param payType
         */
        abstract void finalTransferDirecPay(int payType, Map<String, Object> map);

        /*=========================================分割线================================================*/
        /*=========================================分割线================================================*/
        /*=========================================分割线================================================*/


        /**
         * 电水网支付宝充值
         *
         * @param map
         */
        abstract void ewnRechargeAli(Map<String, Object> map);

        /**
         * 电水网微信充值
         *
         * @param map
         */
        abstract void ewnRechargeWX(Map<String, Object> map);

        /**
         * 电水网余额充值
         *
         * @param map
         */
        abstract void ewnRechargeBalance(Map<String, Object> map);


        /**
         * 统一方法，发起电水网充值
         *
         * @param payType
         */
        abstract void finalEWNnRechargePay(int payType, Map<String, Object> map);

        /*=========================================分割线================================================*/
        /*=========================================分割线================================================*/
        /*=========================================分割线================================================*/


        /**
         * 余额充值：支付宝
         *
         * @param map
         */
        abstract void balanceRechargeAli(Map<String, Object> map);

        /**
         * 余额充值：微信
         *
         * @param map
         */
        abstract void balanceRechargeWX(Map<String, Object> map);

        abstract void finalbalanceRechargePay(int payType, Map<String, Object> map);

        abstract void checkPayPasswoed(String password, PayFragment fragment, UsagePayMode payMode, Map<String, Object> object);

        /*=========================================分割线================================================*/
        /*=========================================分割线================================================*/
        /*=========================================分割线================================================*/

        /**
         * 购买等级：支付宝
         *
         * @param map
         */
        abstract void upgradeByAli(Map<String, Object> map);

        /**
         * 购买等级：微信
         *
         * @param map
         */
        abstract void upgradeByWx(Map<String, Object> map);

        /**
         * 购买等级：余额
         *
         * @param map
         */
        abstract void upgradeByBalance(Map<String, Object> map);

        abstract void finalUpgradePay(int payType, Map<String, Object> map);


        abstract void getUserBalance();



    }
}
