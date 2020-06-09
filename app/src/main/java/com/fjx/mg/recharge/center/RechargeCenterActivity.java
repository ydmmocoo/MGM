package com.fjx.mg.recharge.center;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.fjx.mg.utils.RankPermissionHelper;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.fjx.mg.view.PhoneTextWatcher;
import com.fjx.mg.web.CommonWebActivity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
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

public class RechargeCenterActivity extends BaseMvpActivity<RechargeCenterPresenter> implements RechargeCenterContract.View {
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
    TextView tvContent9;
    @BindView(R.id.tvPrice9)
    TextView tvPrice9;

    @BindView(R.id.tvBuyNow)
    TextView tvBuyNow;
    @BindView(R.id.tvAreaCode)
    TextView tvAreaCode;

    @BindView(R.id.s_tab)
    CommonTabLayout tabLayout;

    @BindView(R.id.etPhoneNum)
    EditText etPhoneNum;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.phoneRecycler)
    RecyclerView phoneRecycler;

    @BindView(R.id.flPhoneList)
    FrameLayout flPhoneList;

    private RechargeAdapter mAdapter;

    private int type = 1;//	1为话费，2为流量
    private PhoneListAdapter phoneListAdapter;

    private String areaCode = "261";

    private PhoneTextWatcher phoneTextWatcher;
    private MaterialDialog updateDliaog;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, RechargeCenterActivity.class);
        return intent;
    }

    @Override
    protected RechargeCenterPresenter createPresenter() {
        return new RechargeCenterPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_recharge_center;
    }

    @Override
    public void setMobile(String text) {
        etPhoneNum.setText(text);
    }

    private int max = 200000;
    private int min = 1;

    protected void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void ChangeChecks() {

        Log.e("", "");
        for (int i = 0; i < datass.size(); i++) {
            datass.get(i).setCheck(false);
        }
//        if (i2 != 8) {
//            tvContent9.setCursorVisible(false);//显示光标
//            hideKeyboard(tvContent9);
//        }
        SetDatax(datass.get(0), tvPrice1, tvContent1, llParent1);
        SetDatax(datass.get(1), tvPrice2, tvContent2, llParent2);
        SetDatax(datass.get(2), tvPrice3, tvContent3, llParent3);
        SetDatax(datass.get(3), tvPrice4, tvContent4, llParent4);
        SetDatax(datass.get(4), tvPrice5, tvContent5, llParent5);
        SetDatax(datass.get(5), tvPrice6, tvContent6, llParent6);
        SetDatax(datass.get(6), tvPrice7, tvContent7, llParent7);
        SetDatax(datass.get(7), tvPrice8, tvContent8, llParent8);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        nines.setVisibility(View.GONE);
//        tvContent9.setOnEditorActionListener(new TextView.OnEditorActionListener() {

//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                GradientDrawableHelper.whit(llParent9).setColor(R.color.trans).setStroke(1, R.color.colorAccent);
//                tvContent9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorAccent));
//                tvPrice9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorAccent));
//                return true;
//            }
//        });
//        tvContent9.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                ChangeChecks();
//                GradientDrawableHelper.whit(llParent9).setColor(R.color.trans).setStroke(1, R.color.colorAccent);
//                tvContent9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorAccent));
//                tvPrice9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorAccent));
//                return false;
//
//            }
//        });
        tvContent9.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ChangeChecks();
                GradientDrawableHelper.whit(llParent9).setColor(R.color.trans).setStroke(1, R.color.colorAccent);
                tvContent9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorAccent));
                tvPrice9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorAccent));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                ChangeChecks();
                GradientDrawableHelper.whit(llParent9).setColor(R.color.trans).setStroke(1, R.color.colorAccent);
                tvContent9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorAccent));
                tvPrice9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorAccent));
                if (start >= 0) {//从一输入就开始判断，
                    if (s.length() > 0) {
                        tvPrice9.setText(getString(R.string.selling_price).concat("：") + s + "AR");
                    } else {
                        tvPrice9.setText("");
                    }
                    if (min != -1 && max != -1) {
                        try {
                            int num = Integer.parseInt(s.toString());
                            //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                            if (num > max) {
                                s = String.valueOf(max);//如果大于max，则内容为max
                                tvContent9.setText(s);
                                tvPrice9.setText(getString(R.string.selling_price).concat("：") + s + "AR");

                            } else if (num < min) {
                                s = String.valueOf(min);//如果小于min,则内容为min
                                tvContent9.setText(s);
                                tvPrice9.setText(getString(R.string.selling_price).concat("：") + s + "AR");
                            }
                        } catch (NumberFormatException e) {
                            Log.e("ontextchanged", "==" + e.toString());
                        }
                        //edittext中的数字在max和min之间，则不做处理，正常显示即可。
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final List<RechargePhoneModel> phoneList = RepositoryFactory.getLocalRepository().getLastRechargePhone();
        phoneTextWatcher = new PhoneTextWatcher();
        phoneTextWatcher.setAreaCode(areaCode);

        etPhoneNum.addTextChangedListener(phoneTextWatcher);
        if (getIntent() != null) {
            etPhoneNum.setText(getIntent().getStringExtra("phone"));
        }

        ImageView imageView = ToolBarManager.with(this).setTitle(getString(R.string.recharge_center)).setRightImage(R.drawable.recharge_record,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (UserCenter.needLogin()) return;
                        startActivity(BillRecordActivity.newInstance(getCurContext()));
                    }
                });
        GradientDrawableHelper.whit(tvBuyNow).setCornerRadius(100).setColor(R.color.colorAccent);
        mAdapter = new RechargeAdapter();
        recycler.setLayoutManager(new GridLayoutManager(getCurContext(), 3));


        recycler.setAdapter(mAdapter);
        mAdapter.setPhone(true);
        recycler.addItemDecoration(new SpacesItemDecoration(10, 10));
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                mAdapter.reCheck(position);
            }
        });

        mPresenter.initData(this);
        if (phoneList.isEmpty()) {
            if (UserCenter.hasLogin()) {
//                areaCode = UserCenter.getUserInfo().getSn();
                String phone = UserCenter.getUserInfo().getPhone();
                if (phone.length() != 11) {
                    etPhoneNum.setText(UserCenter.getUserInfo().getPhone());
                    etPhoneNum.setSelection(UserCenter.getUserInfo().getPhone().length());
                }
//                tvAreaCode.setText("+".concat(areaCode));
            }
            return;
        }

