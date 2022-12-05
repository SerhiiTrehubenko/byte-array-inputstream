package com.trehubenko.bytearray;

import com.trehubenko.AbstractInputStreamTest;

class ByteArrayInputStreamTest extends AbstractInputStreamTest {

    public ByteArrayInputStreamTest() {
        inputStream = new ByteArrayInputStream(BYTES);
    }
}