package com.common.paylibrary.model;


/**
 * 发起支付的种类
 */
public enum UsagePayMode {
    im_transfer,//im转账
    im_redpacket,//im红包
    mg_transfer,//平台转账
    phone_recharge,//手机充值
    data_recharge,//流量充值
    water_recharge,//水费充值
    net_recharge,//网费充值
    elect_recharge,//电费充值
    recharge_balance,//余额充值
    levle_recharge,//购买会员等级
    question_pay,//有偿提问支付
    nearby_city_pay,//发布同城支付
    nearby_city_set_top_pay,//修改同城置顶支付
    agent_shop,//给代理商店铺充值
    three_scan_pay,//第三方店铺扫码支付
    im_group_red_packet,//群红包
    food_pay
}
