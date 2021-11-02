package com.inke.library.core.decode;

import android.graphics.Bitmap;

import com.inke.library.bean.ImageDecoding;

import java.io.IOException;

/**
 * 图片解码接口
 */
public interface ImageDecoder {

    Bitmap decode(ImageDecoding imageDecoding) throws IOException;

}
