package com.tencent.qcloud.uikit.common;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Window;

import com.afollestad.materialdialogs.MaterialDialog;

public class BaseFragment extends Fragment {

    private MaterialDialog mDialog;

    public void forward(Fragment fragment, boolean hide) {
        forward(getId(), fragment, null, hide);
    }

    public void forward(int viewId, Fragment fragment, String name, boolean hide) {
        FragmentTransaction trans = getChildFragmentManager().beginTransaction();
        if (hide) {
            trans.hide(this);
            trans.add(viewId, fragment);
        } else {
            trans.replace(viewId, fragment);
        }

        trans.addToBackStack(name);
        trans.commitAllowingStateLoss();
    }

    public void backward() {
        getFragmentManager().popBackStack();
    }

    public void showLoading() {
        if (mDialog == null) {
            mDialog = new MaterialDialog.Builder(getActivity())
                    .backgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent))
                    .customView(com.library.common.R.layout.dialog_loading, false)
                    .build();
            Window window = mDialog.getWindow();
            if (window == null) return;
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setDimAmount(0);
        }

        if (mDialog.isShowing()) return;
        mDialog.show();


    }

    public void hideLoading() {
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
    }
}
