package com.fjx.mg.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCache {

    private static FileCache insctance;

    private FileCache() {
        super();

    }

    public static FileCache getInstance() {

        if (insctance == null) {
            synchronized (FileCache.class) {
                if (insctance == null) {
                    synchronized (FileCache.class) {
                        insctance = new FileCache();
                    }
                }
            }
        }
        return insctance;
    }

    /**
     * 根据具体存储路径获得本地图片
     *
     */

    /**
     * 把位图保存到文件中
     */
    public File saveBitmapToFile(String name, Bitmap bitmap) {
        Bitmap bitmap2 = compressImage_new(bitmap);

        FileCache fileCache = FileCache.getInstance();
        File file = new File(fileCache.getSDPath() + name);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            if (bitmap2 != null) {

                bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    private Bitmap compressImage_new(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (image == null) {
            return null;
        } else {
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 80;
            while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();// 重置baos即清空baos
                options -= 5;// 每次都减少10
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
            return bitmap;
        }
    }

    /**
     * 获得图片的缩放比例
     */
    private int getSampleSize(String path) {

        Options bitmapFactoryOptions = getOptions(true);
        BitmapFactory.decodeFile(path, bitmapFactoryOptions);
        int inSampleSize = computeSampleSize(bitmapFactoryOptions, -1, 480 * 800);
        return inSampleSize;
    }

    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128
                : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 获得位图工厂options
     *
     * @param inJustDecodeBounds 是否只获取位图大小
     */
    private Options getOptions(boolean inJustDecodeBounds) {

        Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inInputShareable = true;
        bitmapFactoryOptions.inPurgeable = true;
        bitmapFactoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmapFactoryOptions.inJustDecodeBounds = inJustDecodeBounds;

        return bitmapFactoryOptions;
    }

    /**
     * 获得Bitmap
     */
    private Bitmap getBitmap(String path) {

        Options bitmapFactoryOptions = getOptions(false);
        bitmapFactoryOptions.inSampleSize = getSampleSize(path);
        FileInputStream localFileInputStream;
        Bitmap bitmap = null;
        try {
            localFileInputStream = new FileInputStream(new File(path));
            bitmap = BitmapFactory.decodeStream(localFileInputStream, null, bitmapFactoryOptions);
            localFileInputStream.close();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return bitmap;
    }

    private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10; // ����ռ��С


    public void saveText(String name, String data, Context context) {

        // 判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            // SD空间不足
            return;
        }
        File dir = new File(Environment.getExternalStorageDirectory(), "MG");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, name + ".txt");
        if (!file.exists()) {

        } else {
            file.delete();
        }
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(data.getBytes());
            outStream.flush();
            outStream.close();

        } catch (FileNotFoundException e) {
            Log.w("ImageFileCache", "FileNotFoundException");
        } catch (IOException e) {
            Log.w("ImageFileCache", "IOException");
        }

    }

    /**
     * 保存图片到相册
     */
    public void saveToAlbum(Bitmap bitmap, String name, Context context) {

        if (bitmap == null) {
            return;
        }
        // 判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            // SD空间不足
            return;
        }
        File dir = new File(Environment.getExternalStorageDirectory(), "MG");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, name + ".jpg");
        if (!file.exists()) {

        } else {
            file.delete();
        }
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (FileNotFoundException e) {
            Log.w("ImageFileCache", "FileNotFoundException");
        } catch (IOException e) {
            Log.w("ImageFileCache", "IOException");
        }

        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), name, null);
            context.sendBroadcast(
                    new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

    }

    /**
     * 保存图片到相册
     */
    public File saveF(Bitmap bitmap, String name, Context context) {

        if (bitmap == null) {
            return new File("");
        }
        // 判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            // SD空间不足
            return new File("");
        }
        File dir = new File(Environment.getExternalStorageDirectory(), "MG");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, name);
        if (!file.exists()) {

        } else {
            file.delete();
        }
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (FileNotFoundException e) {
            Log.w("ImageFileCache", "FileNotFoundException");
        } catch (IOException e) {
            Log.w("ImageFileCache", "IOException");
        }

        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), name, null);
            context.sendBroadcast(
                    new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        return file;
    }

    /**
     * 修改文件的最后修改时间 这里需要考虑,是否将使用的图片日期改为当前日期
     */
    private void updateFileTime(String path) {
        File file = new File(path);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }

    private int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        /**
         * 计算sdcard上的剩余空间
         */
        int MB = 1024 * 1024;
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        return (int) sdFreeMB;
    }

    /**
     * 取SD卡路径
     */
    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    }
}
