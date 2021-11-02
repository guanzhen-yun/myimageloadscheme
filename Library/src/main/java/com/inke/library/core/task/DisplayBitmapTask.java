package com.inke.library.core.task;

import android.graphics.Bitmap;

import com.inke.library.core.LoadedFrom;
import com.inke.library.core.display.BitmapDisplayer;
import com.inke.library.core.view.ImageAware;

public class DisplayBitmapTask implements Runnable {

    private final Bitmap mBitmap;
    private final ImageAware mImageAware;
    private final LoadedFrom mLoadedFrom;
    private final BitmapDisplayer mDisplayer;

    public DisplayBitmapTask(Bitmap bitmap, ImageAware imageAware, BitmapDisplayer displayer, LoadedFrom loadedFrom) {
        mBitmap = bitmap;
        mImageAware = imageAware;
        mLoadedFrom = loadedFrom;
        mDisplayer = displayer;
    }

    @Override
    public void run() {
        mDisplayer.display(mBitmap, mImageAware, mLoadedFrom);
    }
}
