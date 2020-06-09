package com.fjx.mg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fjx.mg.R;
import com.fjx.mg.food.adapter.LvDeliveryTimeAdapter;
import com.lxj.xpopup.core.BottomPopupView;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/6/7.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class DeliveryTimePop extends BottomPopupView {

    private TextView tvTitle;
    private TextView tvCancel;
    private ListView lvTime;

    private Context context;
    private String title;
    private LvDeliveryTimeAdapter adapter;
    private List<String> timeList;

    private OnTimeClickListener mListener;

    public DeliveryTimePop(@NonNull Context context, String title, List<String> timeList) {
        super(context);
        this.context=context;
        this.title = title;
        this.timeList = timeList;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pop_bottom_delivery_time;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tvTitle = findViewById(R.id.tv_title);
        tvCancel = findViewById(R.id.tv_cancel);
        lvTime = findViewById(R.id.lv_time);

        tvTitle.setText(title);
        adapter=new LvDeliveryTimeAdapter(context,timeList);
        lvTime.setAdapter(adapter);
        //取消
        tvCancel.setOnClickListener(v -> dismiss());

        lvTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onClickTime(timeList.get(position));
                dismiss();
            }
        });
    }

    public void setOnTimeClickListener(OnTimeClickListener listener){
        mListener=listener;
    }

    public interface OnTimeClickListener{
        void onClickTime(String time);
    }
}
