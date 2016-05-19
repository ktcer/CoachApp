package com.cn.coachs.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.cn.coachs.ui.chat.common.CCPAppManager;
import com.cn.coachs.ui.chat.ui.chatting.ViewImageInfo;
import com.google.zxing.WriterException;

public class UtilsImage {
    /*
	 * 显示大图(单图)
	 */

    public static void displayBigPic(Activity mContext, String url) {

        if (!url.equals("")) {
            String fullUrl = AbsParam.getBaseUrl() + url;
            int position = 0;
            ViewImageInfo beanPic = new ViewImageInfo(fullUrl, fullUrl);
            ArrayList<ViewImageInfo> urls = new ArrayList<ViewImageInfo>();
            urls.add(beanPic);
            if (urls == null || urls.isEmpty()) {
                return;
            }
            CCPAppManager
                    .startChattingImageViewAction(mContext, position, urls);
        }
    }

	/*
	 * 显示大图(多图)
	 */

    public static void displayBigPics(Activity mContext,
                                      List<String> urls, int position) {

        if (urls == null) {
            return;
        }
        if (urls.size() == 0) {
            return;
        }
        ArrayList<ViewImageInfo> imageUrls = new ArrayList<ViewImageInfo>();
        for (String url : urls) {
            ViewImageInfo beanPic = new ViewImageInfo(AbsParam.getBaseUrl()
                    + url, AbsParam.getBaseUrl() + url);
            imageUrls.add(beanPic);
        }

        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }
        CCPAppManager.startChattingImageViewAction(mContext, position,
                imageUrls);
    }

    /**
     * 将URI转化为Bitmap
     *
     * @param context
     * @param uri
     * @return
     */
//	public static Bitmap getBitmapFromUri(Context context, Uri uri) {
//		Bitmap bitmap = null;
//		try {
//			// 读取uri所在的图片
//			// 不管是拍照还是选择图片每张图片都有在数据中存储也存储有对应旋转角度orientation值
//			// 所以我们在取出图片是把角度值取出以便能正确的显示图片,没有旋转时的效果观看
//
//			ContentResolver cr = context.getContentResolver();
//			Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
//			if (cursor != null) {
//				cursor.moveToFirst();// 把游标移动到首位，因为这里的Uri是包含ID的所以是唯一的不需要循环找指向第一个就是了
//				String filePath = cursor.getString(cursor
//						.getColumnIndex("_data"));// 获取图片路
//				String orientation = cursor.getString(cursor
//						.getColumnIndex("orientation"));// 获取旋转的角度
//				cursor.close();
//				if (filePath != null) {
//					bitmap = BitmapFactory.decodeFile(filePath);// 根据Path读取资源图片
//					int angle = 0;
//					if (orientation != null && !"".equals(orientation)) {
//						angle = Integer.parseInt(orientation);
//					}
//					if (angle != 0) {
//						// 下面的方法主要作用是把图片转一个角度，也可以放大缩小等
//						Matrix m = new Matrix();
//						int width = bitmap.getWidth();
//						int height = bitmap.getHeight();
//						m.setRotate(angle); // 旋转angle度
//						bitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
//								height, m, true);// 从新生成图片
//
//					}
//				}
//			}
//			return bitmap;
//		} catch (Exception e) {
//			return null;
//		}
//	}
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据用户信息生成二维码
     *
     * @param image
     */
    public static void getcodePic(ImageView image, String str, int widthAndHeight) {
        try {
            Bitmap qrCodeBitmap = EncodingHandler.createQRCode(str, widthAndHeight);
//			Bitmap qrCodeBitmap = EncodingHandler.createQRCode("addexpert"
//					+ "," + userId + "," + imageUrl, 350);// addexpert表示加专家
            saveJpeg(qrCodeBitmap);
            image.setImageBitmap(qrCodeBitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    private static String initSavePath() {
        File dateDir = Environment.getExternalStorageDirectory();
        String path = AppDisk.PICTUREPOLDER + "/MyCode/";
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return path;
    }

    private static void saveJpeg(Bitmap bm) {

        long dataTake = System.currentTimeMillis();
        String jpegName = initSavePath() + dataTake + ".jpg";

        // File jpegFile = new File(jpegName);
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);

            // Bitmap newBM = bm.createScaledBitmap(bm, 600, 800, false);

            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }
    }
}
