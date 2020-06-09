package com.fjx.mg.setting.address.list;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.AddressModel;

public class AddressAdapter extends BaseAdapter<AddressModel.AddressListBean> {

    private OnClickEditListener mListener;

    public AddressAdapter() {
        super(R.layout.item_address);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressModel.AddressListBean item) {
        helper.setText(R.id.tvName, item.getName().concat(" ").concat(item.getSex().equals("1")?
                getContext().getResources().getString(R.string.mr):getContext().getResources().getString(R.string.miss)));
        helper.setText(R.id.tvPhone, item.getPhone());
        helper.setText(R.id.tvAddress, item.getAddress().concat(item.getRoomNo()));
        helper.getView(R.id.iv_edit).setOnClickListener(v -> mListener.clickEdit(helper.getLayoutPosition()));
    }

    public void setOnClickEditListener(OnClickEditListener listener){
        mListener=listener;
    }

    public interface OnClickEditListener{
        void clickEdit(int position);
    }
}
