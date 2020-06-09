package com.fjx.mg.main.more;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.AppModel;

public class AllAppAdapter extends BaseAdapter<AppModel> {

    private Context context;

    public AllAppAdapter(Context context) {
        super(R.layout.item_app);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, AppModel item) {
        TextView tvContent = helper.getView(R.id.tvApp);
        Drawable drawable = context.getResources().getDrawable(item.getId());
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  //此为必须写的
        tvContent.setCompoundDrawables(null, drawable, null, null);
        tvContent.setText(item.getAppname());
    }


}
