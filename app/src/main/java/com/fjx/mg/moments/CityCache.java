package com.fjx.mg.moments;

import com.library.common.utils.JsonUtil;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.models.CityCircleListModel;

/**
 * Author    by hanlz
 * Date      on 2019/11/13.
 * Description：
 */
public class CityCache {
    private final static String NAME = "city_cache_name";
    private final static String PUT_MODEL_KEY = "cityCircleListModel";
    private static SharedPreferencesUtil sp = SharedPreferencesUtil.name(NAME);

    private static CityCache instance;

    private CityCache() {
    }

    public static CityCache getInstance() {
        if (instance == null) {
            synchronized (CityCache.class) {
                if (instance == null) {
                    instance = new CityCache();
                }
            }
        }
        return instance;
    }

    /**
     * @param cityCircleListModel 需要保存的数据
     */
    public void putModel(CityCircleListModel cityCircleListModel) {
        String moderToString = JsonUtil.moderToString(cityCircleListModel);
        sp.put(PUT_MODEL_KEY, moderToString);
        sp.apply();
    }

    public CityCircleListModel getModel() {
        String string = sp.getString(PUT_MODEL_KEY, "");
        if (StringUtil.isEmpty(string)) {
            return null;
        }
        return JsonUtil.strToModel(string, CityCircleListModel.class);
    }

    public void deleteAll() {
        sp.clear();
    }

}
