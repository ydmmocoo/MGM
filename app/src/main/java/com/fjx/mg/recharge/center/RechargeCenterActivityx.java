package com.fjx.mg.recharge.center;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.pay.PayActivity;
import com.fjx.mg.recharge.ewnbill.BillActivity;
import com.fjx.mg.recharge.record.BillRecordActivity;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.utils.RankPermissionHelper;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.fjx.mg.view.PhoneTextWatcher;
import com.fjx.mg.web.CommonWebActivity;
import com.google.gson.Gson;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.IntentUtil;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.models.RechargeModel;
import com.library.repository.models.RechargePhoneModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description：充值中心（电话费充值）
 */
public class RechargeCenterActivityx extends BaseMvpActivity<RechargeCenterPresenterx> implements RechargeCenterContractx.View {
    @BindView(R.id.nines)
    LinearLayout nines;
    @BindView(R.id.llParent1)
    View llParent1;
    @BindView(R.id.tvContent1)
    TextView tvContent1;
    @BindView(R.id.tvPrice1)
    TextView tvPrice1;
    //九宫格ID
    @BindView(R.id.llParent2)
    View llParent2;
    @BindView(R.id.tvContent2)
    TextView tvContent2;
    @BindView(R.id.tvPrice2)
    TextView tvPrice2;
    //九宫格ID
    @BindView(R.id.llParent3)
    View llParent3;
    @BindView(R.id.tvContent3)
    TextView tvContent3;
    @BindView(R.id.tvPrice3)
    TextView tvPrice3;
    //九宫格ID
    @BindView(R.id.llParent4)
    View llParent4;
    @BindView(R.id.tvContent4)
    TextView tvContent4;
    @BindView(R.id.tvPrice4)
    TextView tvPrice4;
    //九宫格ID
    @BindView(R.id.llParent5)
    View llParent5;
    @BindView(R.id.tvContent5)
    TextView tvContent5;
    @BindView(R.id.tvPrice5)
    TextView tvPrice5;
    //九宫格ID
    @BindView(R.id.llParent6)
    View llParent6;
    @BindView(R.id.tvContent6)
    TextView tvContent6;
    @BindView(R.id.tvPrice6)
    TextView tvPrice6;
    //九宫格ID
    @BindView(R.id.llParent7)
    View llParent7;
    @BindView(R.id.tvContent7)
    TextView tvContent7;
    @BindView(R.id.tvPrice7)
    TextView tvPrice7;
    //九宫格ID
    @BindView(R.id.llParent8)
    View llParent8;
    @BindView(R.id.tvContent8)
    TextView tvContent8;
    @BindView(R.id.tvPrice8)
    TextView tvPrice8;
    //九宫格ID
    @BindView(R.id.llParent9)
    View llParent9;
    @BindView(R.id.tvContent9)
    EditText tvContent9;
    @BindView(R.id.tvPrice9)
    TextView tvPrice9;

    @BindView(R.id.tvBuyNow)
    TextView tvBuyNow;
    @BindView(R.id.tvAreaCode)
    TextView tvAreaCode;
    @BindView(R.id.flPhoneList)
    FrameLayout flPhoneList;
    @BindView(R.id.phoneRecycler)
    RecyclerView phoneRecycler;

    @BindView(R.id.etPhoneNum)
    EditText etPhoneNum;


    private String areaCode = "261";

    private RechargeModel model;
    private PhoneListAdapter phoneListAdapter;
    private List<RechargePhoneModel> phoneList;
    private boolean mIsFirst=true;


    public static Intent newInstance(Context context) {
        return new Intent(context, RechargeCenterActivityx.class);
    }

    public static Intent newInstance(Context context, String phone) {
        Intent intent = new Intent(context, RechargeCenterActivity.class);
        intent.putExtra("phone", phone);
        return intent;
    }

