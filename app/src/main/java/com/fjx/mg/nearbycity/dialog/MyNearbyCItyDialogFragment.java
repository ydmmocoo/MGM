package com.fjx.mg.nearbycity.dialog;


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
import com.library.common.constant.IntentConstants;
import com.library.common.utils.StringUtil;
import com.library.repository.models.MyCityCircleListModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author    by hanlz
 * Date      on 2019/10/17.
 * Description：我的同城--发布开关/修改置顶
 */
public class MyNearbyCItyDialogFragment extends DialogFragment {

    private MyCityCircleListModel mModel;
    private OnSwitchListener mListener;

    @BindView(R.id.tvSwitch)
    TextView mTvSwitch;
    @BindView(R.id.tvSetTop)
    TextView mTvSetTop;

    public static MyNearbyCItyDialogFragment getInstance(MyCityCircleListModel model) {
        MyNearbyCItyDialogFragment fragment = new MyNearbyCItyDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentConstants.TYPE_LIST, model);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }
        mModel = arguments.getParcelable(IntentConstants.TYPE_LIST);
    }

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
        View view=inflater.inflate(R.layout.pop_my_nearby_city, container, false);
        ButterKnife.bind(this, view);
        if (StringUtil.equals("1", mModel.getStatus())) {
            mTvSwitch.setText("关闭");
        } else {
            mTvSwitch.setText("开启");
        }
        return view;
    }

    @OnClick({R.id.tvSwitch, R.id.tvSetTop})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tvSwitch://关闭/开启同城
                if (mListener != null) {
                    mListener.onSwitch(mModel, StringUtil.equals("1", mModel.getStatus()) ? "4" : "1");
                    dismiss();
                }
                break;
            case R.id.tvSetTop://修改置顶
                if (mListener != null) {
                    mListener.onSetTop(mModel.getcId());
                }
                dismiss();
                break;
            default:
        }
    }

    public interface OnSwitchListener {
        void onSwitch(MyCityCircleListModel model, String status);

        void onSetTop(String cId);
    }


    public void setOnSwitchListener(OnSwitchListener switchListener) {
        mListener = switchListener;
    }


}
