package com.cn.coachs.ui.patient.others.myaccount;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.coachs.R;
import com.cn.coachs.model.personinfo.BeanPhoto;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.chat.common.dialog.ECListDialog;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsImage;
import com.cn.coachs.util.UtilsSharedData;
import com.cn.coachs.util.customgallery.GalleryHelper;
import com.cn.coachs.util.customgallery.model.BeanPhotoInfo;
import com.cn.coachs.util.horizontallistview.adapter.HorizontalPicAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的照片管理页面
 *
 * @author kuangtiecheng
 */
public class ActivityMyPhotos extends ActivityBasic implements OnClickListener {
    private TextView titleText;
    private ImageView iv_photoIdentify;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private List<BeanPhoto> photoList = new ArrayList<BeanPhoto>();
    private ArrayList<String> picList = new ArrayList<String>();
    /**
     * 用户类型，0表示患者
     */
    private static final String userType = "1";
    private long userId;
    // /*上传照片*/
    private GridView gridView; // 网格显示缩略图
    private int nowEditPhoto = -1;
    private HorizontalPicAdapter adapter; // 适配器

    /**
     * 0-5代表生活照，6代表证件照
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myphoto);
        setdata();
        getData();
        initial();
    }

    private void setdata() {
        UtilsSharedData.initDataShare(this);
        options = AppMain.initImageOptions(R.drawable.default_pic, true);
        imageLoader = ImageLoader.getInstance();
        userId = UtilsSharedData.getLong(Constant.USER_ID, 0);
    }

    private void getData() {
        // 创建文件夹
//		CreateFolder.createRegisterFolder(UtilsSharedData
//				.getValueByKey(Constant.USER_ACCOUNT));
    }

    private void initial() {
        titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("照片");
        iv_photoIdentify = (ImageView) findViewById(R.id.image_certification);
        imageLoader.displayImage(
                AbsParam.getBaseUrl()
                        + UtilsSharedData
                        .getValueByKey(Constant.USER_CREDITURL),
                iv_photoIdentify, options);
        iv_photoIdentify.setOnClickListener(this);
        GridView();
    }

    private void GridView() {
        gridView = (GridView) findViewById(R.id.photo1);
        String jsonString = UtilsSharedData.getValueByKey(Constant.USER_PHOTOS);
        Gson gson = new Gson();
        photoList = gson.fromJson(jsonString, new TypeToken<List<BeanPhoto>>() {
        }.getType());
        if (photoList == null) {
            photoList = new ArrayList<BeanPhoto>();
        }
        // 添加几张默认照片
        for (int i = 0; i < photoList.size(); i++) {
            picList.add(photoList.get(i).getUrl());
        }
        adapter = new HorizontalPicAdapter(this, picList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                nowEditPhoto = arg2;
                if (arg2 < picList.size()) {
                    showDialog();
                } else if (arg2 == picList.size()) {
                    //最后一张需要做判断
                    if (picList.size() == 6) {
                        //如果是6张图片的最后一张
                        showDialog();
                    } else {
                        //如果是最后一个添加按钮，则不能显示查看大图
                        //直接选择图片
                        GalleryHelper.openGallerySingle(ActivityMyPhotos.this, true, new GalleryImageLoader());
                    }
                }

            }

        });
    }

    protected void onRestart() {
        // adapter.update();
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.image_certification:
                nowEditPhoto = 6;
                showDialog();
                break;
        }
    }

    public void showDialog() {
        ECListDialog dialog;
        dialog = new ECListDialog(this, (new String[]{
                "修改照片", "查看大图"}));

        dialog.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
            @Override
            public void onDialogItemClick(Dialog d, int position) {
                switch (position) {
                    case 0:
                        GalleryHelper.openGallerySingle(ActivityMyPhotos.this, false, new GalleryImageLoader());
                        break;
                    case 1:
                        if (nowEditPhoto == 6) {
                            UtilsImage
                                    .displayBigPic(
                                            ActivityMyPhotos.this,
                                            UtilsSharedData
                                                    .getValueByKey(Constant.USER_CREDITURL));
                        } else {
                            UtilsImage.displayBigPics(ActivityMyPhotos.this,
                                    picList, nowEditPhoto);
                        }
                        break;
                    default:
                        break;
                }

            }
        });
        String title = "请选择操作";
        dialog.setTitle(title);
        dialog.show();

    }


    public boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryHelper.GALLERY_REQUEST_CODE) {
            if (resultCode == GalleryHelper.GALLERY_RESULT_SUCCESS) {
                BeanPhotoInfo photoInfo = data.getParcelableExtra(GalleryHelper.RESULT_DATA);
                List<BeanPhotoInfo> photoInfoList = (List<BeanPhotoInfo>) data.getSerializableExtra(GalleryHelper.RESULT_LIST_DATA);

                if (photoInfo != null) {
                    setPicAndUploadPic(photoInfo);
                }

                if (photoInfoList != null) {
                    Toast.makeText(this, "选择了" + photoInfoList.size() + "张", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 选择完图片之后进行相应的操作
     *
     * @param photoInfo
     */
    private void setPicAndUploadPic(BeanPhotoInfo photoInfo) {
        switch (nowEditPhoto) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
//			picList.add(nowEditPhoto, "file:/"+photoInfo.getPhotoPath());
//			adapter.notifyDataSetChanged();
//			AscyncSubmitPersonalInfo mTask = new AscyncSubmitPersonalInfo(
//					ActivityMyPhotos.this, userId , nowEditPhoto<photoList.size()?photoList.get(nowEditPhoto).getId():-1, photoInfo.getPhotoPath(),
//					"shz0"+nowEditPhoto) {
//				@Override
//				protected void onPostExecute(BeanPersonInfo result) {
//					// TODO Auto-generated method stub
//					super.onPostExecute(result);
//					if (result.getResult() == 1) {
//						// 完成上传服务器后,删除本地临时缓存图片
//						picList.clear();
//						photoList.clear();
////						for(BeanPhoto bean :result.getPicurl()){
////							picList.add(bean.getUrl());
////							photoList.add(bean);
////						}
//						adapter.notifyDataSetChanged();
//						FileUtils.deleteDir();
//					}
//				}
//
//			};
//			mTask.execute();
                break;
            case 6:
                ImageLoader.getInstance().displayImage("file:/" + photoInfo.getPhotoPath(), iv_photoIdentify);
//			AscyncSubmitPersonalInfo mTask1 = new AscyncSubmitPersonalInfo(
//					ActivityMyPhotos.this, userId + "", -1, photoInfo.getPhotoPath(),
//					"zgzsImgUrl") {
//
//				@Override
//				protected void onPostExecute(BeanPersonInfo result) {
//					// TODO Auto-generated method stub
//					super.onPostExecute(result);
//					if (result.getId() == 1) {
//						// 完成上传服务器后,删除本地临时缓存图片
//						FileUtils.deleteDir();
//					}
//				}
//
//			};
//			mTask1.execute();
                break;

            default:
                break;
        }
    }

}
