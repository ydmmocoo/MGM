package com.fjx.mg.main.scan;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.main.fragment.news.tab.PayCodeAdapter;
import com.fjx.mg.main.scan.setamount.SetAmountActivity;
import com.fjx.mg.utils.FileCache;
import com.fjx.mg.utils.ScreenUtils;
import com.fjx.mg.utils.Unicode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.StatusBarManager;
import com.library.repository.data.UserCenter;
import com.library.repository.models.PayStatusModel;
import com.library.repository.models.WebSocketModel;
import com.tencent.qcloud.uikit.TimConfig;

import java.util.ArrayList;
import java.util.HashMap;
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
import pub.devrel.easypermissions.EasyPermissions;

public class QRCodeCollectionActivity extends BaseMvpActivity<QRCodeCollectionPresenter> implements QRCodeCollectionContract.View {
    private String price = "";
    private ArrayList<PayStatusModel> pays = new ArrayList<>();
    private int REQUESTCODE_STORAGE = 2;
    @BindView(R.id.ivQrCode)
    ImageView ivQrCode;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvTotalMoney)
    TextView tvTotalMoney;
    @BindView(R.id.tvSetAmount)
    TextView tvSetAmount;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    public static final String broadcast_capture = "jason.broadcast.Band";
    private PayCodeAdapter adapter;
    private String uImg;
    private int tomoney = 0;
    @BindView(R.id.tvReset)
    TextView mTvReset;


    @Override
    protected QRCodeCollectionPresenter createPresenter() {
        return new QRCodeCollectionPresenter(this);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, QRCodeCollectionActivity.class);
    }


    @Override
    protected int layoutId() {
        return R.layout.colletion;
//        return R.layout.activity_qrcode_collection;
    }

    @Override
    protected void initView() {
        setListener();
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        ToolBarManager.with(this).setNavigationIcon(R.drawable.iv_back).setTitle(getString(R.string.qr_code), R.color.white).setBackgroundColor(R.color.colorAccent);
//        mPresenter.Band("7f0000010fa000000011");
        IntentFilter filter = new IntentFilter(broadcast_capture);
        this.registerReceiver(broadcastReceiver, filter);
        adapter = new PayCodeAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));
        uImg = UserCenter.getUserInfo().getUImg();
        mPresenter.QRCollectionCode(price, uImg, this);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPresenter.Band(intent.getStringExtra("clientId"));
        }
    };


    @OnClick({R.id.tvSetAmount, R.id.tvSavePicture, R.id.tvReset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSetAmount://设置金额
                if (price.equals("")) {
                    startActivityForResult(new Intent(QRCodeCollectionActivity.this, SetAmountActivity.class), 11);
                } else {
                    price = "";
                    tvSetAmount.setText(getString(R.string.set_amount));
                    tvAmount.setText("");
                    tvAmount.setVisibility(View.GONE);
                    mPresenter.QRCollectionCode("", uImg, this);
                }
                break;
            case R.id.tvSavePicture://保存图片
                requestStoragePermission();
                break;
            case R.id.tvReset:
                uImg = UserCenter.getUserInfo().getUImg();
                mPresenter.QRCollectionCode(price, uImg, this);
                break;
        }
    }


    /**
     * 保存前请求内存卡权限
     */
    private void requestStoragePermission() {
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_storage), REQUESTCODE_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            Bitmap bitmap = ScreenUtils.snapShotWithStatusBar(this);
            FileCache.getInstance().saveToAlbum(bitmap, "我的收款二维码", this);
            Toast.makeText(QRCodeCollectionActivity.this, getResources().getString(R.string.picture_save_success) + getString(R.string.photo_album), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void showQrBitmap(Bitmap bitmap) {
        ivQrCode.setImageBitmap(bitmap);
        mTvReset.setVisibility(View.GONE);
    }

    @Override
    public void downloadBitmapFailed() {
        mTvReset.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (resultCode == requestCode) {
            price = data.getStringExtra("content");
            tvAmount.setVisibility(View.VISIBLE);
            tvAmount.setText(price + " AR");
            tvSetAmount.setText(getString(R.string.clean_money));
            uImg = UserCenter.getUserInfo().getUImg();
            mPresenter.QRCode(price, this);
        }
    }

    private long sendTime = 0L;
    // 发送心跳包
    private Handler mHandler = new Handler();
    // 每隔2秒发送一次心跳包，检测连接没有断开
    private static final long HEART_BEAT_RATE = 2 * 1000;

    // 发送心跳包
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
            mHandler.postDelayed(this, HEART_BEAT_RATE); //每隔一定的时间，对长连接进行一次心跳检测
        }
    };


    private WebSocket mSocket;

    private void setListener() {
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间
                .build();
        Request request = new Request.Builder().url(TimConfig.getWebSocket()).build();
        EchoWebSocketListener socketListener = new EchoWebSocketListener();
        // 刚进入界面，就开启心跳检测
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);

        mOkHttpClient.newWebSocket(request, socketListener);
        mOkHttpClient.dispatcher().executorService().shutdown();

    }

    private Handler.Callback callback = new Handler.Callback() {

        public boolean handleMessage(Message msg) {
            tomoney = 0;
            for (int i = 0, size = pays.size(); i < size; i++) {
                if (pays.get(i).getStatus().equals("1")) {
                    String price = pays.get(i).getPrice();
                    int i1 = Integer.parseInt(price.replace("AR", ""));
                    tomoney = tomoney + i1;
                }
                tvTotalMoney.setText(getResources().getString(R.string.receiving_total) + tomoney + "AR");
            }
            if (pays.size() > 0) {
                tvTotalMoney.setVisibility(View.VISIBLE);
            }
            adapter.setList(pays);
            return false;
        }
    };
    private Handler handler = new Handler(callback);

    /**
     * 播放语音
     *
     * @param rawId
     */
    public void playSound(int rawId) {
        SoundPool soundPool;
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入音频的数量
            builder.setMaxStreams(1);
            //AudioAttributes是一个封装音频各种属性的类
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        } else {
            //第一个参数是可以支持的声音数量，第二个是声音类型，第三个是声音品质
            soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        }
        //第一个参数Context,第二个参数资源Id，第三个参数优先级
        soundPool.load(this.getApplicationContext(), rawId, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(1, 1, 1, 0, 0, 1);
            }
        });
        //第一个参数id，即传入池中的顺序，第二个和第三个参数为左右声道，第四个参数为优先级，第五个是否循环播放，0不循环，-1循环
        //最后一个参数播放比率，范围0.5到2，通常为1表示正常播放
