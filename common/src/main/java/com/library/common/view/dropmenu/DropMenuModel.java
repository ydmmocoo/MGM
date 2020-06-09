package com.library.common.view.dropmenu;

import java.util.List;

public class DropMenuModel {

    private String typeId;
    private String typeName;
    boolean isSelect;

    List<DropMenuModel> childList;

    public DropMenuModel(String typeId, String typeName, boolean isSelect, List<DropMenuModel> childList) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.isSelect = isSelect;
        this.childList = childList;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<DropMenuModel> getChildList() {
        return childList;
    }

    public void setChildList(List<DropMenuModel> childList) {
        this.childList = childList;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
