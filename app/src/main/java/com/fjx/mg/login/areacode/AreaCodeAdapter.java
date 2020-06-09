package com.fjx.mg.login.areacode;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.repository.models.AreaCodeSectionModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AreaCodeAdapter extends BaseSectionQuickAdapter<AreaCodeSectionModel, BaseViewHolder> {

    public AreaCodeAdapter(List<AreaCodeSectionModel> data) {
        super(R.layout.item_contact_section, R.layout.item_area_code, data);
    }

    @Override
    protected void convertHeader(@NotNull BaseViewHolder helper, @NotNull AreaCodeSectionModel item) {
        helper.setText(R.id.tvSection, item.getHeader());
    }

    @Override
    protected void convert(BaseViewHolder helper, AreaCodeSectionModel item) {
        helper.setText(R.id.tvArea, item.getData().getName() + " (" + item.getData().getEn() + ")");
        helper.setText(R.id.tvCode, "+" + item.getData().getTel());
    }
}
