package com.inke.library.cache;

import android.graphics.Bitmap;

import com.inke.library.utils.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 本地缓存接口
 */
public interface DiskCache {
    /**
     * 返回文件的缓存图像
     * @param imageUri 原始图像的URI
     * @return 如果图像不缓存，文件的缓存图像或null
     */
    File get(String imageUri);

    /**
     * 保存在磁盘告诉缓存图像流,
     * 在这个方法中不能关闭传入的图像流。
     *
     * @param imageUri 原始图像的URI
     * @param imageStream 输入流的图像（不应该关闭在这个方法）
     * @param listener listener for saving progress, can be ignored if you don't use
     * @return <b>true</b> - 如果位图已成功保存；<b>false</b> - 如果位图没有保存在磁盘高速缓存
     * @throws IOException IOException
     */
    boolean save(String imageUri, InputStream imageStream, IOUtils.CopyListener listener) throws IOException;

    /**
     * Saves image bitmap in disk cache.
     *
     * @param imageUri 原始图像的URI
     * @param bitmap 位图
     * @throws IOException IOException
     */
    void save(String imageUri, Bitmap bitmap) throws IOException;

    /**
     * 关闭磁盘缓存，释放资源
     */
    void close();

    /**
     * 清理磁盘高速缓存.
     */
    void clear();
}