    @Override
    protected RechargeCenterPresenterx createPresenter() {
        return new RechargeCenterPresenterx(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_recharge_center_activityx;
    }

//    @Override
////    public void setMobile(String text) {
////        etPhoneNum.setText(text);
////    }

    private int max = 500000;
    private int min = 1;


    private void ChangeChecks() {

        Log.e("", "");
        for (int i = 0; i < datass.size(); i++) {
            datass.get(i).setCheck(false);
        }
        model = null;
        SetDatax(tvPrice1, tvContent1, llParent1);
        SetDatax(tvPrice2, tvContent2, llParent2);
        SetDatax(tvPrice3, tvContent3, llParent3);
        SetDatax(tvPrice4, tvContent4, llParent4);
        SetDatax(tvPrice5, tvContent5, llParent5);
        SetDatax(tvPrice6, tvContent6, llParent6);
        SetDatax(tvPrice7, tvContent7, llParent7);
        SetDatax(tvPrice8, tvContent8, llParent8);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {

        nines.setVisibility(View.GONE);
        tvContent9.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SoftInputUtil.showSoftInputView(RechargeCenterActivityx.this, tvContent9);
                tvContent9.setCursorVisible(true);//显示光标
                ChangeChecks();
                GradientDrawableHelper.whit(llParent9).setColor(R.color.trans).setStroke(1, R.color.colorAccent);
                tvContent9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorAccent));
                tvPrice9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorAccent));
                return false;
            }
        });
        tvContent9.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (start >= 0) {//从一输入就开始判断，
                    if (s.length() > 0) {
                        tvPrice9.setText(getString(R.string.selling_price).concat("：") + s + "AR");
                    } else {
                        tvPrice9.setText("");
                    }
                    if (min != -1 && max != -1) {
                        try {
                            int num = Integer.parseInt(s.toString());
                            if (num > max) {
                                s = String.valueOf(max);
                                tvContent9.setText(s);
                                tvPrice9.setText(getString(R.string.selling_price).concat("：") + s + "AR");

                            } else if (num < min) {
                                s = String.valueOf(min);
                                tvContent9.setText(s);
                                tvPrice9.setText(getString(R.string.selling_price).concat("：") + s + "AR");
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
        InputFilter[] filters = {new InputFilter.LengthFilter(6)};
        tvContent9.setFilters(filters);

        ToolBarManager.with(this).setTitle(getString(R.string.recharge_center)).setRightImage(R.drawable.recharge_record,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (UserCenter.needLogin()) return;
                        if (UserCenter.hasLogin()) {
                            startActivity(BillRecordActivity.newInstance(getCurContext()));
                        } else {
                            new DialogUtil().showAlertDialog(getCurActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    UserCenter.goLoginActivity();
                                }
                            });
                        }

                    }
                });
        GradientDrawableHelper.whit(tvBuyNow).setCornerRadius(100).setColor(R.color.colorAccent);

        final List<RechargePhoneModel> searchPhoneList = new ArrayList<>();
        phoneList = RepositoryFactory.getLocalRepository().getLastRechargePhone();
        mPresenter.initData(this);
        if (phoneList != null || phoneList.size() > 0) {
            if (UserCenter.hasLogin()) {
                String phone = UserCenter.getUserInfo().getPhone();
                if (phone.length() != 11) {
                    etPhoneNum.setText(UserCenter.getUserInfo().getPhone());
                    etPhoneNum.setSelection(UserCenter.getUserInfo().getPhone().length());
//                    areaCode = UserCenter.getUserInfo().getSn();
                }
            }
        }
        if (getIntent() != null) {
            etPhoneNum.setText(getIntent().getStringExtra("phone"));
        }
        PhoneTextWatcher phoneTextWatcher = new PhoneTextWatcher();
        phoneTextWatcher.setAreaCode(areaCode);
        etPhoneNum.addTextChangedListener(phoneTextWatcher);
