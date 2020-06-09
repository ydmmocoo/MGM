package com.fjx.mg.video;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.fjx.mg.R;
import com.library.common.base.BaseActivity;
import com.library.common.constant.IntentConstants;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author    by hanlz
 * Date      on 2019/11/14.
 * Description：视频播放页使用原生VideoView
 */
public class VideoActivity extends BaseActivity {

    @BindView(R.id.videoView)
    VideoView mVideoView;
    @BindView(R.id.tvNetworkSpeed)
    TextView mTvNetworkSpeed;
    @BindView(R.id.llLoading)
    LinearLayout mLlLoading;
    @BindView(R.id.ivReset)
    ImageView mIvReset;
    @BindView(R.id.tvPercentage)
    TextView mTvPercentage;
    @BindView(R.id.clPercentage)
    ConstraintLayout mClPercentage;

    //    private NetSpeedTimer mNetSpeedTimer;
//    private NerworkHanlder mHandler = new NerworkHanlder();
    private PercentageHanlder mHandler = new PercentageHanlder();
    private PercentageRunnable mRunnable;
    private MediaController mLocalMediaController;

    public static Intent newIntent(Context context, String videPath) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(IntentConstants.VIDE_PATH, videPath);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_video;
    }


    @Override
    protected void initView() {
        super.initView();
        if (getIntent() == null) {
            return;
        }
        //创建NetSpeedTimer实例
//        mNetSpeedTimer = new NetSpeedTimer(getCurContext(), new NetSpeed(), mHandler).setDelayTime(1000).setPeriodTime(2000);
//        mNetSpeedTimer.startSpeedTimer();
        mRunnable = new PercentageRunnable(1000);
        mHandler.post(mRunnable);
        mLocalMediaController = new MediaController(this);
        mVideoView.setMediaController(mLocalMediaController);
        String url = getIntent().getStringExtra(IntentConstants.VIDE_PATH);
        mVideoView.setVideoPath(url);
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
//                    if (mNetSpeedTimer != null) {
//                        //在想要开始执行的地方调用该段代码
//                        mNetSpeedTimer.startSpeedTimer();
//                    }
//                    mLlLoading.setVisibility(View.VISIBLE);
                    mClPercentage.setVisibility(View.VISIBLE);
                    if (mRunnable == null)
                        mRunnable = new PercentageRunnable(1000);
                    mHandler.post(mRunnable);
                    mIvReset.setVisibility(View.GONE);
                } else {
//                    if (mNetSpeedTimer != null) {
//                        mNetSpeedTimer.stopSpeedTimer();
//                    }
//                    mLlLoading.setVisibility(View.GONE);
                    if (mHandler != null && mRunnable != null) {
                        mHandler.removeCallbacks(mRunnable);
                    }
                    mClPercentage.setVisibility(View.GONE);
                }
                return true;
            }
        });
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mIvReset.setVisibility(View.GONE);
                mVideoView.start();
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mIvReset.setVisibility(View.VISIBLE);
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                mIvReset.setVisibility(View.VISIBLE);
                return true;
            }
        });

    }

    @OnClick({R.id.ivBack, R.id.ivReset})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                if (mVideoView != null) {
                    mVideoView.suspend();
                    mVideoView.setOnErrorListener(null);
                    mVideoView.setOnPreparedListener(null);
                    mVideoView.setOnCompletionListener(null);
                    mVideoView = null;
                    mLocalMediaController.removeAllViews();
                }
//                if (mNetSpeedTimer != null) {
//                    mNetSpeedTimer.stopSpeedTimer();
//                }
                if (mHandler != null && mRunnable != null) {
                    mHandler.removeCallbacks(mRunnable);
                }
                finish();
                break;
            case R.id.ivReset:
                mVideoView.start();
                mIvReset.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    protected void onDestroy() {
//        if (mNetSpeedTimer != null) {
//            mNetSpeedTimer.stopSpeedTimer();
//        }
        if (mHandler != null && mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        if (mVideoView != null) {
            mVideoView.suspend();
            mVideoView.setOnErrorListener(null);
            mVideoView.setOnPreparedListener(null);
            mVideoView = null;
            mLocalMediaController.removeAllViews();
        }
        super.onDestroy();
    }

    //    class NerworkHanlder extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == NetSpeedTimer.NET_SPEED_TIMER_DEFAULT) {
//                String speed = (String) msg.obj;
//                mTvNetworkSpeed.setText(speed);
//            }
//
//        }
//    }
    class PercentageHanlder extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    }

    class PercentageRunnable implements Runnable {
        int time;

        public PercentageRunnable(int time) {
            this.time = time;
        }

        @Override
        public void run() {
            mTvPercentage.setText(mVideoView.getBufferPercentage() + "%");
            mHandler.postDelayed(this, time);
            mClPercentage.setVisibility(View.VISIBLE);
        }
    }
}
