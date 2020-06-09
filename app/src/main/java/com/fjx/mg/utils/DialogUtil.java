package com.fjx.mg.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.appcompat.app.AlertDialog;

import com.fjx.mg.R;
import com.library.repository.data.UserCenter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author    by hanlz
 * Date      on 2020/3/24.
 * Description：
 */
public class DialogUtil {
    public static final int CENTER = 0x01;
    public static final int LEFT = 0x02;
    public static final int TOP = 0x03;
    public static final int RIGHT = 0x04;
    public static final int BOTTOM = 0x05;

    @IntDef({CENTER, LEFT, TOP, RIGHT, BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GravityType {

    }

    @GravityType
    int def = CENTER;

    public static class Builder {
        private Context mContext;
        private Dialog mDialog;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder create() {
            this.create(0);
            return this;
        }

        public Builder create(int style) {
            mDialog = new Dialog(mContext, style);
            if (style != 0) {
                Window dialogWindow = mDialog.getWindow();
                dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                //设置宽
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                //设置高
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialogWindow.setAttributes(lp);
            }
            return this;
        }

        public Builder setContentView(int layoutId) {
            View view = LayoutInflater.from(mContext).inflate(layoutId, null);
            mDialog.setContentView(view);
            return this;
        }

        public Builder setContentView() {
            this.setContentView(R.layout.dialog_qr_layout);
            return this;
        }

        public Builder setContentView(String tips) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_qr_layout, null);
            TextView tvTips = view.findViewById(R.id.tvTips);
            tvTips.setVisibility(View.VISIBLE);
            tvTips.setText(tips);
            mDialog.setContentView(view);
            return this;
        }

        public Builder setContentView(View view) {
            mDialog.setContentView(view);
            return this;
        }

        public Builder setBackgroundDrawableResource(int colorId) {
            mDialog.getWindow().setBackgroundDrawableResource(colorId);
            return this;
        }

        public Builder setGravity(@GravityType int gravity) {
            Window window = mDialog.getWindow();
            switch (gravity) {
                case CENTER:
                    window.setGravity(Gravity.CENTER);
                    break;
                case TOP:
                    window.setGravity(Gravity.TOP);
                    break;
                case LEFT:
                    window.setGravity(Gravity.LEFT);
                    break;
                case RIGHT:
                    window.setGravity(Gravity.RIGHT);
                    break;
                case BOTTOM:
                    window.setGravity(Gravity.BOTTOM);
                    break;
            }
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean outside) {
            mDialog.setCanceledOnTouchOutside(outside);
            return this;
        }

        public void show() {
            if (!mDialog.isShowing() && mDialog != null) {
                mDialog.show();
            }
        }

        public void dimiss() {
            if (mDialog.isShowing() && mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
        }
    }

    public void showAlertDialog(Context context, int title, int message, DialogInterface.OnClickListener listener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.ok), listener)
                .setNegativeButton(context.getString(R.string.cancel), null)
                .create();
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!dialog.isShowing() && !activity.isFinishing()) {
                dialog.show();
            }
        } else {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    public void showLoginAlertDialog(Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.tips))
                .setMessage(context.getString(R.string.not_login_forward_login))
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UserCenter.goLoginActivity();
                    }
                })
                .setNegativeButton(context.getString(R.string.cancel), null)
                .create();
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!dialog.isShowing() && !activity.isFinishing()) {
                dialog.show();
            }
        } else {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }
}
