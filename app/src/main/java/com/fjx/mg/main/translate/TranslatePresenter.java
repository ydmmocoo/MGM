package com.fjx.mg.main.translate;

import android.content.res.AssetManager;
import android.text.TextUtils;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.JsonUtil;
import com.library.repository.models.LanTranslateResultM;
import com.library.repository.models.TranslateLanModel;
import com.library.repository.repository.RepositoryFactory;
import com.library.repository.repository.translate.rate.TranslateListener;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class TranslatePresenter extends TranslateContract.Presenter {

    private List<TranslateLanModel> fromList = new ArrayList<>();
    private List<TranslateLanModel> toList = new ArrayList<>();

    private List<String> showFromList = new ArrayList<>();
    private List<String> showToList = new ArrayList<>();

    TranslatePresenter(TranslateContract.View view) {
        super(view);
    }

    @Override
    void initData() {

        Map<String, String> map = RepositoryFactory.getLocalRepository().getLastTranslate();
        String fromText = map.get("fromText");
        String from = map.get("from");
        String toText = map.get("toText");
        String to = map.get("to");
        if (TextUtils.isEmpty(fromText)) {
            fromText = mView.getCurContext().getString(R.string.translate_fra);
            from = "fra";
        }

        if (TextUtils.isEmpty(toText)) {
            toText = mView.getCurContext().getString(R.string.translate_zh);
            to = "zh";
        }
        mView.initData(fromText, from, toText, to);

    }

    @Override
    void translate(String content, String from, String to) {
        mView.showLoading();
        RepositoryFactory.getTranslateApi().translate(content, from, to,
                new TranslateListener<LanTranslateResultM>() {
                    @Override
                    public void onSuccess(LanTranslateResultM lanTranslateM) {
                        mView.hideLoading();
                        mView.showTranslateResult(lanTranslateM);

                    }

                    @Override
                    public void onError(String code) {
                        mView.hideLoading();
//                        CommonToast.toast(code);

                    }
                });
    }

    @Override
    void showlanguageDialog(final boolean isFrom) {
        if (isFrom) {
            if (fromList.isEmpty()) {
                fromList = JsonUtil.jsonToList(getJson(), TranslateLanModel.class);
                for (TranslateLanModel m : fromList) {
                    showFromList.add(m.getType());
                }
            }
        } else {
            //被转化的类型不能是auto类型
            if (toList.isEmpty()) {
                toList = JsonUtil.jsonToList(getJson(), TranslateLanModel.class);
                toList.remove(0);
                for (TranslateLanModel m : toList) {
                    showToList.add(m.getType());
                }
            }
        }

        //list转成String[]
        String[] strings = new String[isFrom ? showFromList.size() : showToList.size()];
        strings = isFrom ? showFromList.toArray(strings) : showToList.toArray(strings);

        new XPopup.Builder(mView.getCurContext())
                .maxHeight(DimensionUtil.getScreenHight() / 2)
                .asCenterList(mView.getCurActivity().getString(R.string.select), strings,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                mView.showLanguage(isFrom, isFrom ? fromList.get(position) : toList.get(position));
                            }
                        })
                .show();
    }


    private String getJson() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assets = mView.getCurContext().getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assets.open("TranslateLanguages.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}
