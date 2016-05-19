package com.cn.coachs.ui.patient.homepage.doctorinterview;

import java.net.URL;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.cn.coachs.R;
import com.cn.coachs.util.Constant;

public class VideoPlay extends Activity {
    private String urlStream = "http://114.112.74.20/www.imediciner.com.cn/lnrjkzhgl/04/vts_01_4.m3u8";
    private VideoView myVideoView;
    private URL url;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video_play);
        myVideoView = (VideoView) this.findViewById(R.id.my_video_view);
        MediaController mc = new MediaController(this);
        myVideoView.setMediaController(mc);
        // urlStream =
        // "http://114.112.74.20/www.imediciner.com.cn/lnrjkzhgl/01/vts_01_1.m3u8";
        urlStream = getIntent().getExtras().getString(Constant.VEDIO_URL);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (urlStream != null) {
                        myVideoView.setVideoURI(Uri.parse(urlStream));
                        myVideoView.start();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
