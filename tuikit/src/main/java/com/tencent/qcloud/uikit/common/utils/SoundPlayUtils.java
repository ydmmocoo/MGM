package com.tencent.qcloud.uikit.common.utils;

import android.media.AudioManager;
import android.media.SoundPool;

import com.library.common.utils.ContextManager;
import com.tencent.qcloud.uikit.R;

public class SoundPlayUtils {

    // SoundPool对象
    public static SoundPool mSoundPlayer = new SoundPool(10,
            AudioManager.STREAM_SYSTEM, 5);
    public static SoundPlayUtils soundPlayUtils;

    /**
     * 初始化
     */
    public static SoundPlayUtils init() {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPlayUtils();
        }
        // 初始化声音
        mSoundPlayer.load(ContextManager.getContext(), R.raw.sent_message, 1);// 1
        mSoundPlayer.load(ContextManager.getContext(), R.raw.pay_notice, 2);// 1
        mSoundPlayer.load(ContextManager.getContext(), R.raw.receive_money, 3);// 1
        return soundPlayUtils;
    }

    /**
     * 播放声音
     *
     * @param soundID
     */
    public static void play(int soundID) {
        mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
    }


}
