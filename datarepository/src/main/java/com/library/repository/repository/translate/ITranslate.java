package com.library.repository.repository.translate;

import com.library.repository.models.LanTranslateResultM;
import com.library.repository.repository.translate.model.RateResultModel;
import com.library.repository.repository.translate.model.TranslateCurrencyM;
import com.library.repository.repository.translate.rate.TranslateListener;

import java.util.List;

public interface ITranslate {


    /**
     * 获取可以货币类型列表
     *
     * @param listener
     */
    void getCurrency(TranslateListener<List<TranslateCurrencyM>> listener);


    /**
     * 转换汇率
     *
     * @param amount   钱
     * @param from
     * @param to
     * @param listener
     */
    void convert(String amount, String from, String to, TranslateListener<RateResultModel> listener);


    /**
     * @param q    请求翻译
     * @param from 语言类型
     * @param to   语言类型
     */
    void translate(String q, String from, String to, TranslateListener<LanTranslateResultM> listener);
}
