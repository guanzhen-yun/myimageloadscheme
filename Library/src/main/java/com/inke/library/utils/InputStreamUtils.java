package com.inke.library.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;

/**
 * InputStream处理器
 */
public class InputStreamUtils extends InputStream {

    private final InputStream stream;
    private final int length;

    public InputStreamUtils(InputStream stream, int length) {
        this.stream = stream;
        this.length = length;
    }

    @Override
    public int available() throws IOException {
        return length;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        stream.mark(readlimit);
    }

    @Override
    public int read() throws IOException {
        return stream.read();
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        return stream.read(buffer);
    }

    @Override
    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        return stream.read(buffer, byteOffset, byteCount);
    }

    @Override
    public void reset() throws IOException {
        stream.reset();
    }

    @Override
    public long skip(long byteCount) throws IOException {
        return stream.skip(byteCount);
    }

    @Override
    public boolean markSupported() {
        return stream.markSupported();
    }
}
