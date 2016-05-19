/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cn.coachs.ui.chat.ui;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.cn.coachs.ui.chat.common.AudioManagerTools;
import com.cn.coachs.ui.chat.common.utils.LogUtil;
import com.cn.coachs.util.CustomProgressDialog;

/**
 * 自定义BaseFragment，处理上下音量键按下事件
 * Created by Jorstin on 2015/3/18.
 */
public abstract class BaseFragment extends CCPFragment {
    protected CustomProgressDialog dialogProgress;

    //显示载入弹窗
    public void showProgressBar() {
        hideProgressBar();
        dialogProgress = new CustomProgressDialog(getActivity(), "正在加载中");
        dialogProgress.show();
    }

    //隐藏载入弹窗
    public void hideProgressBar() {
        if (dialogProgress != null)
            dialogProgress.dismiss();
    }

    /**
     * 当前CCPFragment所承载的FragmentActivity实例
     */
    private FragmentActivity mActionBarActivity;
    private AudioManager mAudioManager;

    /**
     * AudioManager.STREAM_MUSIC类型的音量最大值
     */
    private int mMusicMaxVolume;

    /**
     * 设置ActionBarActivity实例
     *
     * @param activity
     */
    public void setActionBarActivity(FragmentActivity activity) {
        this.mActionBarActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAudioManager = AudioManagerTools.getInstance().getAudioManager();
        mMusicMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 自定义页面方法,处理上下音量键按下事件
     *
     * @param keyCode
     * @param event
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP)
                && mAudioManager != null) {
            int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (streamVolume >= mMusicMaxVolume) {
                LogUtil.d(LogUtil.getLogUtilsTag(BaseFragment.class),
                        "has set the max volume");
                return true;
            }
            int mean = mMusicMaxVolume / 7;
            if (mean == 0) {
                mean = 1;
            }
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    streamVolume + mean, AudioManager.FLAG_PLAY_SOUND
                            | AudioManager.FLAG_SHOW_UI);
            return true;
        }
        if ((event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
                && mAudioManager != null) {
            int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int mean = mMusicMaxVolume / 7;
            if (mean == 0) {
                mean = 1;
            }
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    streamVolume - mean, AudioManager.FLAG_PLAY_SOUND
                            | AudioManager.FLAG_SHOW_UI);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}

