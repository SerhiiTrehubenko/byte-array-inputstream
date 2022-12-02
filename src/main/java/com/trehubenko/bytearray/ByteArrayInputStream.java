package com.trehubenko.bytearray;

import java.io.InputStream;

public class ByteArrayInputStream extends InputStream {

    private byte[] buffer;

    private int position;

    public ByteArrayInputStream(byte[] b) {
        buffer = b;
    }

    public ByteArrayInputStream(byte[] b, int off, int len) {
        buffer = new byte[len];
        System.arraycopy(b, off, this.buffer, 0, len);
    }

    @Override
    public int read() {
        ensureIsOpen();
        if (position == buffer.length) {
            return -1;
        }
        return buffer[position++];
    }

    @Override
    public int read(byte[] b) {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) {
        ensureIsOpen();
        if (off > b.length | len > b.length | Math.abs(len + off) > b.length | off < 0 | len < 0) {
            throw new IndexOutOfBoundsException("array length = b[" + b.length + "]" + ", off = " + off + ", len = " + len);
        }

        int readBytes = buffer.length - position;

        if (readBytes == 0) {
            return -1;
        } else if (len > readBytes) {
            System.arraycopy(buffer, position, b, off, readBytes);
            position += readBytes;
        } else {
            System.arraycopy(buffer, position, b, off, len);
            position += readBytes = len;
        }

        return readBytes;
    }

    @Override
    public void close() {
        buffer = null;
    }

    private void ensureIsOpen() {
        if (buffer == null) {
            throw new RuntimeException("InputStream is closed");
        }
    }
}
