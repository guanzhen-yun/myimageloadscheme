package com.inke.library.core.display;

import android.graphics.Bitmap;

import com.inke.library.core.LoadedFrom;
import com.inke.library.core.view.ImageAware;

/**
 * Bitmap显示接口
 */
public interface BitmapDisplayer {

   void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom);

}
