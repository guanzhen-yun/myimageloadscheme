package com.inke.library.core.filename;

import android.util.Log;

import com.inke.library.utils.Constants;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 图片文件名的Md5哈希图片URI
 */
public class Md5FileNameGenerator implements FileNameGenerator {

    private static final String HASH_ALGORITHM = "MD5";
    private static final int RADIX = 10 + 26; // 10位数字 + 26个字母

    @Override
    public String generate(String imageUri) {
        byte[] md5 = getMD5(imageUri.getBytes());
        BigInteger bi = new BigInteger(md5).abs();
        return bi.toString(RADIX);
    }

    private byte[] getMD5(byte[] data) {
        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(data);
            hash = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            Log.e(Constants.LOG_TAG, e.getMessage());
        }
        return hash;
    }
}
