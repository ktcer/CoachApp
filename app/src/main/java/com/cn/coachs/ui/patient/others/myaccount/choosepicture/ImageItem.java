package com.cn.coachs.ui.patient.others.myaccount.choosepicture;

import java.io.Serializable;

/**
 * 一个图片对象
 *
 * @author kuangtiecheng
 */
public class ImageItem implements Serializable {
    public String imageId;
    public String thumbnailPath;
    public String imagePath;
    public boolean isSelected = false;
}
