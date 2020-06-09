package com.fjx.mg.network.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fjx.mg.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Author    by hanlz
 * Date      on 2019/10/22.
 * Description：
 */
public class MapDialogFragment extends DialogFragment {

    private Unbinder mUnbinder;
    private OnNavigationLinsenter mLinsenter;

    @BindView(R.id.tvBaiduMap)
    TextView mTvBaiduMap;

    @Override
    public void onStart() {
        super.onStart();
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
    }

    @OnClick({R.id.tvGoogleMap,R.id.tvGaodeMap, R.id.tvBaiduMap, R.id.tvCancel})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tvGoogleMap:
                if (mLinsenter != null) {
                    mLinsenter.onGoogleMapNavi();
                }
                dismiss();
                break;
            case R.id.tvGaodeMap:
                if (mLinsenter != null) {
                    mLinsenter.onGaodeMapNavi();
                }
                dismiss();
                break;
            case R.id.tvBaiduMap:
                if (mLinsenter != null) {
                    mLinsenter.onBaiduMapNavi();
                }
                dismiss();
                break;
            case R.id.tvCancel:
                dismiss();
                break;
            default:
        }
    }

    @Override
    public void onDestroy() {
        if (mUnbinder != null) mUnbinder.unbind();
        super.onDestroy();
    }

    public void setOnNavigationLinsenter(OnNavigationLinsenter linsenter) {
        this.mLinsenter = linsenter;
    }

    public interface OnNavigationLinsenter {
        void onGoogleMapNavi();

        void onGaodeMapNavi();

        void onBaiduMapNavi();

    }

}
