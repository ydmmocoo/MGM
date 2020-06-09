package com.fjx.mg.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class PickImageDialog extends BottomPopupView {

    private Fragment fragment;
    private Activity activity;
    private int aspect_ratio_x, aspect_ratio_y;
    private LinearLayout llAvatar;
    private OnSelectDetaultAvatarListener selectAvatarListener;
    private AvatarAdapter avatarAdapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private boolean selectSingle = true;
    private boolean selected = false;
    private Context mContext;
    private List<String> list;

    public void withAspectRatio(int aspect_ratio_x, int aspect_ratio_y) {
        this.aspect_ratio_x = aspect_ratio_x;
        this.aspect_ratio_y = aspect_ratio_y;
    }

    public void setSelectAvatarListener(OnSelectDetaultAvatarListener selectAvatarListener) {
        this.selectAvatarListener = selectAvatarListener;
    }

    public void setDefaultAvatars(List<String> avatars) {
        list=avatars;
    }

    public void setSelectSingle(boolean selectSingle) {
        this.selectSingle = selectSingle;
    }

    public void setselectedList(List<LocalMedia> selectList) {
        this.selectList = selectList;
    }

    public PickImageDialog(@NonNull Context context) {
        super(context);
        mContext=context;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        llAvatar = findViewById(R.id.llAvatar);
        findViewById(R.id.tvTackPhone).setOnClickListener(clickListener);
        findViewById(R.id.tvSelectImage).setOnClickListener(clickListener);
        findViewById(R.id.tvCancle).setOnClickListener(clickListener);
        activity = ((Activity) mContext);
        if (selectAvatarListener!=null){
            llAvatar.setVisibility(VISIBLE);
        }

        avatarAdapter = new AvatarAdapter();
        RecyclerView recyclerView = findViewById(R.id.imageRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(avatarAdapter);
        avatarAdapter.setList(list);
        avatarAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (selectAvatarListener == null) return;
            selectAvatarListener.onSelect(avatarAdapter.getItem(position));
            dismiss();
        });
    }

    public PickImageDialog(@NonNull Context context, Fragment fragment) {
        super(context);
        mContext=context;
        this.fragment = fragment;
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
                    if (fragment != null) {
                        ImageSelectHelper.tackPhoto(fragment, aspect_ratio_x, aspect_ratio_y);
                    } else {
                        ImageSelectHelper.tackPhoto(activity, aspect_ratio_x, aspect_ratio_y);
                    }
                    break;
                case R.id.tvSelectImage:
                    if (fragment != null) {
                        if (selectSingle)
                            ImageSelectHelper.pickPhotoSingle(fragment, aspect_ratio_x, aspect_ratio_y);
                        else
                            ImageSelectHelper.pickPhotoMul(fragment);
                    } else {
                        if (selectSingle)
                            ImageSelectHelper.pickPhotoSingle(activity, aspect_ratio_x, aspect_ratio_y);
                        else
                            ImageSelectHelper.pickPhotoMul(activity, selectList);
                    }
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
