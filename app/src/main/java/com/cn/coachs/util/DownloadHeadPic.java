package com.cn.coachs.util;

import java.io.File;
import java.io.IOException;

import android.app.Activity;

import com.cn.coachs.http.NetTool;

public class DownloadHeadPic {
    private Activity act;
    private String headName;
    private String imageUrl;
    private String path;

    public DownloadHeadPic() {
        super();
        // TODO Auto-generated constructor stub
    }

    public DownloadHeadPic(Activity act, String headName, String imageUrl) {
        super();
        this.act = act;
        this.headName = headName;
        this.imageUrl = imageUrl;
        UtilsSharedData.initDataShare(act);
        String basicFolder = AppDisk.appInursePath
                + UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT)
                + File.separator;
        path = basicFolder + AppDisk.MYHEAD;
    }

    public void CreateFilede() throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            try {
                // 按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        File dir = new File(path + headName);
        if (!dir.exists()) {
            try {
                // 在指定的文件夹中创建文件
                // dir.createNewFile();
                NetTool.downloadFile1(imageUrl, path, headName);
            } catch (Exception e) {
            }
        }
    }

}
