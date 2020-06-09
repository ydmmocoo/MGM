package com.fjx.mg.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.dialog.adapter.AvatarAdapter;
import com.library.common.utils.ImageSelectHelper;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.core.BottomPopupView;


import java.util.ArrayList;
import java.util.List;

public class PickAllTypeDialog extends BottomPopupView {

    private Activity activity;
    private int aspect_ratio_x, aspect_ratio_y;
    private OnSelectDetaultAvatarListener selectAvatarListener;
    private AvatarAdapter avatarAdapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private boolean all = false;
private Context mContext;

    public void setselectedList(List<LocalMedia> selectList) {
        this.selectList = selectList;
    }

    public void setselectedType(boolean all) {
        this.all = all;
    }

    public PickAllTypeDialog(@NonNull Context context) {
        super(context);
        mContext=context;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.tvTackPhone).setOnClickListener(clickListener);
        findViewById(R.id.tvSelectImage).setOnClickListener(clickListener);
        findViewById(R.id.tvCancle).setOnClickListener(clickListener);
        activity = ((Activity) mContext);

        avatarAdapter = new AvatarAdapter();
        RecyclerView recyclerView = findViewById(R.id.imageRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(avatarAdapter);
        avatarAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (selectAvatarListener == null) return;
                selectAvatarListener.onSelect(avatarAdapter.getItem(position));
                dismiss();
            }
        });
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_pick_image;
    }

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            switch (v.getId()) {
                case R.id.tvTackPhone:
                    ImageSelectHelper.tackPhoto(activity, aspect_ratio_x, aspect_ratio_y);
//                    ImageSelectHelper.tackAllType(activity, aspect_ratio_x, aspect_ratio_y);
                    break;
                case R.id.tvSelectImage:
                    ImageSelectHelper.pickAllTypeMul(activity, selectList);
                    break;
                case R.id.tvCancle:
                    break;
            }
        }
    };

    public interface OnSelectDetaultAvatarListener {
        void onSelect(String imageUrl);
    }
}
