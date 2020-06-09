package com.fjx.mg.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.fjx.mg.R;

/**
 * Author    by hanlz
 * Date      on 2019/12/13.
 * Description：
 */
public class CopyTipsDialog extends Dialog {

    private CardView mCvCopy;
    private OnCopyCallback mCallback;

    public CopyTipsDialog(@NonNull Context context) {
        super(context);
    }

    public CopyTipsDialog(@NonNull Context context,OnCopyCallback callback) {
        super(context);
        this.mCallback = callback;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_copy_tips);
        mCvCopy = findViewById(R.id.cvCopy);
        mCvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (mCallback!=null){
                    mCallback.onclick();
                }
            }
        });
    }

    public interface OnCopyCallback{
        void onclick();
    }
}
