package com.fjx.mg.me.wallet.recharge;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.me.safe_center.lock.GestureLockActivity;
import com.fjx.mg.me.wallet.recharge.success.SuccessfulShowActivity;
import com.fjx.mg.pay.PayActivity;
import com.fjx.mg.recharge.center.RechargeAdapter;
import com.fjx.mg.utils.RankPermissionHelper;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.fjx.mg.utils.Unicode;
import com.fjx.mg.view.PriceTextWatcher;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.NetworkUtil;
import com.library.common.utils.StatusBarManager;
import com.library.repository.Constant;
import com.library.repository.models.PayStatusModel;
import com.library.repository.models.RechargeModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.TabModel;
import com.library.repository.models.WebSocketModel;
import com.tencent.qcloud.uikit.TimConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @author hanlz
 * date:         2019年11月4日10:37:28
 * description：余额充值
 */
public class BalanceRechargeActivity extends BaseMvpActivity<BalanceRechargePresenter> implements BalanceRechargeContract.View {

    @BindView(R.id.s_tab)
    CommonTabLayout tabLayout;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;

    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.viewMine)
    LinearLayout viewMine;
    @BindView(R.id.viewMineX)
    LinearLayout viewMineX;

    public static final String broadcast_capture = "jason.UserWithDrawalActivity.Band";
    @BindView(R.id.ediUserWithDrAmount)
    EditText amount;//提现金额

    @BindView(R.id.tvUserWithDrPay)
    TextView pay;//实付现金

    @BindView(R.id.tvUserWithDrServiceCharge)
    TextView serviceCharge;//手续费


    private Boolean types = true;
    private RechargeAdapter mAdapter;
    Dialog dialog;

    @Override
    protected BalanceRechargePresenter createPresenter() {
        return new BalanceRechargePresenter(this);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, BalanceRechargeActivity.class);
    }

    @Override
    public void ConvertSuccess(String servicePrice, String totalPrice) {
        pay.setText(totalPrice);
        serviceCharge.setText(servicePrice);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_recharge_balance;
    }


    @Override
    public void showTabAndItems(ArrayList<CustomTabEntity> tabEntitys) {
        tabLayout.setTabData(tabEntitys);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                if (position == 0) {
                    types = true;
                    viewMineX.setVisibility(View.VISIBLE);
                    viewMine.setVisibility(View.GONE);
                } else {
                    types = false;
                    viewMineX.setVisibility(View.GONE);
                    viewMine.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    protected void initView() {
        ArrayList<CustomTabEntity> tabEntity = new ArrayList<>();
        tabEntity.add(new TabModel(getCurContext().getString(R.string.Pay_phone_billx)));
        tabEntity.add(new TabModel(getCurContext().getString(R.string.Pay_phone_billxx)));
        showTabAndItems(tabEntity);
        SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
        String string = sp.getString("bwalletbalance");
        if (string != null && !string.equals("")) {
            tvBalance.setText(string);
        }
        mPresenter.TextWatch(amount);

        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        ToolBarManager.with(this).setTitle(getString(R.string.Balance_recharge), R.color.white).setBackgroundColor(R.color.colorAccent)
                .setNavigationIcon(R.drawable.iv_back);
        GradientDrawableHelper.whit(tvConfirm).setColor(R.color.colorAccent).setCornerRadius(50);
        mPresenter.balancePackage();
        mPresenter.getUserBalance();
        etMoney.addTextChangedListener(new PriceTextWatcher());

        mAdapter = new RechargeAdapter();
        mAdapter.setPhoneCharge(false);
        recycler.setLayoutManager(new GridLayoutManager(getCurContext(), 3));
        recycler.setAdapter(mAdapter);
        recycler.addItemDecoration(new SpacesItemDecoration(10, 10));
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                mAdapter.reCheck(position);

                RechargeModel model = mAdapter.getItem(position);
                etMoney.setText(model.getPackageX().replace("AR", ""));
                etMoney.setSelection(etMoney.getText().length());
            }
        });
        try {
            Gson gson = new Gson();
            String json = sp.getString("showDataList");
            if (!json.equals("")) {
                Log.e("showDataList:", "" + json);
                List<RechargeModel> statusLs = gson.fromJson(json, new TypeToken<List<RechargeModel>>() {
                }.getType());
                if (statusLs != null) {
                    showDataList(statusLs);
                }
            }
        } catch (Exception e) {
            Log.e("Exception:", "" + e.toString());
        }
        sp.close();
        WebSocket();

        IntentFilter filter = new IntentFilter(broadcast_capture);
        this.registerReceiver(broadcastReceiver, filter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPresenter.Band(intent.getStringExtra("clientId"));
        }
    };
    private WebSocket mSocket;
    private long sendTime = 0L;
    private Handler mHandler = new Handler();
    private static final long HEART_BEAT_RATE = 2 * 1000;
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {

                String message = sendData();
                if (mSocket != null) {
                    mSocket.send(message);

                    sendTime = System.currentTimeMillis();
                }
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };


    private void WebSocket() {
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间
                .build();
        Request request = new Request.Builder().url(TimConfig.getWebSocket()).build();
        EchoWebSocketListener socketListener = new EchoWebSocketListener();
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
        mOkHttpClient.newWebSocket(request, socketListener);
        mOkHttpClient.dispatcher().executorService().shutdown();
    }

    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            mSocket = webSocket;
            output("连接成功！");
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            output("receive bytes:" + bytes.hex());
        }

        @Override
        public void onMessage(WebSocket webSocket, String s) {
            super.onMessage(webSocket, s);
            String text = Unicode.convertUnicode(s);
            output("服务器端发送来的信息：" + text);
            Gson gson = new Gson();
            if (text.contains("clientId")) {
                try {

                    WebSocketModel statusLs = gson.fromJson(text, new TypeToken<WebSocketModel>() {
                    }.getType());
                    String clientId = statusLs.getClientId();
                    Intent intent = new Intent(broadcast_capture);
                    intent.putExtra("clientId", clientId);
                    sendBroadcast(intent);
                } catch (Exception e) {
                    Looper.prepare();
                    Log.e("Exceptions", "" + e.toString());
                    Looper.loop();

                }

            } else if (text.contains("payTip")) {
                PayStatusModel PayStatus = gson.fromJson(text, new TypeToken<PayStatusModel>() {
                }.getType());
                if (PayStatus.getPayTip().equals("支付成功")) {
                    mPresenter.playSound();
                    mPresenter.dismiss();

                    startActivity(SuccessfulShowActivity.newInstance(getCurContext(), PayStatus.getRealPrice(), PayStatus.getPrice(), PayStatus.getServicePrice(), Constant.SuccessfullShowType.RC, true));
                    finish();
                }
            }
            final String message = sendHeart();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mSocket.send(message);
                }
            }, 1000);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            output("closed:" + reason);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            output("closing:" + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            output("failure:" + t.getMessage());
        }
    }

    private void output(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", text);
            }
        });
    }


    private String sendHeart() {
        Map<String, Object> mapHead = new HashMap<>();
        mapHead.put("heart", "heart");
        Gson gson = new Gson();
        return gson.toJson(mapHead);
    }

    private String sendData() {
        Map<String, Object> mapHead = new HashMap<>();
        mapHead.put("qrCode", "123456");
        Gson gson = new Gson();
        return gson.toJson(mapHead);
    }

    @Override
    public void responseFailed(ResponseModel data) {
        tvConfirm.setEnabled(true);
        if (dialog.isShowing() && dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void responseSuccess() {
        tvConfirm.setEnabled(true);
        if (dialog.isShowing() && dialog != null) {
            dialog.dismiss();
        }
    }


    @OnClick(R.id.tvConfirm)
    public void onViewClicked() {


        if (RankPermissionHelper.setPayPsw()) return;


        if (types) {
            String price = etMoney.getText().toString();
            if (!TextUtils.isEmpty(price.trim())&&Integer.parseInt(price.trim()) < 1000) {
                CommonToast.toast(R.string.less_1000_ar);
                return;
            }
            Map<String, Object> map = new HashMap<>();
            if (TextUtils.isEmpty(price)) {
                RechargeModel model = mAdapter.getCheckModel();
                map.put("price", model.getPackageX().replace("AR", ""));
            } else {
                float e = Float.parseFloat(price);
                if (e < Constant.limitAmount) {
                    CommonToast.toast(getString(R.string.limit_amount));
                    return;
                }
                map.put("price", price);
            }


            PayExtModel extModel = new PayExtModel(UsagePayMode.recharge_balance, map);
            startActivityForResult(PayActivity.newInstance(getCurContext(), JsonUtil.moderToString(extModel), getString(R.string.recharge)), 111);
        } else {
            String amountText=amount.getText().toString().trim();
            if (!TextUtils.isEmpty(amountText)&&Integer.parseInt(amountText) < 1000) {
                CommonToast.toast(R.string.less_1000_ar);
                return;
            }
            dialog = new Dialog(getCurActivity());
            dialog.setContentView(R.layout.dialog_qr_layout);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            tvConfirm.setEnabled(false);
            if (!dialog.isShowing() && dialog != null) {
                dialog.show();
            }
            mPresenter.QRCollectionCode(amount.getText().toString());
        }


    }

    @Override
    public void closeDialog() {
        tvConfirm.setEnabled(true);
    }

    @Override
    public void showDataList(List<RechargeModel> datas) {
        SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
        Gson gson = new Gson();
        sp.putString("showDataList", gson.toJson(datas));
        sp.close();
        mAdapter.setList(datas);
    }

    @Override
    public void getBalanceSuccess(String balance) {
        SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
        sp.putString("bwalletbalance", balance);
        sp.close();
        tvBalance.setText(balance);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 111 && requestCode == 111) {
            setResult(resultCode);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (broadcastReceiver!=null)
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
