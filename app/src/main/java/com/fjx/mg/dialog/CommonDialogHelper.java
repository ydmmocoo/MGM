package com.fjx.mg.dialog;

import android.app.Activity;

import com.fjx.mg.R;
import com.fjx.mg.house.comment.HouseCommentActivity;
import com.library.common.utils.SoftInputUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

public class CommonDialogHelper {

    public static void showAreaCodeDialog(Activity activity, OnSelectListener listener) {
        new XPopup.Builder(activity)
                .asBottomList(activity.getString(R.string.please_select_area_code), new String[]{"+86", "+261"}, listener)
                .show();
    }

    public static void showNewPublishDialog(Activity activity, android.view.View view, OnSelectListener listener) {
        NewPublishDialog publishDialog = new NewPublishDialog(activity);
        publishDialog.setOnSelectListener(listener);
        new XPopup.Builder(activity)
                .atView(view)
                .asCustom(publishDialog)
                .show();
    }

    public static void showPublishDialog(Activity activity, android.view.View view, OnSelectListener listener,
                                         boolean isExpire, boolean hasClosed, Boolean isLove, Boolean isShare) {
        PublishDialog publishDialog = new PublishDialog(activity);
        publishDialog.setOnSelectListener(listener);
        publishDialog.isExpire(isExpire, hasClosed, isLove, isShare);
        new XPopup.Builder(activity)
                .atView(view)
                .asCustom(publishDialog)
                .show();
    }

    public static void showPublishDialog(Activity activity, android.view.View view, OnSelectListener listener) {
        PublishDialog publishDialog = new PublishDialog(activity);
        publishDialog.setOnSelectListener(listener);
        publishDialog.setCompany();
        publishDialog.isExpire(true);
        new XPopup.Builder(activity)
                .atView(view)
                .asCustom(publishDialog)
                .show();
    }

}
