package com.trehubenko.bytearray;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ByteArrayOutputStreamTest {
    private final static byte[] BYTES = ("This is true").getBytes();
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    @DisplayName("Test write(), IOException thrown when ByteArrayOutputStream is closed")
    @Test
    void testWriteByOneByteThrowsIOException() {

        byteArrayOutputStream.close();

        assertThrows(IOException.class, () -> byteArrayOutputStream.write(10), "InputStream is closed");

    }

    @DisplayName("Test write(), ByteArrayOutputStream writes to its buffer")
    @Test
    void testWriteByOneByte() throws IOException {

        byteArrayOutputStream.write(BYTES[0]);
        byteArrayOutputStream.write(BYTES[1]);
        byteArrayOutputStream.write(BYTES[2]);

        byte[] resultArray = byteArrayOutputStream.toByteArray();

        assertEquals(3, resultArray.length);
        assertEquals(BYTES[0], resultArray[0]);
        assertEquals(BYTES[1], resultArray[1]);
        assertEquals(BYTES[2], resultArray[2]);
    }

    @DisplayName("Test write(), ByteArrayOutputStream increases buffer size when buffer is full")
    @Test
    void testWriteByOneByteIncreaseBuffer() throws IOException {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(2)) {

            byteArrayOutputStream.write(BYTES[0]);
            byteArrayOutputStream.write(BYTES[1]);

            assertEquals(2, byteArrayOutputStream.getBufferSize());

            byteArrayOutputStream.write(BYTES[2]);
            byteArrayOutputStream.write(BYTES[3]);

            assertEquals(4, byteArrayOutputStream.getBufferSize());
        }
    }

    @DisplayName("Test write(b[]), IOException thrown when ByteArrayOutputStream is closed")
    @Test
    void testWriteWholeSrcArrayThrowsIOException() {

        byteArrayOutputStream.close();

        assertThrows(IOException.class, () -> byteArrayOutputStream.write(BYTES), "InputStream is closed");

    }

    @DisplayName("Test write(b[]), ByteArrayOutputStream writes whole Src Array")
    @Test
    void testWriteWholeSrcArray() throws IOException {

        byteArrayOutputStream.write(BYTES);

        byte[] resultArray = byteArrayOutputStream.toByteArray();

        assertEquals(12, resultArray.length);
        assertEquals(BYTES[0], resultArray[0]);
        assertEquals(BYTES[1], resultArray[1]);
        assertEquals(BYTES[2], resultArray[2]);


    }

    @DisplayName("Test write(b[]), ByteArrayOutputStream writes whole Src Array and increase Buffer size when Buffer " +
            "size is less then Src Array size")
    @Test
    void testWriteWholeSrcArrayIncreaseBuffer() throws IOException {
        try (var byteArrayOutputStream = new ByteArrayOutputStream(2)) {

            byteArrayOutputStream.write(BYTES);

            byte[] resultArray = byteArrayOutputStream.toByteArray();

            assertEquals(12, resultArray.length);
            assertEquals(17, byteArrayOutputStream.getBufferSize());
            assertEquals(BYTES[0], resultArray[0]);
            assertEquals(BYTES[6], resultArray[6]);
            assertEquals(BYTES[11], resultArray[11]);
        }
    }

    @DisplayName("Test write(byte[], off, len), IOException thrown when ByteArrayOutputStream is closed")
    @Test
    void testWriteWithThreeParametersTrowsIOException() {

        byteArrayOutputStream.close();

        assertThrows(IOException.class, () -> byteArrayOutputStream.write(BYTES, 0, BYTES.length), "InputStream is closed");

    }

    @DisplayName("Test write(byte[], off, len), IndexOutOfBoundsException is thrown when (off > b.length)")
    @Test
    void testWriteWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenOffBiggerDestArraySize() {

        assertThrows(IndexOutOfBoundsException.class, () -> byteArrayOutputStream.write(BYTES, 13, 1));


    }

    @DisplayName("Test write(byte[], off, len), IndexOutOfBoundsException is thrown when (len > b.length)")
    @Test
    void testWriteWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenLenBiggerDestArraySize() {

        assertThrows(IndexOutOfBoundsException.class, () -> byteArrayOutputStream.write(BYTES, 0, 13));

    }

    @DisplayName("Test write(byte[], off, len), IndexOutOfBoundsException is thrown when ((len + off) > b.length)")
    @Test
    void testWriteWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenSumOfLenAndOffBiggerDestArraySize() {

        assertThrows(IndexOutOfBoundsException.class, () -> byteArrayOutputStream.write(BYTES, 7, 7));

    }

    @DisplayName("Test write(byte[], off, len), IndexOutOfBoundsException is thrown when (off < 0)")
    @Test
    void testWriteWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenOffLessNil() {

        assertThrows(IndexOutOfBoundsException.class, () -> byteArrayOutputStream.write(BYTES, -1, 2));

    }

    @DisplayName("Test write(byte[], off, len), IndexOutOfBoundsException is thrown when (len < 0)")
    @Test
    void testWriteWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenLenLessNil() {

        assertThrows(IndexOutOfBoundsException.class, () -> byteArrayOutputStream.write(BYTES, 0, -1));

    }

    @DisplayName("Test write(byte[], off, len), IndexOutOfBoundsException is thrown when (len == 0)")
    @Test
    void testWriteWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenLenEqualsNil() {

        assertThrows(IndexOutOfBoundsException.class, () -> byteArrayOutputStream.write(BYTES, 0, 0));

    }

    @DisplayName("Test write(byte[], off, len), ByteArrayOutputStream writes Src Array")
    @Test
    void testWriteWithThreeParameters() throws IOException {

        byteArrayOutputStream.write(BYTES, 0, 6);

        byte[] resultArray = byteArrayOutputStream.toByteArray();

        assertEquals(6, resultArray.length);
        assertEquals(BYTES[0], resultArray[0]);
        assertEquals(BYTES[5], resultArray[5]);

    }

    @DisplayName("Test write(byte[], off, len), ByteArrayOutputStream increases Buffer size when (len) bytes to write" +
            "bigger then Buffer")
    @Test
    void testWriteWithThreeParametersIncreaseBuffer() throws IOException {
        try (var byteArrayOutputStream = new ByteArrayOutputStream(2)) {
            byteArrayOutputStream.write(BYTES, 0, 6);

            byte[] resultArray = byteArrayOutputStream.toByteArray();

            assertEquals(7, byteArrayOutputStream.getBufferSize());
            assertEquals(6, resultArray.length);
            assertEquals(BYTES[0], resultArray[0]);
            assertEquals(BYTES[5], resultArray[5]);
        }
    }

    @DisplayName("Test write(byte[], off, len), write with different (off)")
    @Test
    void testWriteWithThreeParametersWriteWithDifferentOff() throws IOException {
        byteArrayOutputStream.write(BYTES, 0 , 3);
        byteArrayOutputStream.write(BYTES, 3 , 4);
        byteArrayOutputStream.write(BYTES, 7 , BYTES.length - 7);

        byte[] resultArray = byteArrayOutputStream.toByteArray();

        assertEquals(new String(BYTES, 0, BYTES.length), new String(resultArray, 0, resultArray.length));
    }

    @Test
    void testWriteTo() throws IOException {
        String fileName = "text2.txt";
        byteArrayOutputStream.write(BYTES);

        byteArrayOutputStream.writeTo(new FileOutputStream(fileName));

        File file = new File(fileName);

        assertEquals(12, file.length());
        file.delete();
    }

}