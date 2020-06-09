package com.fjx.mg.network.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Author    by hanlz
 * Date      on 2019/11/5.
 * Description：网点筛选条件dialog
 */
public class FilterDialogFragment extends DialogFragment {

    private Unbinder mUnbinder;
    private OnFilterListener mListener;

    @BindView(R.id.etMoney)
    EditText mEtMoney;

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
        return inflater.inflate(R.layout.dialog_fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
    }

    @OnClick({R.id.tvReset, R.id.tvConfirm})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tvReset://重置
                mEtMoney.setText("");
                mEtMoney.setHint(getString(R.string.input_conversion_money));
                break;
            case R.id.tvConfirm://确定
                String money = mEtMoney.getText().toString().trim();
                if (StringUtil.isEmpty(money)) {
                    CommonToast.toast(getString(R.string.input_conversion_money));
                    return;
                }
                if (mListener != null) {
                    mListener.onFilter(money);
                }
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

    public interface OnFilterListener {
        void onFilter(String money);
    }

    public void setOnFilterListener(OnFilterListener listener) {
        this.mListener = listener;
    }
}
