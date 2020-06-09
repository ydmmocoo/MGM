package com.fjx.mg.moments.city.type;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.fjx.mg.R;
import com.library.common.utils.ContextManager;

/**
 * @author yiw
 */
public class TypeMomentsCommentDialog extends Dialog implements
        View.OnClickListener {

    private CityMomentsTypePresenter mPresenter;
    private Boolean showDele;
    private String replyId;
    private String content;

    public TypeMomentsCommentDialog(Context context, CityMomentsTypePresenter presenter, String replyId, String content, boolean showDele) {
        super(context, R.style.comment_dialog);
        this.mPresenter = presenter;
        this.replyId = replyId;
        this.content = content;
        this.showDele = showDele;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);
        initWindowParams();
        initView();
    }

    private void initWindowParams() {
        Window dialogWindow = getWindow();
        // 获取屏幕宽、高用
        WindowManager wm = (WindowManager) ContextManager.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (display.getWidth() * 0.65); // 宽度设置为屏幕的0.65

        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
    }

    private void initView() {
        TextView copyTv = findViewById(R.id.copyTv);
        copyTv.setOnClickListener(this);
        TextView deleteTv = findViewById(R.id.deleteTv);
        deleteTv.setVisibility(showDele ? View.VISIBLE : View.GONE);
        deleteTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copyTv:
                ClipboardManager clipboard = (ClipboardManager) ContextManager.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(content);

                dismiss();
                break;
            case R.id.deleteTv:
                mPresenter.delReply(replyId);
                dismiss();
                break;
            default:
                break;
        }
    }

}
