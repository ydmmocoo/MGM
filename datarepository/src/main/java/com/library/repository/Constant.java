package com.library.repository;

import com.library.repository.repository.RepositoryFactory;
import com.tencent.qcloud.uikit.TimConfig;

public class Constant {

//    public static boolean isRelease = false;

    public static final String TIM_LOG = "tim_log";
    public static final String HOST /**/ = getHost();//"http://www.messageglobal-online.com/";


    public static String getSearch() {
        String env = RepositoryFactory.getLocalRepository().getHostEnvironment();
//        if (TextUtils.equals("1", env)) {
        if (TimConfig.isRelease) {//http://47.97.159.184/invite/balanceQuery 测试服务器
            return "invite/search?ispType=";
        } else {
            return "invite/search?ispType=";//测试服务器
        }
    }

    public static String getHost() {
        if (TimConfig.isRelease) {
            return "https://www.messageglobal-online.com/";
        } else {
            return "http://139.9.38.218/";//测试服务器
        }
    }

    public static String getUrl(){
        if (TimConfig.isRelease){
            return "https://www.messageglobal-online.com/";
        } else {
            return "http://139.9.38.218/";//测试服务器
        }
    }

    //汇率查询域名
    public static final String IM_TRANSLATE_HOST = "https://service-p90qcght-1257101137.ap-shanghai.apigateway.myqcloud.com/";
    //百度翻译域名
    public static final String LAN_TRANSLATE_HOST = "http://api.fanyi.baidu.com/";

    public static final String Api_key = "1212121@#@ASFSDfTTYTYsfafafdfa";

    public static final String DOWNLOAD_URL = HOST.concat("invite/index");
    public static final String INVITE_URL = HOST.concat("invite/register?iv=");
    public static final String RED_ENVELOPE_URL = HOST.concat("invite/redEnvelope?oId=");
    public static float limitAmount = 1;

    public interface RecordType {
        //类型：1为电费，2为水费，3为网费
        int ELECT = 1;
        int WATER = 2;
        int NET = 3;

    }

    public interface RecordPhoneType {
        //类型：1为话费，2为流量
        int PHONE = 1;
        int DATA = 2;
    }

    /**
     * 聊天转账类型
     */
    public interface ImTransType {
        //类型：1:转账，2：红包
        String ACCOUNT = "1";
        String REDPACKET = "2";
    }

    public interface PayType {
        int ACCOUNT = 1;
        int WX = 2;
        int ZFB = 3;
        int BANKCARD = 4;
    }

    public interface LoginType {
        //1:微信，2:支付宝
        String WX = "1";
        String ZFB = "2";
        String FACEBOOK = "3";

    }


    public interface ERROR {
        int NO_USER = 10005;
    }

    public interface Gender {
        String man = "1";
        String woman = "2";
    }
    public interface SuccessfullShowType {
        int WD = 1;
        int RC = 2;
    }
    /**
     * 获取服务费类型
     */
    public interface ConvertType {
        String USERWITHDRAWAL = "1";//用户提现
        String AGENTWITHDRAWAL = "2";//代理商或者网点提现
    }
    /**
     * 支付状态类型
     */
    public interface PayStatus {
        String SUCC = "1";//付款成功
        String IN = "2";//支付中
        String FAILE = "3";//支付失败
    }
}
