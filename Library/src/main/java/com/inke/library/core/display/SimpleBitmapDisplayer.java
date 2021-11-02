package com.inke.library.core.display;

import android.graphics.Bitmap;

import com.inke.library.core.LoadedFrom;
import com.inke.library.core.view.ImageAware;

/**
 * 实现简单的Bitmap显示
 */
public class SimpleBitmapDisplayer implements BitmapDisplayer {
    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        imageAware.setImageBitmap(bitmap);
    }
}
