package com.inke.library.core.downloader;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;

/**
 * 提供检索，实现必须是线程安全的
 */
public interface ImageDownloader {

    /**
     * 图片的URI检索
     * @param imageUri 图片的URI
     */
    InputStream getStream(String imageUri) throws IOException;

    /**
     * 代表支持的schemes(协议)的URI.提供方便的方法来处理schemes和URIs
     */
    enum Scheme { // [ski:m]
        HTTP("http"), HTTPS("https"), FILE("file"), UNKNOWN("");

        private String scheme;
        private String uriPrefix;

        Scheme(String scheme) {
            this.scheme = scheme;
            uriPrefix = scheme + "://";
        }

        public static Scheme ofUri(String imageUri) {
            if(imageUri.startsWith("https")) {
                return HTTPS;
            } else if(imageUri.startsWith("http")) {
                return HTTP;
            } else if(imageUri.startsWith("file")) {
                return FILE;
            } else {
                return UNKNOWN;
            }
        }

        public String wrap(String absolutePath) {
            return Uri.parse(uriPrefix + absolutePath).toString();
        }

        public String crop(String imageUri) {
            return imageUri.replace(uriPrefix, "");
        }
    }
}
