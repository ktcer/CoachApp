package com.cn.coachs.baidumap;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import com.cn.coachs.R;

public class MyIcon2 extends View {
    public static int w;
    public static int h;
    private Bitmap mBitmap;

    public MyIcon2(Context context) {
        super(context);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jiewo);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        w = this.getWidth() / 2 - mBitmap.getWidth() / 2;
        h = (this.getHeight() / 2 - mBitmap.getHeight() / 2) - (MyIcon.mBitmap.getHeight() / 2);
        canvas.drawBitmap(mBitmap, w, h, null);
    }
}
