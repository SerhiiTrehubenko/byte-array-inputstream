package com.trehubenko.bytearray;

import java.io.IOException;
import java.io.OutputStream;

public class ByteArrayOutputStream extends OutputStream {
    private final static int DEFAULT_CAPACITY = 8 * 1024;
    private final static double GROW_FACTOR = 1.5;
    private byte[] buffer;
    private int position;

    public ByteArrayOutputStream() {
        this(DEFAULT_CAPACITY);
    }

    public ByteArrayOutputStream(int customCapacity) {
        if (customCapacity < 0) {
            throw new RuntimeException("Capacity can not be less than 0");
        }
        this.buffer = new byte[customCapacity];
    }

    @Override
    public void write(int b) throws IOException {
        ensureIsOpen();

        if (position == buffer.length) {
            grow();
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
            throw new IndexOutOfBoundsException("array length = b[" + b.length + "], off = " + off + ", len = " + len);
        }
        int remainderOfBuffer = buffer.length - position;

        if (len > remainderOfBuffer) {
            grow();
            write(b, off, len);
        } else {
            System.arraycopy(b, off, buffer, position, len);
            position += len;
        }
    }

    @Override
    public void close() {
        this.buffer = null;
    }

    private void grow() {
        byte[] newBuffer = new byte[(int) (buffer.length * GROW_FACTOR) + 1];
        if (position > 0) {
            System.arraycopy(buffer, 0, newBuffer, 0, position);
        }
        buffer = newBuffer;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(buffer, 0, position);
        position = 0;
        outputStream.flush();
        outputStream.close();
    }

    public byte[] toByteArray() {
        byte[] arrayToReturn = new byte[position];
        System.arraycopy(buffer, 0, arrayToReturn, 0, position);
        return arrayToReturn;
    }

    private void ensureIsOpen() throws IOException {
        if (buffer == null) {
            throw new IOException("InputStream is closed");
        }
    }

    int getBufferSize() {
        return buffer.length;
    }
}
