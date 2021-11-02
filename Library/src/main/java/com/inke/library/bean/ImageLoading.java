package com.inke.library.bean;

import com.inke.library.core.view.ImageAware;

/**
 * 图片加载对象
 */
public final class ImageLoading {

    //图片路径
    private String loadUrl;
    //图片缓存key,如：http://xyz.jpg_480x800
    private String cacheKey;
    // 图片控件
    private ImageAware imageAware;
    //图片大小
    private ImageSize targetSize;

    public ImageLoading(String loadUrl, ImageAware imageAware, ImageSize targetSize, String cacheKey) {
        this.loadUrl = loadUrl;
        this.imageAware = imageAware;
        this.targetSize = targetSize;
        this.cacheKey = cacheKey;
    }

    public String getLoadUrl() {
        return loadUrl;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public ImageAware getImageAware() {
        return imageAware;
    }

    public ImageSize getTargetSize() {
        return targetSize;
    }
}
