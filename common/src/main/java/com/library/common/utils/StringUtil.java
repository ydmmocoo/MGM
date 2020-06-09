package com.library.common.utils;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 密码加密后传给后台
     *
     * @param psw
     * @return
     */
    public static String getPassword(String psw) {
        psw = psw.concat("webiwokeruser");
        psw = StringUtil.md5(psw);
        return StringUtil.md5(psw);
    }

    /**
     * 文字高亮
     *
     * @param text 文字内容
     * @param key  要高亮的文字
     * @return
     */
    public static SpannableStringBuilder setTextHightLines(String text, String key, int color) {
        int start = 0, end = 0;
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        if (text.toLowerCase().contains(key.toLowerCase())) {
            start = text.toLowerCase().indexOf(key.toLowerCase());
            end = start + key.length();
            style.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return style;
    }


    /**
     * 文字高亮
     *
     * @param text  文字内容
     * @param start 要高亮的文字
     * @param end   要高亮的文字
     * @return
     */
    public static SpannableStringBuilder setTextHightLines(String text, int start, int end, int color) {
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }


    /**
     * 是否是数字
     *
     * @param text
     * @return
     */
    public static boolean isNum(String text) {
        Pattern pattern = Pattern.compile("^[-+]?[0-9]");
        return pattern.matcher(text).matches();
    }

    /**
     * 隐藏手机号中间部分
     *
     * @param phoneNum
     * @return
     */
    public static String phoneText(String phoneNum) {
        //...
        if (phoneNum == null) return "";
        if (phoneNum.length() <= 7) return phoneNum;
        else if (phoneNum.length() <= 10)
            return phoneNum.substring(0, 3).concat("***") + phoneNum.substring(phoneNum.length() - 3);
        else
            return phoneNum.substring(0, 3).concat("****") + phoneNum.substring(phoneNum.length() - 4);
    }


    /**
     * string 转成float
     *
     * @return
     */
    public static float convertToFloat(String number, float defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(number);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    @SuppressLint("NewApi")
    public static void copyClip(String text) {
        if (TextUtils.isEmpty(text)) return;
        ClipData clipData = ClipData.newPlainText("copy", text);
        ClipboardManager cmb = (ClipboardManager) ContextManager.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(clipData);
    }


    private boolean phoneLegal(String areaCode, String phone) {
        if (TextUtils.equals("86", areaCode)) {
            return phone.length() == 11;
        } else if (TextUtils.equals("261", areaCode)) {

        }
        return false;
    }

    public static boolean phoneLegal(String phone) {
        String reg = "^(32|33|34)\\d{7}$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(phone);
        return m.find();
    }


    /**
     * 自定义获取设备id
     *
     * @return
     */
    public static String getDeviceId() {
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        return m_szDevIDShort;
    }

    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String str) {
        return !TextUtils.isEmpty(str);
    }

    public static boolean equalsIgnoreCase(String s1, String s2) {
        if (isNotEmpty(s1) && isNotEmpty(s2)) {
            return s1.equalsIgnoreCase(s2);
        }
        return false;
    }

    public static boolean equals(String s1, String s2) {
        if (isNotEmpty(s1) && isNotEmpty(s2)) {
            return s1.equals(s2);
        }
        return false;
    }

    public static String add(String v1, String v2) {
        if (isEmpty(v1) && isEmpty(v2)) {
            return "0";
        } else if (isEmpty(v1) && isNotEmpty(v2)) {
            return v2;
        } else if (isNotEmpty(v1) && isEmpty(v2)) {
            return v1;
        }
        Long i1 = Long.valueOf(v1);
        Long i2 = Long.valueOf(v2);
        return String.valueOf(i1 + i2);
    }

    public static String subtract(String v1, String v2, Long limit) {
        if (isEmpty(v1) && isEmpty(v2)) {
            return "0";
        } else if (isEmpty(v1) && isNotEmpty(v2)) {
            return "0";
        } else if (isNotEmpty(v1) && isEmpty(v2)) {
            return v1;
        }
        Long i1 = Long.valueOf(v1);
        Long i2 = Long.valueOf(v2);
        i1 = i1 - i2;
        if (i1 <= limit) {
            i1 = limit;
        }
        return String.valueOf(i1);
    }

    public static String multiply(String v1, String v2) {
        if (isEmpty(v1) || isEmpty(v2)) {
            return "0";
        } else if (Double.valueOf(v1) == 0 || Double.valueOf(v2) == 0) {
            return "0";
        } else {
            Long l1 = Long.valueOf(v1);
            Long l2 = Long.valueOf(v2);
            l1 = l1 * l2;
            return String.valueOf(l1);
        }
    }

    public static String divide(String v1, String v2) {
        return "";
    }

}
