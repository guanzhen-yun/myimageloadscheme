package com.inke.library.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * I/O操作
 */
public class IOUtils {

    private static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 KB
    private static final int DEFAULT_IMAGE_TOTAL_SIZE = 500 * 1024; // 500 Kb
    private static final int CONTINUE_LOADING_PERCENTAGE = 75;

    private IOUtils() {
        throw new UnsupportedOperationException("IOUtils不能被构造方法初始化");
    }
    
    //复制流是否成功
    public static boolean copyStream(InputStream is, OutputStream os, CopyListener listener, int bufferSize) throws IOException {
        int current = 0;
        int total = is.available();
        if(total <= 0) {
            total = DEFAULT_IMAGE_TOTAL_SIZE;
        }
        
        final byte[] bytes = new byte[bufferSize];
        int count;
//        if(shouldStopLoading(listener, current, total)) return false;
        while ((count = is.read(bytes, 0, bufferSize)) != -1) {
            os.write(bytes, 0, count);
            current += count;
//            if(shouldStopLoading(listener, current, total)) return false;
        }
        os.flush();
        return true;
    }

    //是否应该停止加载
    private static boolean shouldStopLoading(CopyListener listener, int current, int total) {
        if(listener != null) {
            boolean shouldContinue = listener.onBytesCopied(current, total);
            if(!shouldContinue) {
                return 100 * current / total < CONTINUE_LOADING_PERCENTAGE;
            }
        }
        return false;
    }

    //读完流再静默关闭流
    public static void readAndCloseStream(InputStream is) {
        final byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
        try {
            while (is.read(bytes, 0, DEFAULT_BUFFER_SIZE) != -1) ;
        } catch (IOException ignored) {

        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeSilently(InputStream in) {
        try {
            if (in != null)
                in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeSilently(OutputStream os) {
        try {
            if (os != null)
                os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface CopyListener {

        boolean onBytesCopied(int current, int total);
    }
}
