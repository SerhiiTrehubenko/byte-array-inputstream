package com.trehubenko.bytearray;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class ByteArrayInputStreamTest {

    private final static byte[] BYTES = ("This is true").getBytes();

    InputStream byteArray = new ByteArrayInputStream(BYTES);

    @DisplayName("Test read(), read from beginning")
    @Test
    public void testReadByOneByte() throws IOException {
        assertEquals('T', (char) byteArray.read());
        assertEquals('h', (char) byteArray.read());
        assertEquals('i', (char) byteArray.read());
        assertEquals('s', (char) byteArray.read());
    }

    @DisplayName("Test read(), return -1")
    @Test
    void testReadByOneByteReturnMinusOne() throws IOException {
        byte[] destArray = new byte[BYTES.length];
        int readBytes = byteArray.read(destArray);

        assertEquals(BYTES.length, readBytes);
        assertEquals(-1, byteArray.read());

    }

    @DisplayName("Test read(), RuntimeException is thrown when InputStream is closed")
    @Test
    void testReadByOneByteThrowsRuntimeException() throws IOException {
        byteArray.close();
        assertThrows(RuntimeException.class, () -> byteArray.read(), "InputStream is closed");
    }

    @DisplayName("Test read(b[]), fill total dest Array, dest Array size is less then buffer of ByteArrayInputStream")
    @Test
    public void testReadToWholeDestArraySizeLessThenBuffer() throws IOException {
        byte[] destArray = new byte[4];
        int readBytes = byteArray.read(destArray);

        assertEquals(4, readBytes);
        assertEquals('T', (char) destArray[0]);
        assertEquals('h', (char) destArray[1]);
        assertEquals('i', (char) destArray[2]);
        assertEquals('s', (char) destArray[3]);

    }

    @DisplayName("Test read(b[]), fill total dest Array, dest Array size is bigger then buffer of ByteArrayInputStream")
    @Test
    public void testReadToWholeDestArraySizeBiggerThenBuffer() throws IOException {
        byte[] destArray = new byte[100];
        int readBytes = byteArray.read(destArray);

        assertEquals(BYTES.length, readBytes);
        assertEquals('T', (char) destArray[0]);
        assertEquals('h', (char) destArray[1]);
        assertEquals('i', (char) destArray[2]);
        assertEquals('s', (char) destArray[3]);
        assertEquals(new String(BYTES), new String(destArray, 0, readBytes));

    }

    @DisplayName("Test read(b[]), return -1")
    @Test
    void testReadToWholeDestArrayReturnMinusOne() throws IOException {
        byte[] destArray = new byte[BYTES.length];
        int readBytes = byteArray.read(destArray);

        assertEquals(BYTES.length, readBytes);
        assertEquals(-1, byteArray.read(destArray));

    }

    @DisplayName("Test read(b[]), RuntimeException is thrown when InputStream is closed")
    @Test
    void testReadToWholeDestArrayThrowsRuntimeException() throws IOException {
        byte[] destArray = new byte[BYTES.length];
        byteArray.close();
        assertThrows(RuntimeException.class, () -> byteArray.read(destArray), "InputStream is closed");
    }

    @DisplayName("Test read(byte[] b, int off, int len), RuntimeException is thrown when InputStream is closed")
    @Test
    void testReadWithThreeParametersThrowsRuntimeException() throws IOException {
        byte[] destArray = new byte[BYTES.length];
        byteArray.close();
        assertThrows(RuntimeException.class, () -> byteArray.read(destArray, 0, destArray.length), "InputStream is closed");
    }

    @DisplayName("Test read(byte[] b, int off, int len), IndexOutOfBoundsException is thrown when (off > b.length)")
    @Test
    void testReadWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenOffBiggerDestArraySize() {
        byte[] destArray = new byte[4];
        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.read(destArray, 10, destArray.length));
    }

    @DisplayName("Test read(byte[] b, int off, int len), IndexOutOfBoundsException is thrown when (len > b.length)")
    @Test
    void testReadWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenLenBiggerDestArraySize() {
        byte[] destArray = new byte[4];
        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.read(destArray, 0, 10));
    }

    @DisplayName("Test read(byte[] b, int off, int len), IndexOutOfBoundsException is thrown when ((len + off) > b.length)")
    @Test
    void testReadWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenSumOfLenAndOffBiggerDestArraySize() {
        byte[] destArray = new byte[4];
        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.read(destArray, 3, 2));
    }

    @DisplayName("Test read(byte[] b, int off, int len), IndexOutOfBoundsException is thrown when (off < 0)")
    @Test
    void testReadWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenOffLessNil() {
        byte[] destArray = new byte[4];
        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.read(destArray, -1, 2));
    }

    @DisplayName("Test read(byte[] b, int off, int len), IndexOutOfBoundsException is thrown when (len < 0)")
    @Test
    void testReadWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenLenLessNil() {
        byte[] destArray = new byte[4];
        assertThrows(IndexOutOfBoundsException.class, () -> byteArray.read(destArray, 0, -1));
    }

    @DisplayName("Test read(byte[] b, int off, int len), read with Off")
    @Test
    void testReadWithThreeParametersReadWithOff() throws IOException {
        int count = 0;
        byte[] destArray = new byte[100];

        int readBytes = byteArray.read(destArray, count, 4);
        count += readBytes;

        readBytes = byteArray.read(destArray, count, 4);
        count += readBytes;

        readBytes = byteArray.read(destArray, count, 4);
        count += readBytes;

        assertEquals(12, count);
        assertEquals(new String(BYTES), new String(destArray, 0, count));
    }

    @DisplayName("Test read(byte[] b, int off, int len), returns -1 when there is nothing to read from Buffer")
    @Test
    void testReadWithThreeParametersReturnsMinusOneWhenNothingToRead() throws IOException {
        byte[] destArray = new byte[100];

        int readBytes = byteArray.read(destArray, 0, destArray.length);


        assertEquals(12, readBytes);
        assertEquals(new String(BYTES), new String(destArray, 0, readBytes));

        assertEquals(-1, byteArray.read(destArray, 0, destArray.length));
    }

}