//        soundPool.play(1, 1, 1, 0, 0, 1);
        //回收Pool中的资源
        //soundPool.release();
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
                    playSound(R.raw.sc);
                }
                pays.add(0, PayStatus);
                for (int i = 0; i < pays.size() - 1; i++) {
                    for (int j = i + 1; j < pays.size(); j++) {
                        if (pays.get(i).getuId().equals(pays.get(j).getuId())) {//1:付款成功,2:支付中,3:支付失败
                            if (!pays.get(j).getStatus().equals("1") && !pays.get(j).getStatus().equals("3")) {//支付中被支付失败代替(非支付成功!=1&&)
                                pays.remove(j);
                            }
                        }
                    }
                }
                new Thread() {

                    @Override
                    public void run() {
                        Message msg = new Message();
                        handler.sendMessage(msg);
                    }

                }.start();
            }
            //收到服务器端发送来的信息后，每隔1秒发送一次心跳包
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
                Log.e("TAG", "text: " + text);
            }
        });
    }


    private String sendData() {
        String jsonHead = "";
        Map<String, Object> mapHead = new HashMap<>();
        mapHead.put("qrCode", "123456");
        jsonHead = buildRequestParams(mapHead);
        Log.e("TAG", "sendData: " + jsonHead);
        return jsonHead;
    }


    public static String buildRequestParams(Object params) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(params);
        return jsonStr;
    }

    private String sendHeart() {
        String jsonHead = "";
        Map<String, Object> mapHead = new HashMap<>();
        mapHead.put("heart", "heart");
        jsonHead = buildRequestParams(mapHead);
        Log.e("TAG", "sendHeart：" + jsonHead);
        return jsonHead;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(broadcastReceiver);
        // 清除handler后，就不能再发送数据了
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (mSocket != null) {
            mSocket.close(1000, null);
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

    }
}