//        etPhoneNum.setText(phoneList.get(0).getPhone());
//        etPhoneNum.setSelection(phoneList.get(0).getPhone().length());
//        areaCode = phoneList.get(0).getAreaCode();
        tvAreaCode.setText("+".concat(areaCode));
        phoneTextWatcher.setAreaCode(areaCode);
        if (phoneList.isEmpty()) return;
        phoneRecycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        phoneRecycler.addItemDecoration(new SpacesItemDecoration(1));
        phoneListAdapter = new PhoneListAdapter();
        phoneRecycler.setAdapter(phoneListAdapter);
        phoneListAdapter.setList(phoneList);
        phoneListAdapter.addFooterView(getView());
        etPhoneNum.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (phoneListAdapter.getData().isEmpty()) return false;
                    //etPhoneNum.setText("");
                    flPhoneList.setVisibility(View.VISIBLE);
                    SoftInputUtil.showSoftInputView(getCurContext(), etPhoneNum);
                    return true;
                }
                return false;
            }
        });
//        etPhoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    if (phoneListAdapter.getData().isEmpty()) return;
//                    etPhoneNum.setText("");
//                    flPhoneList.setVisibility(View.VISIBLE);
//                }
//            }
//        });
        phoneListAdapter.setOnItemClickListener((adapter, view, position) -> {
            flPhoneList.setVisibility(View.GONE);
            RechargePhoneModel item = phoneListAdapter.getItem(position);
            if (TextUtils.isEmpty(item.getPhone())){
                etPhoneNum.setText("");
            }else {
                etPhoneNum.setText(item.getPhone());
                etPhoneNum.setSelection(item.getPhone().length()+1);
            }
            areaCode = item.getAreaCode();
            tvAreaCode.setText("+".concat(areaCode));
            flPhoneList.setVisibility(View.GONE);
            SoftInputUtil.hideSoftInput(getCurActivity());
        });

        phoneTextWatcher.setCompleteListener(new PhoneTextWatcher.InputCompleteListener() {
            @Override
            public void onInputComplete(String phone) {
            }

            @Override
            public void afterTextChanged(CharSequence s, int start, int before, int count) {
                if (phoneList.isEmpty()) return;
                searchPhoneList.clear();
                if (0 == s.length()) {
                    phoneListAdapter.setList(phoneList);
                    flPhoneList.setVisibility(View.VISIBLE);
                } else {
                    if (TextUtils.isEmpty(s.toString().trim())){
                        return;
                    }
                    String text;
                    if (s.toString().length()>10){
                        text=s.toString().substring(0,10).replace(" ","");
                    }else {
                        text=s.toString().replace(" ","");
                    }
                    for (int i = 0; i < phoneList.size(); i++) {
                        if (phoneList.get(i).getPhone().substring(0,text.length()).equals(text)) {
                            searchPhoneList.add(phoneList.get(i));
                        }
                    }
                    phoneListAdapter.setList(searchPhoneList);

                    if (searchPhoneList.size() <= 0) {
                        flPhoneList.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < searchPhoneList.size(); i++) {
                        if (TextUtils.equals(text, searchPhoneList.get(i).getPhone().substring(0,text.length()))) {
                            flPhoneList.setVisibility(View.VISIBLE);
                        } else {
                            flPhoneList.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

    }


    private View getView() {
        View view = LayoutInflater.from(getCurContext()).inflate(R.layout.layout_center_text, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepositoryFactory.getLocalRepository().clearPhoneList();
                flPhoneList.setVisibility(View.GONE);
                phoneListAdapter.setList(null);

            }
        });
        return view;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsFirst) {
            phoneList = RepositoryFactory.getLocalRepository().getLastRechargePhone();
            if (phoneListAdapter!=null) {
                phoneListAdapter.setList(phoneList);
            }
        }else {
            mIsFirst=false;
        }
    }

    @Override
    public void showSelectPhoneNUm(String number) {
        etPhoneNum.setText(number);
        etPhoneNum.setSelection(etPhoneNum.getText().length());
    }

    @OnClick(R.id.flPhoneList)
    public void clicflPhoneList() {
        flPhoneList.setVisibility(View.GONE);
    }

    private List<RechargeModel> datass = new ArrayList<>();

    @Override
    public void show9PhonePackage(List<RechargeModel> datas) {
        datass = datas;
        model = datass.get(0);
        SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
        Gson gson = new Gson();
        sp.putString("json", gson.toJson(datas));
        sp.close();

        SetDatas(datass.get(0), tvPrice1, tvContent1, llParent1);
        SetDatas(datass.get(1), tvPrice2, tvContent2, llParent2);
        SetDatas(datass.get(2), tvPrice3, tvContent3, llParent3);
        SetDatas(datass.get(3), tvPrice4, tvContent4, llParent4);
        SetDatas(datass.get(4), tvPrice5, tvContent5, llParent5);
        SetDatas(datass.get(5), tvPrice6, tvContent6, llParent6);
        SetDatas(datass.get(6), tvPrice7, tvContent7, llParent7);
        SetDatas(datass.get(7), tvPrice8, tvContent8, llParent8);
        nines.setVisibility(View.VISIBLE);

    }

    @SuppressLint("SetTextI18n")
    private void SetDatas(RechargeModel rec1, TextView tv_Price, TextView tv_Content1, View ll_Parent1) {
        tv_Price.setVisibility(View.VISIBLE);
        tv_Content1.setText(rec1.getPackageX() + rec1.getUnit());
        tv_Price.setText(getString(R.string.selling_price).concat("：") + rec1.getPrice() + rec1.getUnit());
        GradientDrawableHelper.whit(llParent1).setColor(R.color.trans).setStroke(1, R.color.colorAccent);
        tvContent1.setTextColor(ContextCompat.getColor(llParent1.getContext(), R.color.textColorAccent));
        tvPrice1.setTextColor(ContextCompat.getColor(llParent1.getContext(), R.color.textColorAccent));

        GradientDrawableHelper.whit(ll_Parent1).setColor(R.color.trans).setStroke(1, R.color.textColorHint);
        tv_Content1.setTextColor(ContextCompat.getColor(ll_Parent1.getContext(), R.color.textColorHint));
        tv_Price.setTextColor(ContextCompat.getColor(ll_Parent1.getContext(), R.color.textColorHint));

        GradientDrawableHelper.whit(llParent9).setColor(R.color.trans).setStroke(1, R.color.textColorHint);
        tvContent9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorHint));
        tvPrice9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorHint));

    }

    @SuppressLint("SetTextI18n")
    private void SetData(RechargeModel rec1, TextView tv_Price, TextView tv_Content1, View ll_Parent1) {
        tv_Price.setVisibility(View.VISIBLE);
        tv_Content1.setText(rec1.getPackageX() + rec1.getUnit());
        tv_Price.setText(getString(R.string.selling_price).concat("：") + rec1.getPrice() + rec1.getUnit());
        if (rec1.isCheck()) {
            GradientDrawableHelper.whit(ll_Parent1).setColor(R.color.trans).setStroke(1, R.color.colorAccent);
            tv_Content1.setTextColor(ContextCompat.getColor(ll_Parent1.getContext(), R.color.textColorAccent));
            tv_Price.setTextColor(ContextCompat.getColor(ll_Parent1.getContext(), R.color.textColorAccent));
        } else {
            GradientDrawableHelper.whit(ll_Parent1).setColor(R.color.trans).setStroke(1, R.color.textColorHint);
            tv_Content1.setTextColor(ContextCompat.getColor(ll_Parent1.getContext(), R.color.textColorHint));
            tv_Price.setTextColor(ContextCompat.getColor(ll_Parent1.getContext(), R.color.textColorHint));
        }
        GradientDrawableHelper.whit(llParent9).setColor(R.color.trans).setStroke(1, R.color.textColorHint);
        tvContent9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorHint));
        tvPrice9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorHint));
        tvContent9.setText("");
        tvPrice9.setText("");

    }

    private void SetDatax(TextView tv_Price, TextView tv_Content1, View ll_Parent1) {
        GradientDrawableHelper.whit(ll_Parent1).setColor(R.color.trans).setStroke(1, R.color.textColorHint);
        tv_Content1.setTextColor(ContextCompat.getColor(ll_Parent1.getContext(), R.color.textColorHint));
        tv_Price.setTextColor(ContextCompat.getColor(ll_Parent1.getContext(), R.color.textColorHint));
    }

    private void ChangeCheck(int i2) {
        for (int i = 0; i < datass.size(); i++) {
            datass.get(i).setCheck(i == i2);
        }

        SoftInputUtil.hideSoftInput(this);
        model = datass.get(i2);
        SetData(datass.get(0), tvPrice1, tvContent1, llParent1);
        SetData(datass.get(1), tvPrice2, tvContent2, llParent2);
        SetData(datass.get(2), tvPrice3, tvContent3, llParent3);
        SetData(datass.get(3), tvPrice4, tvContent4, llParent4);
        SetData(datass.get(4), tvPrice5, tvContent5, llParent5);
        SetData(datass.get(5), tvPrice6, tvContent6, llParent6);
        SetData(datass.get(6), tvPrice7, tvContent7, llParent7);
        SetData(datass.get(7), tvPrice8, tvContent8, llParent8);
        tvContent9.setCursorVisible(false);//显示光标
    }

    @OnClick(R.id.llParent1)
    public void llParent1() {
        ChangeCheck(0);
    }

    @OnClick(R.id.llParent2)
    public void llParent2() {
        ChangeCheck(1);
    }

    @OnClick(R.id.llParent3)
    public void llParent3() {
        ChangeCheck(2);
    }

    @OnClick(R.id.llParent4)
    public void llParent4() {
        ChangeCheck(3);
    }

    @OnClick(R.id.llParent5)
    public void llParent5() {
        ChangeCheck(4);
    }

    @OnClick(R.id.llParent6)
    public void llParent6() {
        ChangeCheck(5);
    }

    @OnClick(R.id.llParent7)
    public void llParent7() {
        ChangeCheck(6);
    }

    @OnClick(R.id.llParent8)
    public void llParent8() {
        ChangeCheck(7);
    }

    @OnClick(R.id.llParent9)
    public void llParent9() {
        SoftInputUtil.showSoftInputView(this, tvContent9);
        tvContent9.setCursorVisible(true);//显示光标
        ChangeChecks();
        GradientDrawableHelper.whit(llParent9).setColor(R.color.trans).setStroke(1, R.color.colorAccent);
        tvContent9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorAccent));
        tvPrice9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorAccent));
    }

    @OnClick(R.id.tvPhomeMail)
    public void clickPhomeMail() {
        IntentUtil.pickContact(this);

    }

    @OnClick(R.id.tvBuyNow)
    public void clickPay() {
//        if (UserCenter.needLogin()) return;
        if (UserCenter.hasLogin()) {
            if (RankPermissionHelper.NoPrivileges()) return;
            String phone = etPhoneNum.getText().toString().replace(" ", "");

            if (TextUtils.isEmpty(phone)) {
                CommonToast.toast(getString(R.string.login_input_phone));
                return;
            }

            if (!StringUtil.phoneLegal(phone)) {
                CommonToast.toast(getString(R.string.error_format_phone));
                return;
            }
            if (model == null) {
                if (tvContent9.getText().toString().equals("")) {
                    CommonToast.toast(R.string.input_custom_money);
                    return;
                } else if (Integer.parseInt(tvContent9.getText().toString()) < 500) {
                    CommonToast.toast(R.string.less_300_money);
                    return;
                }
            }

            Map<String, Object> map = new HashMap<>();
            map.put("type", 1);
            map.put("sn", areaCode);
            map.put("phone", phone);
            map.put("num", model == null ? tvContent9.getText().toString() : model.getPackageX());
            map.put("price", model == null ? tvContent9.getText().toString() : model.getPrice().replace("AR", ""));
            map.put("unit", model == null ? "AR" : model.getUnit());
            UsagePayMode payMode = UsagePayMode.phone_recharge;
            PayExtModel extModel = new PayExtModel(payMode, map);
            RepositoryFactory.getLocalRepository().saveLastRechargePhone(new RechargePhoneModel(areaCode, phone));
            startActivity(PayActivity.newInstance(getCurContext(), JsonUtil.moderToString(extModel)));
        } else {
            new DialogUtil().showAlertDialog(getCurActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    UserCenter.goLoginActivity();
                }
            });
        }

    }


    @OnClick(R.id.tvElectBill)
    public void clicktvElectBill() {
        startActivity(BillActivity.newInstance(getCurContext(), BillActivity.BILL_TYPE_ELEC));
    }


    @OnClick(R.id.tvNetBill)
    public void clicktvNetBill() {
        startActivity(BillActivity.newInstance(getCurContext(), BillActivity.BILL_TYPE_NET));
    }

    @OnClick(R.id.tvWaterBill)
    public void clicktvWaterBill() {
        startActivity(BillActivity.newInstance(getCurContext(), BillActivity.BILL_TYPE_WATER));
    }


    @OnClick(R.id.tvInstructions)//充值说明
    public void ClickInstructions() {
        showUpdateDialog();
    }

    @OnClick(R.id.tvCharges)//资费套餐
    public void ClickCharges() {
        try {
            CommonWebActivity.Options options = new CommonWebActivity.Options();
            StringBuilder sb = new StringBuilder();
            sb.append(Constant.HOST);
            sb.append("invite/flowAdv?ispType=");
            UserInfoModel userInfo = null;
            if (UserCenter.hasLogin()) {
                userInfo = UserCenter.getUserInfo();
            }
            String phone = "";
            if (userInfo != null) {
                phone = userInfo.getPhone();
                if (StringUtil.isNotEmpty(phone)) {
                    sb.append(userInfo.getPhone().substring(0, 3));
                }
            }
            sb.append("&l=");
            sb.append(RepositoryFactory.getLocalRepository().getLangugeType());
            Log.e("资费套餐链接:", sb.toString());
            options.setLoadUrl(sb.toString());
            startActivity(CommonWebActivity.newInstance(getCurContext(), JsonUtil.moderToString(options)));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.tvSearch)//查询余额
    public void tvSearch() {//https://www.messageglobal-online.com/invite/search
        UserInfoModel userInfo = null;
        if (UserCenter.hasLogin()) {
            userInfo = UserCenter.getUserInfo();
        }
        try {
            String phone = "";
            if (userInfo != null) {
                phone = userInfo.getPhone();
            }
            CommonWebActivity.Options options = new CommonWebActivity.Options();
            StringBuilder sb = new StringBuilder();
            sb.append(Constant.HOST);
            sb.append(Constant.getSearch());
            if (StringUtil.isNotEmpty(phone)) {
                sb.append(userInfo.getPhone().substring(0, 3));
            }
            sb.append("&l=");
            sb.append(RepositoryFactory.getLocalRepository().getLangugeType());
            Log.e("资费套餐链接:", sb.toString());
            options.setLoadUrl(sb.toString());
            startActivity(CommonWebActivity.newInstance(getCurContext(), JsonUtil.moderToString(options)));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    public void showUpdateDialog() {
        MaterialDialog updateDliaog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_instructions, true)
                .build();

        updateDliaog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = updateDliaog.getCustomView();
        if (view == null) return;


        updateDliaog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (resultCode == RESULT_OK && requestCode == 100) {
            mPresenter.getPhoneNum(data);
        }
    }

}
