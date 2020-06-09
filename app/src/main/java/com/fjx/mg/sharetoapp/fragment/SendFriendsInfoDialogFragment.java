package com.fjx.mg.sharetoapp.fragment;

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
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fjx.mg.R;
import com.library.common.constant.IntentConstants;
import com.tencent.qcloud.uikit.business.chat.model.CacheUtil;
import com.tencent.qcloud.uikit.business.session.view.SessionIconView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author    by hanlz
 * Date      on 2019/11/6.
 * Description：
 */
public class SendFriendsInfoDialogFragment extends DialogFragment {

    @BindView(R.id.session_icon)
    SessionIconView mSessionIcon;
    @BindView(R.id.etLeaveAMessage)
    EditText mEtLeaveAMessage;
    @BindView(R.id.tvContent)
    TextView mTvContent;
    @BindView(R.id.session_title)
    TextView mTvSessionTitle;
    private onDialogFragmentClickListener onListener;

    @Override
    public void onStart() {
        super.onStart();
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = (int) (width * 0.8);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_send_friends_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            String title = getArguments().getString(IntentConstants.FLAG);
            mTvContent.setText(title);
            mTvSessionTitle.setText(getArguments().getString(IntentConstants.NICKNAME));
            String url = CacheUtil.getInstance().userAvatar(getArguments().getString(IntentConstants.USER_AVATAR));
            mSessionIcon.setIcon(url);
        }
    }

    @OnClick({R.id.btnCancel, R.id.btnConfirm})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.btnCancel://取消
                dismiss();
                break;
            case R.id.btnConfirm://确定
                if (onListener != null) {
                    onListener.onClick(this, mEtLeaveAMessage.getText().toString());
                }
                dismiss();
                break;
        }
    }

    public interface onDialogFragmentClickListener {
        void onClick(DialogFragment dialogFragment, String say);
    }

    public void setonDialogFragmentClickListener(onDialogFragmentClickListener listener) {
        this.onListener = listener;
    }
}
