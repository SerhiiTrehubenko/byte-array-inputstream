package com.trehubenko.buffered;

import java.io.IOException;
import java.io.InputStream;

public class BufferedInputStream extends InputStream {

    private static final int DEFAULT_CAPACITY = 8 * 1024;
    private final InputStream inputStream;
    private int position;
    private int count;
    private byte[] buffer;

    public BufferedInputStream(InputStream inputStream) {
        this(DEFAULT_CAPACITY, inputStream);
    }

    public BufferedInputStream(int initialCapacity, InputStream inputStream) {
        buffer = new byte[initialCapacity];
        this.inputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        ensureIsOpen();
        if (position >= count) {
            fillBuffer();
            if (position >= count) {
                return -1;
            }
        }
        return buffer[position++];
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        ensureIsOpen();
        if (off > b.length | len > b.length | Math.abs(len + off) > b.length | off < 0 | len < 0) {
            throw new IndexOutOfBoundsException("array length = b[" + b.length + "], off = " + off + ", len = " + len);
        }

        if (off == b.length | len == 0) {
            return 0;
        }

        if (len >= buffer.length && inputStream.available() > 0) {
            return readFromBufferAndInputStream(b, off, len);
        }

        return readFromBuffer(b, off, len);
    }

    @Override
    public void close() throws IOException {
        buffer = null;
        inputStream.close();
    }

    private void ensureIsOpen() throws IOException {
        if (buffer == null) {
            throw new IOException("InputStream is closed, use new one");
        }
    }

    private void fillBuffer() throws IOException {
        position = count = 0;
        int readCount = inputStream.read(buffer, 0, buffer.length);
        count += readCount;
    }

    private int readFromBuffer(byte[] b, int off, int len) throws IOException {
        int remainderBytesToReadInBuffer = count - position;
        int readBytes = 0;

        if (remainderBytesToReadInBuffer > 0 && remainderBytesToReadInBuffer < len) {
            readBytes = readFromBufferAndInputStream(b, off, len);
        } else if (remainderBytesToReadInBuffer == 0) {
            fillBuffer();
            readBytes += readFromBuffer(b, off, len);
        } else {
            if ((count - position) < 0) {
                return -1;
            }
            System.arraycopy(buffer, position, b, off, len);
            position += len;
            readBytes = len;
        }
        return readBytes;
    }

    private int readFromBufferAndInputStream(byte[] b, int off, int len) throws IOException {
        int writtenBytesToDestArray = count - position;
        System.arraycopy(buffer, position, b, off, writtenBytesToDestArray);
        position += writtenBytesToDestArray;
        int resultInputRead = inputStream.read(b, off + writtenBytesToDestArray, len - writtenBytesToDestArray);
        writtenBytesToDestArray += resultInputRead < 0 ? 0 : resultInputRead;

        return writtenBytesToDestArray;
    }
}
