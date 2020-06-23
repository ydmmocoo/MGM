package com.library.common.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

public class MulLanguageUtil {
    private static final String LANGUAGE_FILE = "language_file";
    private static final String LOCAL_LANGUAGE = "local_Language";


    /**
     * 中文简体
     */
    public static final Locale LOCALE_CHINESE = Locale.SIMPLIFIED_CHINESE;


    /**
     * 中文繁体
     */
    public static final Locale LOCALE_CHINESE_TW = Locale.TRADITIONAL_CHINESE;
    /**
     * 英文
     */
    public static final Locale LOCALE_ENGLISH = Locale.ENGLISH;
    public static final Locale LOCALE_FRENCH = Locale.FRENCH;


    /**
     * 保存当前选择的语言，开始默认都是中文的
     */
    private static void saveLocalLanguage(Locale local) {
        SharedPreferencesUtil.name(LANGUAGE_FILE).put(LOCAL_LANGUAGE, JsonUtil.moderToString(local)).apply();
    }

    /**
     * 获取本地保存的语言选择
     *
     * @return
     */
    public static Locale getLocalLanguage() {
        String str = SharedPreferencesUtil.name(LANGUAGE_FILE).getString(LOCAL_LANGUAGE, "");
        if (TextUtils.isEmpty(str)){
            return Locale.getDefault();
        }
        return JsonUtil.strToModel(str, Locale.class);
    }

    public static boolean updateLocale(Locale locale) {

        if (needUpdateLocale(locale)) {
            setLanguage(locale);
            saveLocalLanguage(locale);
            return true;
        }
        return false;
    }


    public static void setLanguage(Locale locale) {
        Context context = ContextManager.getContext();
        Configuration configuration = context.getResources().getConfiguration();

        //适配android 8.0之后无法切换简繁体的问题
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            configuration.setLocale(locale);
//            configuration.setLocales(new LocaleList(locale));
//            context = context.createConfigurationContext(configuration);
//            ContextManager.init(context);
////            resources.updateConfiguration(config, dm);
//        }
        configuration.setLocale(locale);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        context.getResources().updateConfiguration(configuration, displayMetrics);
    }

    public static boolean needUpdateLocale(Locale newUserLocale) {
        return newUserLocale != null && getLocalLanguage() != null && !getLocalLanguage().equals(newUserLocale);
    }

    public static void initAppLanguage() {
        Locale locale = getLocalLanguage();
        setLanguage(locale);
    }
}
