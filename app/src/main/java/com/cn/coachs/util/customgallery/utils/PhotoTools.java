/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.cn.coachs.util.customgallery.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.cn.coachs.util.Logger;
import com.cn.coachs.util.StringUtils;
import com.cn.coachs.util.customgallery.model.BeanPhotoFolderInfo;
import com.cn.coachs.util.customgallery.model.BeanPhotoInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Desction:
 * Author:kuangtiecheng
 * Date:15/10/10 下午4:26
 */
public class PhotoTools {

    /**
     * 获取所有图片
     *
     * @param context
     * @return
     */
    public static List<BeanPhotoFolderInfo> getAllPhotoFolder(Context context) {
        List<BeanPhotoFolderInfo> allFolderList = new ArrayList<BeanPhotoFolderInfo>();
        final String[] projectionPhotos = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Thumbnails.DATA
        };
        final ArrayList<BeanPhotoFolderInfo> allPhotoFolderList = new ArrayList<BeanPhotoFolderInfo>();
        HashMap<Integer, BeanPhotoFolderInfo> bucketMap = new HashMap<Integer, BeanPhotoFolderInfo>();
        Cursor cursor = null;
        //所有图片
        BeanPhotoFolderInfo allPhotoFolderInfo = new BeanPhotoFolderInfo();
        allPhotoFolderInfo.setFolderId(0);
        allPhotoFolderInfo.setFolderName("所有图片");
        allPhotoFolderInfo.setPhotoList(new ArrayList<BeanPhotoInfo>());
        allPhotoFolderList.add(0, allPhotoFolderInfo);
        try {
            cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    , projectionPhotos, "", null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
            if (cursor != null) {
                int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                final int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
                while (cursor.moveToNext()) {
                    int bucketId = cursor.getInt(bucketIdColumn);
                    String bucketName = cursor.getString(bucketNameColumn);
                    final int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    final int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    int thumbImageColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                    final int imageId = cursor.getInt(imageIdColumn);
                    final String path = cursor.getString(dataColumn);
                    final String thumb = cursor.getString(thumbImageColumn);
                    final BeanPhotoInfo photoInfo = new BeanPhotoInfo();
                    photoInfo.setPhotoId(imageId);
                    photoInfo.setPhotoPath(path);
                    photoInfo.setThumbPath(thumb);
                    if (StringUtils.isEmpty(photoInfo.getPhotoPath())) {
                        continue;
                    }
                    if (allPhotoFolderInfo.getCoverPhoto() == null) {
                        allPhotoFolderInfo.setCoverPhoto(photoInfo);
                    }
                    //添加到所有图片
                    allPhotoFolderInfo.getPhotoList().add(photoInfo);

                    //通过bucketId获取文件夹
                    BeanPhotoFolderInfo photoFolderInfo = bucketMap.get(bucketId);
                    if (photoFolderInfo == null) {
                        photoFolderInfo = new BeanPhotoFolderInfo();
                        photoFolderInfo.setPhotoList(new ArrayList<BeanPhotoInfo>());
                        photoFolderInfo.setFolderId(bucketId);
                        photoFolderInfo.setFolderName(bucketName);
                        photoFolderInfo.setCoverPhoto(photoInfo);
                        bucketMap.put(bucketId, photoFolderInfo);
                        allPhotoFolderList.add(photoFolderInfo);
                    }
                    photoFolderInfo.getPhotoList().add(photoInfo);
                }
            }
        } catch (Exception ex) {
            Logger.e(ex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        allFolderList.addAll(allPhotoFolderList);
        return allFolderList;
    }
}
