package com.fjx.mg.me.wallet;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fjx.mg.R;
import com.fjx.mg.network.KiosqueNetworkActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tencent.qcloud.uikit.common.utils.ScreenUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletTipsDialogFragment extends DialogFragment {

    private OnShowDialogListener mOnShowDialogListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_wallet_tips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    public void setOnShowDialogListener(OnShowDialogListener listener){
        mOnShowDialogListener = listener;
    }

    @OnClick({R.id.tvCancel, R.id.btnSearchNearbyNetwork})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tvCancel://取消
                dismiss();
                break;
            case R.id.btnSearchNearbyNetwork://确定
                //搜索网点
                dismiss();
//                startActivity(KiosqueNetworkActivity.newIntent(getContext(), "4"));
                GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
                int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(getActivity());
                if (resultCode != ConnectionResult.SUCCESS) {
//                    if (googleApiAvailability.isUserResolvableError(resultCode)) {
//                        googleApiAvailability.getErrorDialog(getActivity(), resultCode, 2404).show();
//                    }
                    //未安装谷歌服务
                    final Dialog dialog = new Dialog(getActivity());
                    View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_gms_tips_layout, null);
                    v.findViewById(R.id.tvOk).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();

                        }
                    });
                    v.findViewById(R.id.tvContactService).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            if (mOnShowDialogListener!=null){
                                mOnShowDialogListener.onShow();
                            }
                        }
                    });
                    dialog.setContentView(v);
                    dialog.show();
                    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                    int w = ScreenUtil.getScreenWidth(getActivity());
                    params.width = w / 5 * 4;
                    dialog.getWindow().setAttributes(params);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                } else {
                    startActivity(KiosqueNetworkActivity.newIntent(getActivity(), "4"));
                }
                break;
        }
    }

    public interface OnShowDialogListener{
        void onShow();
    }


}