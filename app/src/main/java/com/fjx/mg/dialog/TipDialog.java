package com.fjx.mg.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.ViewUtil;
import com.library.repository.models.PriceListModel;

import java.util.List;

public class TipDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private TipDialogClickListener cl;
    private List<PriceListModel.PriceListBean> list;
    private TextView tvContent1;

    private TextView tvContent2;

    private TextView tvContent3;

    private TextView tvContent4;

    private TextView tvContent5;

    private EditText tvContent6;
    private String price = "";
    private int max = 500000;
    private int min = 1;

    public interface TipDialogClickListener {
        void onClick(View view, String price);
    }

    public TipDialog(List<PriceListModel.PriceListBean> list, Context context, int theme, TipDialogClickListener cl) {
        super(context, theme);
        this.cl = cl;
        this.list = list;
        this.context = context;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_tip);
        initUI();
        initValue();
        setData();
        price = list.get(0).getPrice();
        Select();
        tvContent6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                price = "";
                for (int i = 0; i < list.size(); i++) {
                    PriceListModel.PriceListBean model = list.get(i);
                    if (i == 5) {
                        model.setClick(true);
                    } else {
                        model.setClick(false);
                    }

                }
                Select();


                SoftInputUtil.showSoftInputView(context, tvContent6);
                tvContent6.setCursorVisible(true);//显示光标
                ViewUtil.setDrawableLeft(tvContent6, R.drawable.envelope_red);
                tvContent6.setBackground(context.getResources().getDrawable(R.drawable.solid_stroke_redx));
                tvContent6.setTextColor(context.getResources().getColor(R.color.textColorAccent));
                return false;
            }
        });
        tvContent6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start >= 0) {//从一输入就开始判断，
                    if (s.length() > 0) {
                    } else {
                    }
                    if (min != -1 && max != -1) {
                        try {
                            int num = Integer.parseInt(s.toString());
                            if (num > max) {
                                s = String.valueOf(max);
                                tvContent6.setText(s);

                            } else if (num < min) {
                                s = String.valueOf(min);
                                tvContent6.setText(s);
                            }
                        } catch (NumberFormatException e) {
                            Log.e("NumberFormatException", "==" + e.toString());
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setData() {

        tvContent1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list.size(); i++) {
                    PriceListModel.PriceListBean model = list.get(i);
                    if (i == 0) {
                        price = model.getPrice();
                        model.setClick(true);
                    } else {
                        model.setClick(false);
                    }

                }
                Select();
            }
        });
        tvContent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list.size(); i++) {
                    PriceListModel.PriceListBean model = list.get(i);
                    if (i == 1) {
                        price = model.getPrice();
                        model.setClick(true);
                    } else {
                        model.setClick(false);
                    }

                }
                Select();
            }
        });
        tvContent3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list.size(); i++) {
                    PriceListModel.PriceListBean model = list.get(i);
                    if (i == 2) {
                        price = model.getPrice();
                        model.setClick(true);
                    } else {
                        model.setClick(false);
                    }

                }
                Select();
            }
        });
        tvContent4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list.size(); i++) {
                    PriceListModel.PriceListBean model = list.get(i);
                    if (i == 3) {
                        price = model.getPrice();
                        model.setClick(true);
                    } else {
                        model.setClick(false);
                    }

                }
                Select();
            }
        });
        tvContent5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list.size(); i++) {
                    PriceListModel.PriceListBean model = list.get(i);
                    if (i == 4) {
                        price = model.getPrice();
                        model.setClick(true);
                    } else {
                        model.setClick(false);
                    }

                }
                Select();
            }
        });
    }

    private void Select() {
        tvContent6.setCursorVisible(false);//显示光标
        tvContent1.setText(list.get(0).getPrice().concat(list.get(0).getUnit()));
        ViewUtil.setDrawableLeft(tvContent1, list.get(0).getClick() ? R.drawable.envelope_red : R.drawable.envelope_gray);
        tvContent1.setBackground(context.getResources().getDrawable(list.get(0).getClick() ? R.drawable.solid_stroke_redx : R.drawable.solid_stroke_gray));
        tvContent1.setTextColor(context.getResources().getColor(list.get(0).getClick() ? R.color.textColorAccent : R.color.black_33));

        tvContent2.setText(list.get(1).getPrice().concat(list.get(1).getUnit()));
        ViewUtil.setDrawableLeft(tvContent2, list.get(1).getClick() ? R.drawable.envelope_red : R.drawable.envelope_gray);
        tvContent2.setBackground(context.getResources().getDrawable(list.get(1).getClick() ? R.drawable.solid_stroke_redx : R.drawable.solid_stroke_gray));
        tvContent2.setTextColor(context.getResources().getColor(list.get(1).getClick() ? R.color.textColorAccent : R.color.black_33));

        tvContent3.setText(list.get(2).getPrice().concat(list.get(2).getUnit()));
        ViewUtil.setDrawableLeft(tvContent3, list.get(2).getClick() ? R.drawable.envelope_red : R.drawable.envelope_gray);
        tvContent3.setBackground(context.getResources().getDrawable(list.get(2).getClick() ? R.drawable.solid_stroke_redx : R.drawable.solid_stroke_gray));
        tvContent3.setTextColor(context.getResources().getColor(list.get(2).getClick() ? R.color.textColorAccent : R.color.black_33));

        tvContent4.setText(list.get(3).getPrice().concat(list.get(3).getUnit()));
        ViewUtil.setDrawableLeft(tvContent4, list.get(3).getClick() ? R.drawable.envelope_red : R.drawable.envelope_gray);
        tvContent4.setBackground(context.getResources().getDrawable(list.get(3).getClick() ? R.drawable.solid_stroke_redx : R.drawable.solid_stroke_gray));
        tvContent4.setTextColor(context.getResources().getColor(list.get(3).getClick() ? R.color.textColorAccent : R.color.black_33));

        tvContent5.setText(list.get(4).getPrice().concat(list.get(4).getUnit()));
        ViewUtil.setDrawableLeft(tvContent5, list.get(4).getClick() ? R.drawable.envelope_red : R.drawable.envelope_gray);
        tvContent5.setBackground(context.getResources().getDrawable(list.get(4).getClick() ? R.drawable.solid_stroke_redx : R.drawable.solid_stroke_gray));
        tvContent5.setTextColor(context.getResources().getColor(list.get(4).getClick() ? R.color.textColorAccent : R.color.black_33));

        ViewUtil.setDrawableLeft(tvContent6, R.drawable.envelope_gray);
        tvContent6.setBackground(context.getResources().getDrawable(R.drawable.solid_stroke_gray));
        tvContent6.setTextColor(context.getResources().getColor(R.color.black_33));
        tvContent6.setText("");
    }

    private void initUI() {
        tvContent1 = findViewById(R.id.tvContent1);
        tvContent2 = findViewById(R.id.tvContent2);
        tvContent3 = findViewById(R.id.tvContent3);
        tvContent4 = findViewById(R.id.tvContent4);
        tvContent5 = findViewById(R.id.tvContent5);
        tvContent6 = findViewById(R.id.tvContent6);
        findViewById(R.id.tvSend).setOnClickListener(this);
        findViewById(R.id.tvSendCancle).setOnClickListener(this);
    }


    private void initValue() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        lp.width = dm.widthPixels;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {

        if (price.equals("")) {
            if (tvContent6.getText().toString().equals("")) {
                CommonToast.toast(R.string.input_custom_money);
            } else {
                cl.onClick(v, price.equals("") ? tvContent6.getText().toString() : price);
            }
        } else {
            cl.onClick(v, price);
        }
    }

}
