package com.kanban.switchfragmaster.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.kanban.switchfragmaster.R;


/**
 * [声音提示类]
 * Created by YANGT on 2017/11/13.
 */

public class SoundPlayUtil {
    // SoundPool对象
    public static SoundPool mSoundPlayer = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
    public static SoundPlayUtil soundPlayUtils;
    // 上下文
    static Context mContext;

    /**
     * 初始化
     *
     * @param context
     */
    public static SoundPlayUtil init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPlayUtil();
        }

        // 初始化声音
        mContext = context;

        mSoundPlayer.load(mContext, R.raw.lb_ok, 1);// 1
        mSoundPlayer.load(mContext, R.raw.lb_ng, 1);// 2
        mSoundPlayer.load(mContext, R.raw.lb_warning, 1);// 3
        return soundPlayUtils;
    }

    //OK/NG声音播放
    public static void playOkNg(boolean isOkSound) {
        int soundID =1;
        if(isOkSound)
        {
            soundID=1;
        }
        else
        {
            soundID=2;
        }
        mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
    }

    /**
     * 播放声音
     *
     * @param soundID
     */
    public static void play(int soundID) {
        mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
    }

    //报警声音播放
    public static int playWarning(int soundID) {
        int streamID;
        streamID = mSoundPlayer.play(soundID, 1, 1, 0, -1, 1);
        return streamID;
    }

    //暂停指定播放流的音效
    public static void pause(int streamID){
        mSoundPlayer.pause(streamID);
    }

    //继续播放指定播放流的音效
    public static void resume(int streamID){
        mSoundPlayer.resume(streamID);
    }

    //终止指定播放流的音效
    public static void stop(int streamID){
        mSoundPlayer.stop(streamID);
    }

    //卸载一个指定的音频资源
    public static boolean unload(int soundID){
        mSoundPlayer.unload(soundID);
        return true;
    }

    //释放SoundPool中的所有音频资源
    public static void release(){
        mSoundPlayer.release();
    }
}
