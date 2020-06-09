package com.fjx.mg.dialog;

import android.content.Context;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.fjx.mg.R;
import com.fjx.mg.food.adapter.LvFilterAdapter;
import com.lxj.xpopup.impl.PartShadowPopupView;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/6/5.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class FilterDialog extends PartShadowPopupView {

    private Context mContext;
    private OnFilterClickListener mListener;
    private List<String> mList;

    public FilterDialog(@NonNull Context context,List<String> list) {
        super(context);
        mContext=context;
        mList=list;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_filter;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        ListView lv=findViewById(R.id.lv_content);
        LvFilterAdapter adapter=new LvFilterAdapter(mContext,mList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            mListener.filterClick(position);
        });
    }

    public void setOnFilterClickListener(OnFilterClickListener listener){
        mListener=listener;
    }

    public interface OnFilterClickListener{
        void filterClick(int pos);
    }
}
