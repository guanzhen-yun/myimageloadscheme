package com.inke.library.core;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.inke.library.bean.ImageLoading;
import com.inke.library.bean.ImageSize;
import com.inke.library.config.ImageLoaderConfiguration;
import com.inke.library.core.task.DisplayTask;
import com.inke.library.core.view.ImageAware;
import com.inke.library.core.view.ImageViewAware;
import com.inke.library.utils.Constants;

/**
 * 图片加载
 */
public class ImageLoader {

    private volatile static ImageLoader instance;
    private ImageLoaderConfiguration configuration; //图片加载配置
    private ImageLoaderEngine engine; //图片加载引擎

    public static ImageLoader getInstance() {
        if(instance == null) {
            synchronized (ImageLoader.class) {
                if(instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    private ImageLoader() {

    }

    /**
     * 初始化图片加载配置
     * @param configuration 加载配置
     */
    public synchronized void init(ImageLoaderConfiguration configuration) {
        if(configuration == null) {
            throw new IllegalArgumentException("ImageLoader配置为空，不能初始化");
        }
        if(this.configuration == null) {
            Log.e(Constants.LOG_TAG, "ImageLoader配置初始化");
            engine = new ImageLoaderEngine(configuration);
            this.configuration = configuration;
        } else {
            Log.e(Constants.LOG_TAG, "ImageLoader已被初始化");
        }
    }

    /**
     * 显示图片
     *
     * @param loadUrl 图片路径
     * @param imageView 图片控件
     */
    public void displayImage(String loadUrl, ImageView imageView) {
        checkConfiguration();
        if(imageView == null) {
            throw new IllegalArgumentException("ImageView不能为空");
        }
        if(TextUtils.isEmpty(loadUrl)) {
            Log.e(Constants.LOG_TAG, "loadUrl为空");
            return;
        }
        // new实现，持有引用
        ImageAware imageAware = new ImageViewAware(imageView);
        // 初始化图片文件大小
        ImageSize targetSize = initTargetSize(imageAware, configuration.getMaxImageSize());
        // 生成缓存key,如：http://xyz.jpg_480x800
        String cacheKey = loadUrl + "_" + targetSize.getWidth() + "x" + targetSize.getHeight();
        //封装参数对象
        ImageLoading imageLoading = new ImageLoading(loadUrl, imageAware, targetSize, cacheKey);
        //创建显示任务器
        DisplayTask displayTask = new DisplayTask(engine, imageLoading, initHandler());
        //引擎调度显示
        engine.submit(displayTask);
    }

    /**
     * 检查ImageLoader配置初始化
     */
    private void checkConfiguration() {
        if(configuration == null) {
            throw new IllegalStateException("ImageLoader使用前必须初始化配置");
        }
    }

    /**
     * 初始化消息处理（必须在主线程）
     */

    private Handler initHandler() {
        return new Handler(Looper.getMainLooper());
    }

    private ImageSize initTargetSize(ImageAware imageAware, ImageSize maxImageSize) {
        int width = imageAware.getWidth();
        if(width <= 0) width = maxImageSize.getWidth();

        int height = imageAware.getHeight();
        if(height <= 0) height = maxImageSize.getHeight();

        return new ImageSize(width, height);
    }

    /**
     * 清理本地图片缓存
     */
    public void clearDiskCache() {
        checkConfiguration();
        configuration.getDiskCache().clear();
    }

    /**
     * 销毁图片加载器
     */
    public void destroy() {
        if(configuration != null) {
            configuration.getDiskCache().close();
            Log.e(Constants.LOG_TAG,"ImageLoader销毁");
        }
        engine = null;
        configuration = null;
    }
}