//        etPhoneNum.setText(phoneList.get(0).getPhone());
//        etPhoneNum.setSelection(phoneList.get(0).getPhone().length());
        areaCode = phoneList.get(0).getAreaCode();
        tvAreaCode.setText("+".concat(areaCode));
        phoneTextWatcher.setAreaCode(areaCode);

        phoneRecycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        phoneRecycler.addItemDecoration(new SpacesItemDecoration(1));
        phoneListAdapter = new PhoneListAdapter();
        phoneRecycler.setAdapter(phoneListAdapter);
        phoneListAdapter.setList(phoneList);
        phoneListAdapter.addFooterView(getView());
        etPhoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (phoneListAdapter.getData().isEmpty()) return;
                    etPhoneNum.setText("");
                    flPhoneList.setVisibility(View.VISIBLE);
                }
            }
        });
        phoneListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                flPhoneList.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(phoneListAdapter.getItem(position).getPhone())) {
                    etPhoneNum.setText(phoneListAdapter.getItem(position).getPhone());
                    etPhoneNum.setSelection(phoneListAdapter.getItem(position).getPhone().length()+1);
                }
                areaCode = phoneListAdapter.getItem(position).getAreaCode();
                tvAreaCode.setText("+".concat(areaCode));
                SoftInputUtil.hideSoftInput(getCurActivity());
            }
        });
    }

    @Override
    public void showTabAndItems(ArrayList<CustomTabEntity> tabEntitys) {
        tabLayout.setTabData(tabEntitys);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                if (position == 0) {
                    type = 1;
                    mPresenter.getPhonePackage();
                    mAdapter.setPhone(true);
                } else {
                    type = 2;
                    mPresenter.getDataPackage();
                    mAdapter.setPhone(false);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    public void showSelectPhoneNUm(String number) {
        etPhoneNum.setText(number);
        etPhoneNum.setSelection(etPhoneNum.getText().length());
    }

    @Override
    public void showPhonePackage(List<RechargeModel> datas) {
        Log.e("RechargeModel:", "" + datas);
        mAdapter.setList(datas);
        SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
        Gson gson = new Gson();
        sp.putString("json", gson.toJson(datas));
        sp.close();
    }

    private List<RechargeModel> datass = new ArrayList<>();

    @Override
    public void show9PhonePackage(List<RechargeModel> datas) {
        datass = datas;
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

    private void SetDatas(RechargeModel rec1, TextView tv_Price, TextView tv_Content1, View ll_Parent1) {
        tv_Price.setVisibility(View.VISIBLE);
        tv_Content1.setText(rec1.getPackageX() + rec1.getUnit());
        tv_Price.setText("售价：" + rec1.getPrice() + rec1.getUnit());
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

    private void SetData(RechargeModel rec1, TextView tv_Price, TextView tv_Content1, View ll_Parent1) {
        tv_Price.setVisibility(View.VISIBLE);
        tv_Content1.setText(rec1.getPackageX() + rec1.getUnit());
        tv_Price.setText("售价：" + rec1.getPrice() + rec1.getUnit());
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

    private void SetDatax(RechargeModel rec1, TextView tv_Price, TextView tv_Content1, View ll_Parent1) {
        GradientDrawableHelper.whit(ll_Parent1).setColor(R.color.trans).setStroke(1, R.color.textColorHint);
        tv_Content1.setTextColor(ContextCompat.getColor(ll_Parent1.getContext(), R.color.textColorHint));
        tv_Price.setTextColor(ContextCompat.getColor(ll_Parent1.getContext(), R.color.textColorHint));
    }

    private void ChangeCheck(int i2) {
        for (int i = 0; i < datass.size(); i++) {
            datass.get(i).setCheck(i == i2);
        }
//        if (i2 != 8) {
//            tvContent9.setCursorVisible(false);//显示光标
//            hideKeyboard(tvContent9);
//        }
        SetData(datass.get(0), tvPrice1, tvContent1, llParent1);
        SetData(datass.get(1), tvPrice2, tvContent2, llParent2);
        SetData(datass.get(2), tvPrice3, tvContent3, llParent3);
        SetData(datass.get(3), tvPrice4, tvContent4, llParent4);
        SetData(datass.get(4), tvPrice5, tvContent5, llParent5);
        SetData(datass.get(5), tvPrice6, tvContent6, llParent6);
        SetData(datass.get(6), tvPrice7, tvContent7, llParent7);
        SetData(datass.get(7), tvPrice8, tvContent8, llParent8);
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
        ChangeChecks();
        GradientDrawableHelper.whit(llParent9).setColor(R.color.trans).setStroke(1, R.color.colorAccent);
        tvContent9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorAccent));
        tvPrice9.setTextColor(ContextCompat.getColor(llParent9.getContext(), R.color.textColorAccent));
    }

    @OnClick(R.id.tvContent9)
    public void tvContent9() {
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
        if (UserCenter.needLogin()) return;
//        if (RankPermissionHelper.showSafeCenterDialog()) return;
        if (RankPermissionHelper.NoPrivileges(2)) return;
        String phone = etPhoneNum.getText().toString().replace(" ", "");
        RechargeModel model = mAdapter.getCheckModel();

        if (TextUtils.isEmpty(phone)) {
            CommonToast.toast(getString(R.string.login_input_phone));
            return;
        }
//        if (model == null) {
//            CommonToast.toast(getString(R.string.hint_select_packet));
//            return;
//        }

        if (!StringUtil.phoneLegal(phone)) {
            CommonToast.toast(getString(R.string.error_format_phone));
            return;
        }
        if (model == null) {
            if (tvContent9.getText().toString().equals("")) {
                CommonToast.toast(R.string.input_custom_money);
                return;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("sn", areaCode);
        map.put("phone", phone);
        map.put("num", model == null ? tvContent9.getText().toString() : model.getPackageX());
        map.put("price", model == null ? tvContent9.getText().toString() : model.getPrice().replace("AR", ""));
        map.put("unit", model == null ? "AR" : model.getUnit());
        UsagePayMode payMode = type == 1 ? UsagePayMode.phone_recharge : UsagePayMode.data_recharge;
        PayExtModel extModel = new PayExtModel(payMode, map);
        RepositoryFactory.getLocalRepository().saveLastRechargePhone(new RechargePhoneModel(areaCode, phone));
        startActivity(PayActivity.newInstance(getCurContext(), JsonUtil.moderToString(extModel)));
    }


    @OnClick(R.id.tvElectBill)
    public void clicktvElectBill() {
        startActivity(BillActivity.newInstance(getCurContext(), BillActivity.BILL_TYPE_ELEC));
    }

    @OnClick(R.id.flPhoneList)
    public void clicflPhoneList() {
        flPhoneList.setVisibility(View.GONE);
    }

    @OnClick(R.id.tvNetBill)
    public void clicktvNetBill() {
        startActivity(BillActivity.newInstance(getCurContext(), BillActivity.BILL_TYPE_NET));
    }

    @OnClick(R.id.tvWaterBill)
    public void clicktvWaterBill() {
        startActivity(BillActivity.newInstance(getCurContext(), BillActivity.BILL_TYPE_WATER));
    }

    @OnClick(R.id.tvAreaCode)
    public void clickAreaCode() {
//        CommonDialogHelper.showAreaCodeDialog(this, new OnSelectListener() {
//            @Override
//            public void onSelect(int position, String text) {
//                areaCode = text.replace("+", "");
//                tvAreaCode.setText(text);
//                phoneTextWatcher.setAreaCode(areaCode);
//            }
//        });
    }

    @OnClick(R.id.tvInstructions)//充值说明
    public void ClickInstructions() {
        showUpdateDialog();
    }

    @OnClick(R.id.tvCharges)//资费套餐
    public void ClickCharges() {
        CommonWebActivity.Options options = new CommonWebActivity.Options();
        StringBuilder sb = new StringBuilder();
        sb.append(Constant.HOST);
        sb.append("invite/flowAdv?ispType=");
        UserInfoModel userInfo = UserCenter.getUserInfo();
        sb.append(userInfo.getPhone().substring(0, 3));
        sb.append("&l=");
        sb.append(RepositoryFactory.getLocalRepository().getLangugeType());
        sb.append("&appOpen=1");
        Log.e("资费套餐链接:", sb.toString());
        options.setLoadUrl(sb.toString());
        startActivity(CommonWebActivity.newInstance(getCurContext(), JsonUtil.moderToString(options)));
    }

    @OnClick(R.id.tvSearch)//查询余额
    public void tvSearch() {//https://www.messageglobal-online.com/invite/search
        CommonWebActivity.Options options = new CommonWebActivity.Options();
        StringBuilder sb = new StringBuilder();
        sb.append(Constant.HOST);
        sb.append("invite/search?ispType=");
        UserInfoModel userInfo = UserCenter.getUserInfo();
        sb.append(userInfo.getPhone().substring(0, 3));
        sb.append("&l=");
        sb.append(RepositoryFactory.getLocalRepository().getLangugeType());
        sb.append("&appOpen=1");
        Log.e("资费套餐链接:", sb.toString());
        options.setLoadUrl(sb.toString());
        startActivity(CommonWebActivity.newInstance(getCurContext(), JsonUtil.moderToString(options)));
    }

    public void showUpdateDialog() {
        updateDliaog = new MaterialDialog.Builder(this)
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
}
