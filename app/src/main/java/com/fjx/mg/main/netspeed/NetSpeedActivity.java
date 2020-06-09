package com.fjx.mg.main.netspeed;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.utils.FileCache;
import com.fjx.mg.utils.HttpUtil;
import com.library.common.base.BaseActivity;
import com.library.common.utils.NetworkUtil;
import com.library.repository.Constant;
import com.library.repository.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Author    by hanlz
 * Date      on 2020/1/19.
 * Description：
 */
public class NetSpeedActivity extends BaseActivity {

    private final static String TAG = "IcsTestActivity";
    private final static String ALBUM_PATH
            = Environment.getExternalStorageDirectory() + "/download_test/";

    @BindView(R.id.tvNetDelay)
    TextView mTvNetDelay;
    @BindView(R.id.tvNetException)
    TextView mTvNetException;
    @BindView(R.id.tvNetExceptionInfo)
    TextView mTvNetExceptionInfo;
    @BindView(R.id.tvDownLoadSpeed)
    TextView mTvDownLoadSpeed;
    @BindView(R.id.ivNetSpeedCursor)
    ImageView mIvNetSpeedCursor;
    @BindView(R.id.tvUpLoadSpeed)
    TextView mTvUpLoadSpeed;
    @BindView(R.id.tvStart)
    TextView mTvStart;
    private CountDownTimer downTimer;
    private CountDownTimer upTime;
    private List<Integer> downLoadSpeeds = new ArrayList<>();
    private List<Integer> upLoadSpeeds = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isFinishing()) return;
            if (msg.what == 1) {//下载
                stopService(new Intent(NetSpeedActivity.this, NetSpeedService.class));
                int avg = 0;
                for (int i = 0; i < downLoadSpeeds.size(); i++) {
                    avg = avg + downLoadSpeeds.get(i);
                }
                if (isFinishing() || avg == 0) return;
                mTvDownLoadSpeed.setText(String.valueOf(avg / downLoadSpeeds.size()));
                Log.d("数据", "success " + avg / downLoadSpeeds.size());
            } else if (msg.what == 2) {//上传
                lastX = 0;
                setCursorRotateAnimation(0);
                int avg = 0;
                for (int i = 0; i < upLoadSpeeds.size(); i++) {
                    avg = avg + upLoadSpeeds.get(i);
                }
                if (isFinishing() || avg == 0) return;
                mTvUpLoadSpeed.setText(String.valueOf(avg / upLoadSpeeds.size()));
                showTestSpeedResult();
                stopService(new Intent(NetSpeedActivity.this, UploadNetSpeedService.class));
                Log.d("数据", "upload success " + avg / upLoadSpeeds.size());
            } else if (msg.what == 3) {//下载错误
                mTvDownLoadSpeed.setText("--");
                stopService(new Intent(NetSpeedActivity.this, NetSpeedService.class));
            } else if (msg.what == 4) {//上传错误
                lastX = 0;
                setCursorRotateAnimation(0);
                mTvUpLoadSpeed.setText("--");
                stopService(new Intent(NetSpeedActivity.this, UploadNetSpeedService.class));
            } else if (msg.what == 5) {//开启服务
                //实时监听网路变化情况
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("com.fjx.net.speed.upload");
                BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        int speed = intent.getIntExtra("speed", 0);
                        Log.d("数据", speed + "upload");
                        upLoadSpeeds.add(speed);
                        calRotate(speed);
                        if (isFinishing()) return;
                        mTvUpLoadSpeed.setText(speed + "");
                    }
                };

                registerReceiver(broadcastReceiver, intentFilter);
                startService(new Intent(NetSpeedActivity.this, UploadNetSpeedService.class));
            }
        }
    };
    private HttpUtil httpUtil;

    @Override
    protected int layoutId() {
        return R.layout.act_net_speed;
    }

    @Override
    protected void initView() {
        super.initView();
        ToolBarManager.with(this).setTitle(getString(R.string.net_speed));
        httpUtil = new HttpUtil();
        httpUtil.init();
    }

    @OnClick(R.id.tvStart)
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvStart://开始测速
                //开始测试时 先复位表盘和上次测速数据
                mTvNetDelay.setText("--");
                mTvDownLoadSpeed.setText("--");
                mTvUpLoadSpeed.setText("--");
                mTvNetException.setText("");
                mTvNetExceptionInfo.setText("");
                lastX = 0;
                if (new NetworkUtil().isNetworkConnected(this)) {
                    //依次从ping-download-upload测速
                    if (ping()) {
                        autoGetDownLoadSpeed();
                    } else {
                        //无网络
                        mTvStart.setEnabled(true);
                        mTvStart.setText(getString(R.string.start_test_speed));
                        mTvNetException.setText(getString(R.string.net_speed_error));
                        mTvNetException.setTextColor(getResources().getColor(R.color.c_e62c30));
                        mTvNetExceptionInfo.setText(getString(R.string.please_input_net_status));
                    }
                } else {
                    //无网络
                    mTvStart.setEnabled(true);
                    mTvStart.setText(getString(R.string.start_test_speed));
                    mTvNetException.setText(getString(R.string.net_speed_error));
                    mTvNetException.setTextColor(getResources().getColor(R.color.c_e62c30));
                    mTvNetExceptionInfo.setText(getString(R.string.please_input_net_status));
                }

                break;
            default:
                break;
        }
    }

    private boolean ping() {
        mTvStart.setEnabled(false);
        mTvStart.setText(getString(R.string.is_speed));
        Runtime runtime = Runtime.getRuntime();
        Process mIpAddrProcess;
        try {
            mIpAddrProcess = runtime.exec("ping -c 1 139.9.38.218");
            int waitFor = mIpAddrProcess.waitFor();
            int i = mIpAddrProcess.exitValue();
            System.out.println(" waitFor " + waitFor);
            System.out.println(" waitFor " + i);
            if (waitFor == 0) {
                InputStream inputStream = mIpAddrProcess.getInputStream();
                String s = stream2Log(inputStream);
                LogUtil.printJson("数据", s, "ping");
                int index1 = s.indexOf("time=");
                int index2 = s.indexOf("ms---");
                if (index1 != -1 && index2 != -1) {
                    String substring = s.substring(index1, index2);
                    String delaySpeed = substring.substring(5);
                    Log.d("数据", delaySpeed);
                    mTvNetDelay.setText(delaySpeed);
                }
                mIpAddrProcess.destroy();
                return true;
            } else {
                InputStream errorStream = mIpAddrProcess.getErrorStream();
                stream2Log(errorStream);
                mIpAddrProcess.destroy();
                return false;
            }
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:" + ignore);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(" Exception:" + e);
            return false;
        }
    }

    public String stream2Log(InputStream stream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.d("数据", sb.toString());
        }
        return sb.toString();
    }


    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 3, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(50));

    private void autoGetDownLoadSpeed() {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    httpUtil.downloadImage("http://139.9.38.218/Uploads/testdownload/1.jpg", "tagdown1", new HttpUtil.OnSocketTimeListener() {
                        @Override
                        public void socketTimeoutException() {
                            Message msg = Message.obtain();
                            msg.what = 3;
                            mHandler.sendMessage(msg);
                        }

                        @Override
                        public void connectException() {
                            Message msg = Message.obtain();
                            msg.what = 3;
                            mHandler.sendMessage(msg);
                        }

                        @Override
                        public void onSuccess() {
                            Message msg = Message.obtain();
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                            upload();
                        }

                        @Override
                        public void onFailed() {
                            Message msg = Message.obtain();
                            msg.what = 3;
                            mHandler.sendMessage(msg);
                        }
                    });
//                    byte[] bytes = httpUtil.downloadImage(Constant.HOST + "Uploads/testdownload/1.jpg");
//                    if (bytes.length <= 0) {
//                        msg.what = 3;
//                        mHandler.sendMessage(msg);
//                    } else {
//                        FileCache.getInstance().saveToAlbum(BitmapFactory.decodeByteArray(bytes, 0, bytes.length), "test", getCurContext());
//                        msg.what = 1;
//                        msg.obj = downLoadSpeeds;
//                        mHandler.sendMessage(msg);
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        });
        //实时监听网路变化情况
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.fjx.net.speed");
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int speed = intent.getIntExtra("speed", 0);
                Log.d("数据", speed + "");
                downLoadSpeeds.add(speed);
                calRotate(speed);
                if (isFinishing()) return;
                mTvDownLoadSpeed.setText(speed + "");
            }
        };
        if (isFinishing()) return;
        registerReceiver(broadcastReceiver, intentFilter);
        startService(new Intent(NetSpeedActivity.this, NetSpeedService.class));

    }

    private void calRotate(int speed) {
        int x = 0;
        if (speed <= 32) {
            x = speed * 30 / 32;
        } else if (speed <= 64) {
            x = speed * 30 / 64;
            x += 30;
        } else if (speed <= 128) {
            x = speed * 30 / 128;
            x += 45;
        } else if (speed <= 256) {
            x = speed * 30 / 256;
            x += 75;
        } else if (speed <= 512) {
            x = speed * 30 / 512;
            x += 105;
        } else if (speed <= 1024) {
            x = speed * 30 / 1024;
            x += 135;
        } else if (speed <= 2048) {
            x = speed * 30 / 2048;
            x += 165;
        } else if (speed <= 4069) {
            x = speed * 30 / 4069;
            x += 195;
        }
        setCursorRotateAnimation((float) x);

    }

    private float lastX;

    private void setCursorRotateAnimation(float rotate) {
        try {
            Log.d("数据，", "rotate = " + rotate + "   lastX = " + lastX);
            AnimationSet animationSet = new AnimationSet(true);
            //参数1：从哪个旋转角度开始
            //参数2：转到什么角度
            //后4个参数用于设置围绕着旋转的圆的圆心在哪里
            //参数3：确定x轴坐标的类型，有ABSOLUT绝对坐标、RELATIVE_TO_SELF相对于自身坐标、RELATIVE_TO_PARENT相对于父控件的坐标
            //参数4：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴
            //参数5：确定y轴坐标的类型
            //参数6：y轴的值，0.5f表明是以自身这个控件的一半长度为x轴
            RotateAnimation rotateAnimation = new RotateAnimation(lastX, rotate,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animationSet.addAnimation(rotateAnimation);
            animationSet.setDuration(1000);
            animationSet.setFillAfter(true);
            LinearInterpolator lin = new LinearInterpolator();
            animationSet.setInterpolator(lin);
            mIvNetSpeedCursor.startAnimation(animationSet);
            lastX = rotate;
        } catch (NullPointerException e) {

        }
    }


    private void upload() {
        lastX = 0;
        Log.d("数据", "upload start");
        //实时监听网路变化情况
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.fjx.net.speed.upload");
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int speed = intent.getIntExtra("speed", 0);
                Log.d("数据", speed + "upload");
                upLoadSpeeds.add(speed);
                calRotate(speed);
                if (isFinishing()) return;
                mTvUpLoadSpeed.setText(speed + "");
            }
        };
        if (isFinishing()) return;
        registerReceiver(broadcastReceiver, intentFilter);
        startService(new Intent(NetSpeedActivity.this, UploadNetSpeedService.class));
        Message msg = Message.obtain();
        try {
            byte[] bytes = httpUtil.downloadImage("http://139.9.38.218/Uploads/testdownload/1.jpg");
            if (bytes.length == 0) {
                msg.what = 4;
                mHandler.sendMessage(msg);
            } else {
                msg.what = 2;
                mHandler.sendMessage(msg);
            }
//            httpUtil.uploadImage(Constant.HOST + "base/uploadFile", "test", new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    //文件不存在或者上传失败
//                    Message msg = Message.obtain();
//                    msg.what = 4;
//                    mHandler.sendMessage(msg);
//                }
//
//                @Override
//                public void onResponse(Call call, final Response response) throws IOException {
//                    //上传成功
//                    Message msg = Message.obtain();
//                    msg.what = 2;
//                    mHandler.sendMessage(msg);
//                }
//            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void showTestSpeedResult() {
        mTvStart.setEnabled(true);
        mTvStart.setText(getString(R.string.start_test_speed));
//        String delaySpeed = mTvNetDelay.getText().toString();
//        double v = Double.parseDouble(delaySpeed);
        int uavg = 0;
        int usize = upLoadSpeeds.size();
        for (int i = 0; i < usize; i++) {
            uavg = uavg + upLoadSpeeds.get(i);
        }
        if (uavg == 0 || usize == 0) return;
        uavg = uavg / usize;
        if (uavg >= 1024) {
            //非常好 ping<100 且 davg>512 且 uavg>512
            mTvNetException.setText(getString(R.string.net_speed_best_well));
            mTvNetException.setTextColor(getResources().getColor(R.color.c_19c608));
            mTvNetExceptionInfo.setText(getString(R.string.use_securely));
        } else if (uavg >= 512 && uavg < 1024) {
            //良好 ping<100 且 davg>512 且 uavg>512
            mTvNetException.setText(getString(R.string.net_speed_well));
            mTvNetException.setTextColor(getResources().getColor(R.color.c_19c608));
            mTvNetExceptionInfo.setText(getString(R.string.use_securely));
        } else if (uavg >= 64 && uavg < 512) {
            //正常
            mTvNetException.setText(getString(R.string.network_general));
            mTvNetException.setTextColor(getResources().getColor(R.color.c_ecbb3b));
            mTvNetExceptionInfo.setText(getString(R.string.use_securely));
        } else if (uavg >= 1 && uavg < 64) {
            //较差
            mTvNetException.setText(getString(R.string.net_speed_low));
            mTvNetException.setTextColor(getResources().getColor(R.color.c_e62c30));
            mTvNetExceptionInfo.setText(getString(R.string.switch_better_network));
        } else {
            //很差
            mTvNetException.setText(getString(R.string.net_speed_error));
            mTvNetException.setTextColor(getResources().getColor(R.color.c_e62c30));
            mTvNetExceptionInfo.setText(getString(R.string.please_input_net_status));
        }
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacks(null);
        }
        httpUtil.cancel("download");
        httpUtil.cancel("upload");
        stopService(new Intent(NetSpeedActivity.this, NetSpeedService.class));
        stopService(new Intent(NetSpeedActivity.this, UploadNetSpeedService.class));
        super.onDestroy();
    }

    private void netSpeedError() {
        mTvStart.setEnabled(true);
        mTvStart.setText(getString(R.string.start_test_speed));
        mTvNetException.setText(getString(R.string.net_speed_error));
        mTvNetException.setTextColor(getResources().getColor(R.color.c_e62c30));
        mTvNetExceptionInfo.setText(getString(R.string.please_input_net_status));
    }
}
