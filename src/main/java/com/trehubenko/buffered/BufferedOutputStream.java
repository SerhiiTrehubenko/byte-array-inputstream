package com.trehubenko.buffered;

import java.io.IOException;
import java.io.OutputStream;

public class BufferedOutputStream extends OutputStream {

    private static final int DEFAULT_CAPACITY = 8 * 1024;
    private final OutputStream outputStream;
    private byte[] buffer;
    private int position;

    public BufferedOutputStream(OutputStream outputStream) {
        this(DEFAULT_CAPACITY, outputStream);
    }

    public BufferedOutputStream(int initialCapacity, OutputStream outputStream) {
        this.buffer = new byte[initialCapacity];
        this.outputStream = outputStream;
    }

    @Override
    public void write(int b) throws IOException {
        ensureIsOpen();
        if (position == buffer.length) {
            flush();
        }
        buffer[position++] = (byte) b;
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        ensureIsOpen();
        if (off > b.length | len > b.length | Math.abs(len + off) > b.length | off < 0 | len <= 0) {
            throw new IndexOutOfBoundsException("array length = b[" + b.length + "]" + ", off = " + off + ", len = " + len);
        }

        if (len > (buffer.length - position)) {
            flush();
            outputStream.write(b, off, len);
        } else {
            System.arraycopy(b, off, buffer, position, len);
            position += len;
        }
    }

    @Override
    public void flush() throws IOException {
        ensureIsOpen();
        if (position != 0) {
            outputStream.write(buffer, 0, position);
        }
        position = 0;
    }

    @Override
    public void close() throws IOException {
        if (position != 0) {
            flush();
        }
        buffer = null;
        outputStream.close();
    }

    private void ensureIsOpen() {
        if (buffer == null) {
            throw new RuntimeException("InputStream is closed");
        }
    }
}
