package com.inke.library.core;

import android.widget.ImageView;

public enum  ViewScaleType {
    CROP;

    public static ViewScaleType fromImageView(ImageView imageView) {
        ImageView.ScaleType scaleType = imageView.getScaleType();
        if(scaleType == ImageView.ScaleType.CENTER_CROP) {
            return CROP;
        }
        return CROP;
    }
}
