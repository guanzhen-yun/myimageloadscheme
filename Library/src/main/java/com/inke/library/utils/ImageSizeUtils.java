package com.inke.library.utils;

import android.opengl.GLES10;

import com.inke.library.bean.ImageSize;
import com.inke.library.core.ViewScaleType;

import javax.microedition.khronos.opengles.GL10;

/**
 * 用于计算与图像大小、尺寸
 */
public class ImageSizeUtils {

    //默认Bitmap最大尺寸
    private static final int DEFAULT_MAX_BITMAP_DIMENSION = 2048;
    //图片最大尺寸
    private static ImageSize maxBitmapSize;

    //在开始硬件加速情况下，超大图无法正常显示（图片长宽有一个大于9000）
    //而且程序不会crash,只是图片加载不出来，View显示为黑色
    static {
        int[] maxTextureSize = new int[1];
        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);
        int maxBitmapDimension = Math.max(maxTextureSize[0], DEFAULT_MAX_BITMAP_DIMENSION);
        maxBitmapSize = new ImageSize(maxBitmapDimension, maxBitmapDimension);
    }

    private ImageSizeUtils() {
        throw new UnsupportedOperationException("ImageSizeUtils不能被构造方法初始化");
    }

    /**
     * 根据bitmap的长宽和目标缩略图的长和宽，计算出inSampleSize的大小
     */
    public static int calculateImageSampleSize(ImageSize srcSize, ImageSize targetSize, ViewScaleType viewScaleType) {
        //inSampleSize 越大 读出的图片尺寸越小
        //crop 按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
        //center 按图片的原来size居中显示，当图片长/宽超过View的长/宽，则截取图片的居中部分显示
        //inside 将图片的内容完整居中显示，通过按比例缩小或原来的size使得图片长/宽等于或小于View的长/宽

        final int srcWidth = srcSize.getWidth();
        final int srcHeight = srcSize.getHeight();
        final int targetWidth = targetSize.getWidth();
        final int targetHeight = targetSize.getHeight();

        int scale = 1;
        switch (viewScaleType) {
            case CROP:
                scale = 1;
                break;
            default:
                if (srcWidth > targetWidth || srcHeight > targetHeight) {
                    int widthRatio = Math.round((float) srcWidth / (float) targetWidth);
                    int heightRatio = Math.round((float) srcHeight / (float) (targetHeight));
                    scale = widthRatio > heightRatio ? widthRatio : heightRatio;
                }
                break;
        }
        return scale;
    }
}
