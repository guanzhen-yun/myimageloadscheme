package com.inke.library.core.view;

import android.graphics.Bitmap;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.inke.library.core.ViewScaleType;
import com.inke.library.utils.Constants;

import java.lang.reflect.Field;

/**
 * 实现图像处理和显示的属性和行为
 */
public class ImageViewAware implements ImageAware {

    private ImageView imageView;

    public ImageViewAware(ImageView imageView) {
        if(imageView == null) throw new IllegalArgumentException("ImageView不能为空");
        this.imageView = imageView;
    }

    @Override
    public int getWidth() {
        if(imageView != null) {
            final ViewGroup.LayoutParams params = imageView.getLayoutParams();
            int width = 0;
            if(params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                width = imageView.getWidth();
            }
            if(width <= 0 && params != null) {
                width = params.width;
            }
            if(width <= 0 && imageView != null) {
                width = getImageViewFieldValue(imageView, "mMaxWidth");
            }
            return width;
        }
        return 0;
    }

    private int getImageViewFieldValue(ImageView imageView, String field) {
        Class<? extends ImageView> imageClass = imageView.getClass();
        try {
            Field field1 = imageClass.getField(field);
            return (int)field1.get(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getHeight() {
        if(imageView != null) {
            int height = 0;
            final ViewGroup.LayoutParams params = imageView.getLayoutParams();
            if(params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = imageView.getHeight();
            }
            if(height <= 0 && params != null) {
                height = params.height;
            }
            if(height <= 0 && imageView != null) {
                height = getImageViewFieldValue(imageView, "mMaxHeight");
            }
            return height;
        }
        return 0;
    }

    @Override
    public ViewScaleType getScaleType() {
        if(imageView != null) {
            return ViewScaleType.fromImageView(imageView);
        }
        return ViewScaleType.CROP;
    }

    @Override
    public int getId() {
        return imageView == null ? super.hashCode() : imageView.hashCode();
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        //主线程显示
        if(Looper.myLooper() == Looper.getMainLooper()) { //checkThread
            if(imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        } else {
            Log.e(Constants.LOG_TAG, "不能设置bitmap到ImageView中，必须在UI线程中才能调用");
        }
    }
}
