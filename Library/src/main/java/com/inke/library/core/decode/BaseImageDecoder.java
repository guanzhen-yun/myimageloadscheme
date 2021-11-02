package com.inke.library.core.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.util.Log;

import com.inke.library.bean.ImageDecoding;
import com.inke.library.bean.ImageSize;
import com.inke.library.utils.Constants;
import com.inke.library.utils.IOUtils;
import com.inke.library.utils.ImageSizeUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 解码图像
 */
public class BaseImageDecoder implements ImageDecoder {

    @Override
    public Bitmap decode(ImageDecoding imageDecoding) throws IOException {
        Bitmap decodedBitmap;//解码bitmap
        ImageSize imageSize; //图像大小对象
        //获取downloader下载器的输入流
        InputStream imageStream = getImageStream(imageDecoding);
        if (imageStream == null) {
            Log.e(Constants.LOG_TAG, "该图片无流: " + imageDecoding.getCacheKey());
            return null;
        }
        try {
            //通过图片流获取图像大小对象
            imageSize = getImageSizeByStream(imageStream);
            //获取图像大小对象后，重置输入流
            imageStream = resetStream(imageStream, imageDecoding);
            //准备解码配置
            Options decodingOptions = prepareDecodingOptions(imageSize, imageDecoding);
            //解码Bitmap并赋值
            decodedBitmap = BitmapFactory.decodeStream(imageStream, null, decodingOptions);
        } finally {
            // 静默关闭
            IOUtils.closeSilently(imageStream);
        }

        if (decodedBitmap == null) {
            Log.e(Constants.LOG_TAG, "该图片无法解码" + imageDecoding.getCacheKey());
        } else {
            //解码完成后创建缩略图
            decodedBitmap = createBitmap(decodedBitmap, imageDecoding);
        }
        return decodedBitmap;
    }

    /**
     * 获取downloader下载器的输入流
     *
     * @param imageDecoding 图片解码对象
     * @return InputStream
     * @throws IOException IOException
     */
    private InputStream getImageStream(ImageDecoding imageDecoding) throws IOException {
        return imageDecoding.getDownloader().getStream(imageDecoding.getLoadUrl());
    }

    /**
     * 通过输入流获取图像大小对象
     *
     * @param imageStream 图片流
     * @return ImageSize对象
     */
    private ImageSize getImageSizeByStream(InputStream imageStream) {
        Options options = new Options();
        //当指定inJustDecodeBounds时候，只解析图片的长度和宽度，不载入图片
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, null, options);

        return new ImageSize(options.outWidth, options.outHeight);
    }

    /**
     * 准备解码配置
     *
     * @param imageSize     图片大小对象
     * @param imageDecoding 图片解码对象
     */
    private Options prepareDecodingOptions(ImageSize imageSize, ImageDecoding imageDecoding) {
        ImageSize targetSize = imageDecoding.getTargetSize();
        int scale = ImageSizeUtils.calculateImageSampleSize(imageSize, targetSize, imageDecoding.getViewScaleType());
        //解析bitmap减少内存占用，对原图降采样，防止OOM(如：scale = 2,解析后的图片为原图1/4大小)
        Options decodingOptions = imageDecoding.getDecodingOptions();
        //当指定inSampleSize的时候，会根据inSampleSize载入一个缩略图
        decodingOptions.inSampleSize = scale;
        return decodingOptions;
    }

    /**
     * 在该输入流中mark当前位置。后续调用reset方法重新将流定位于最后标记位置，以便后续读取能重新读取相同字节
     *
     * @param imageStream   图片流
     * @param imageDecoding 图片解码对象
     * @return 输入流
     * @throws IOException IOException
     */
    private InputStream resetStream(InputStream imageStream, ImageDecoding imageDecoding) throws IOException {
        if (imageStream.markSupported()) {
            try {
                imageStream.reset();
                return imageStream;
            } catch (IOException ignored) {
            }
        }
        //静默关闭
        IOUtils.closeSilently(imageStream);
        return getImageStream(imageDecoding);
    }

    private Bitmap createBitmap(Bitmap subsampleBitmap, ImageDecoding imageDecoding) {
        //Bitmap对象进行处理，包括：缩放、旋转、位移、倾斜等
        Matrix m = new Matrix();
        Bitmap bitmap = Bitmap.createBitmap(subsampleBitmap, 0, 0, imageDecoding.getTargetSize().getWidth(), imageDecoding.getTargetSize().getHeight(), m, true);
        return bitmap;
    }
}
