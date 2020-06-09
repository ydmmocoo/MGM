package com.library.common.view.dropmenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DropMenuHelper {

    private List<DrapMenuTab> drapMenuTabs;

    private static class DropMenuHelperHolder {
        private static final DropMenuHelper instance = new DropMenuHelper();
    }

    private DropMenuHelper() {
        drapMenuTabs = new ArrayList<>();
    }


    public static DropMenuHelper getInstance() {
        return DropMenuHelperHolder.instance;
    }

    public DropMenuHelper add(DrapMenuTab drapMenuTab) {
        drapMenuTabs.add(drapMenuTab);
        return this;
    }

    public List<DrapMenuTab> getDrapMenuTabs() {
        return drapMenuTabs;
    }

    public void release() {
        drapMenuTabs.clear();
    }


}
