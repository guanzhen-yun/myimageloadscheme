package com.inke.library.core.view;

import android.graphics.Bitmap;

import com.inke.library.core.ViewScaleType;

/**
 * 提供图像处理和显示的属性和行为
 */
public interface ImageAware {

    int getWidth();

    int getHeight();

    ViewScaleType getScaleType();

    int getId();

    void setImageBitmap(Bitmap bitmap);
}
