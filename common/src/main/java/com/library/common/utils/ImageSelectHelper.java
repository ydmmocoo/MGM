package com.library.common.utils;

import android.app.Activity;
import android.os.Environment;
import androidx.fragment.app.Fragment;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.List;

public class ImageSelectHelper {

    /**
     * // 例如 LocalMedia 里面返回三种path
     * // 1.media.getPath(); 为原图path
     * // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
     * // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
     * // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
     *
     * @param activity
     */


    /**
     * 拍照获取图片
     *
     * @param activity
     */
    public static void tackPhoto(Activity activity,int aspect_ratio_x, int aspect_ratio_y) {
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())
                .enableCrop(false)// 是否裁剪 true or false
                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)
                .compress(true)// 是否压缩 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    public static void tackPhoto(Fragment fragment, int aspect_ratio_x, int aspect_ratio_y) {
        PictureSelector.create(fragment)
                .openCamera(PictureMimeType.ofImage())
                .enableCrop(false)// 是否裁剪 true or false
                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)
                .compress(true)// 是否压缩 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    /**
     * 单选图片
     *
     * @param activity
     */
    public static void pickPhotoSingle(Activity activity,int aspect_ratio_x, int aspect_ratio_y) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .enableCrop(true)// 是否裁剪 true or false
                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)
                .compress(true)// 是否压缩 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 单选图片
     *
     * @param fragment
     */
    public static void pickPhotoSingle(Fragment fragment,int aspect_ratio_x, int aspect_ratio_y) {
        PictureSelector.create(fragment)
                .openCamera(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .enableCrop(true)// 是否裁剪 true or false
                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)
                .compress(true)// 是否压缩 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 多选图片
     *
     * @param activity
     */
    public static void pickPhotoMul(Activity activity) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.MULTIPLE)
                .maxSelectNum(9)
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 多选图片
     *
     * @param fragment
     */
    public static void pickPhotoMul(Fragment fragment) {
        PictureSelector.create(fragment)
                .openCamera(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.MULTIPLE)
                .maxSelectNum(9)
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 多选图片
     * 已选列表
     *
     * @param activity
     * @param selectList
     */
    public static void pickPhotoMul(Activity activity, List<LocalMedia> selectList) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.MULTIPLE)
                .maxSelectNum(9)
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 多选图片
     * 已选列表
     *
     * @param activity
     * @param selectList
     */
    public static void pickAllTypeMul(Activity activity, List<LocalMedia> selectList) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofAll())
                .selectionMode(PictureConfig.MULTIPLE)
                .maxSelectNum(9)
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
    public static void pickAllTypeMulCompress(Activity activity, List<LocalMedia> selectList) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofAll())
                .selectionMode(PictureConfig.MULTIPLE)
                .maxSelectNum(9)
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .freeStyleCropEnabled(false)
                .withAspectRatio(1,1)
                .compressSavePath(getCompressSavePath())
                .selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
    private static String getCompressSavePath(){
        File dir = new File(Environment.getExternalStorageDirectory(), "MGM_COMPRESS");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }
    /**
     * 拍照获取图片
     *
     * @param activity
     */
    public static void tackAllType(Activity activity,int aspect_ratio_x, int aspect_ratio_y) {
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofAll())
                .enableCrop(true)// 是否裁剪 true or false
                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)
                .compress(true)// 是否压缩 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
}
