package com.fjx.mg.login.areacode;

import android.content.res.AssetManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.library.common.utils.JsonUtil;
import com.library.common.utils.LanguageConvent;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.AreaCodeModel;
import com.library.repository.models.AreaCodeSectionModel;
import com.library.repository.models.RemoteAreaCode;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AreaCodePresenter extends AreaCodeContract.Presenter {

    private List<AreaCodeModel> areaCodeList;

    AreaCodePresenter(AreaCodeContract.View view) {
        super(view);
        String areacodes = getJson();
        areaCodeList = JsonUtil.jsonToList(areacodes, AreaCodeModel.class);
    }

    @Override
    void getAreaCodeList(List<AreaCodeModel> originList) {
        if (originList == null) originList = areaCodeList;
        List<AreaCodeSectionModel> list = new ArrayList<>();
        Map<String, List<AreaCodeModel>> map = new HashMap<>();
        for (AreaCodeModel areaModel : originList) {

            String name = areaModel.getName();
            String firstChar = LanguageConvent.getFirstChar(name);
            if (map.containsKey(firstChar)) {
                List<AreaCodeModel> mapList = map.get(firstChar);
                mapList.add(areaModel);
            } else {
                List<AreaCodeModel> mapList = new ArrayList<>();
                mapList.add(areaModel);
                map.put(firstChar, mapList);
            }
        }


        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String section = it.next();
            List<AreaCodeModel> areaList = map.get(section);
            list.add(new AreaCodeSectionModel(true, section));
            for (AreaCodeModel areaCode : areaList) {
                list.add(new AreaCodeSectionModel(false,section,areaCode));
            }
        }

        List<String> headers = new ArrayList<>(map.keySet());

        mView.showAreaCodeList(list, headers);
    }


    @Override
    void searchWatcher(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (TextUtils.isEmpty(text)) {
                    getAreaCodeList(null);
                } else {
                    searchList(text);
                }
            }
        });

    }

    @Override
    void getNationList() {
        if (mView == null) return;
        RepositoryFactory.getRemoteRepository().getNationList(1)
                .compose(RxScheduler.<ResponseModel<RemoteAreaCode>>toMain())
                .as(mView.<ResponseModel<RemoteAreaCode>>bindAutoDispose())
                .subscribe(new CommonObserver<RemoteAreaCode>() {
                    @Override
                    public void onSuccess(RemoteAreaCode data) {
                        if (data == null) return;
                        mView.showNationList(data.getHotList());

                    }

                    @Override
                    public void onError(ResponseModel data) {

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }

    private void searchList(String text) {

        List<AreaCodeModel> list = new ArrayList<>();
        for (AreaCodeModel model : areaCodeList) {
            if (JsonUtil.moderToString(model).contains(text)) {
                list.add(model);
            }
        }
        getAreaCodeList(list);
    }


    private String getJson() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assets = mView.getCurContext().getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assets.open("phoneAreaCode.json")));
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
