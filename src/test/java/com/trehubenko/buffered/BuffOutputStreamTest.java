package com.trehubenko.buffered;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BuffOutputStreamTest {

    private final static byte[] BYTES = ("This is true").getBytes();

    private final static String FILE_NAME = "text.txt";


    @AfterEach
    public void removeFile() {
        new File(FILE_NAME).delete();
    }

    @DisplayName("Test write(), RuntimeException thrown when BufferedOutputStream is closed")
    @Test
    void testWriteByOneByteThrowsRuntimeException() {
        try (var bufferedOutputStream = new BufferedOutputStream(3, new FileOutputStream(FILE_NAME))) {

            bufferedOutputStream.close();

            assertThrows(RuntimeException.class, () -> bufferedOutputStream.write(10), "InputStream is closed");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test write(), BufferedOutputStream will not flush until buffer is not (size + 1) ")
    @Test
    void testWriteByOneByte() {
        try (var bufferedOutputStream = new BufferedOutputStream(3, new FileOutputStream(FILE_NAME))) {

            bufferedOutputStream.write(BYTES[0]);
            bufferedOutputStream.write(BYTES[1]);
            bufferedOutputStream.write(BYTES[2]);

            assertEquals(0, new File(FILE_NAME).length());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test write(), BufferedOutputStream flushes when write until buffer is (size + 1) ")
    @Test
    void testWriteByOneByteFlushes() {
        try (var bufferedOutputStream = new BufferedOutputStream(3, new FileOutputStream(FILE_NAME))) {

            bufferedOutputStream.write(BYTES[0]);
            bufferedOutputStream.write(BYTES[1]);
            bufferedOutputStream.write(BYTES[2]);
            bufferedOutputStream.write(BYTES[3]);

            assertEquals(3, new File(FILE_NAME).length());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test write(b[]), RuntimeException thrown when BufferedOutputStream is closed")
    @Test
    void testWriteWholeSrcArrayThrowsRuntimeException() {
        try (var bufferedOutputStream = new BufferedOutputStream(3, new FileOutputStream(FILE_NAME))) {

            bufferedOutputStream.close();

            assertThrows(RuntimeException.class, () -> bufferedOutputStream.write(BYTES), "InputStream is closed");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test write(b[]), BufferedOutputStream does not flush until buffer is not full")
    @Test
    void testWriteWholeSrcArrayDoNotFlushWhenBufferIsNotFull() {
        try (var bufferedOutputStream = new BufferedOutputStream(100, new FileOutputStream(FILE_NAME))) {

            bufferedOutputStream.write(BYTES);

            assertEquals(0, new File(FILE_NAME).length());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test write(b[]), BufferedOutputStream write to a file whole a Src Array when a Src Array length bigger then Buffer size")
    @Test
    void testWriteWholeSrcArrayFlushWhenBufferIsFull() {
        try (var bufferedOutputStream = new BufferedOutputStream(8, new FileOutputStream(FILE_NAME))) {

            bufferedOutputStream.write(BYTES);

            assertEquals(12, new File(FILE_NAME).length());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test write(byte[], off, len), RuntimeException thrown when BufferedOutputStream is closed")
    @Test
    void testWriteWithThreeParametersTrowsRuntimeException() {
        try (var bufferedOutputStream = new BufferedOutputStream(3, new FileOutputStream(FILE_NAME))) {

            bufferedOutputStream.close();

            assertThrows(RuntimeException.class, () -> bufferedOutputStream.write(BYTES, 0, BYTES.length), "InputStream is closed");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test write(byte[], off, len), IndexOutOfBoundsException is thrown when (off > b.length)")
    @Test
    void testWriteWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenOffBiggerDestArraySize() {
        try (var bufferedOutputStream = new BufferedOutputStream(3, new FileOutputStream(FILE_NAME))) {

            assertThrows(IndexOutOfBoundsException.class, () -> bufferedOutputStream.write(BYTES, 13, 1));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test write(byte[], off, len), IndexOutOfBoundsException is thrown when (len > b.length)")
    @Test
    void testWriteWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenLenBiggerDestArraySize() {
        try (var bufferedOutputStream = new BufferedOutputStream(3, new FileOutputStream(FILE_NAME))) {

            assertThrows(IndexOutOfBoundsException.class, () -> bufferedOutputStream.write(BYTES, 0, 13));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test write(byte[], off, len), IndexOutOfBoundsException is thrown when ((len + off) > b.length)")
    @Test
    void testWriteWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenSumOfLenAndOffBiggerDestArraySize() {
        try (var bufferedOutputStream = new BufferedOutputStream(3, new FileOutputStream(FILE_NAME))) {

            assertThrows(IndexOutOfBoundsException.class, () -> bufferedOutputStream.write(BYTES, 7, 7));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test write(byte[], off, len), IndexOutOfBoundsException is thrown when (off < 0)")
    @Test
    void testWriteWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenOffLessNil() {
        try (var bufferedOutputStream = new BufferedOutputStream(3, new FileOutputStream(FILE_NAME))) {

            assertThrows(IndexOutOfBoundsException.class, () -> bufferedOutputStream.write(BYTES, -1, 2));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test write(byte[], off, len), IndexOutOfBoundsException is thrown when (len < 0)")
    @Test
    void testWriteWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenLenLessNil() {
        try (var bufferedOutputStream = new BufferedOutputStream(3, new FileOutputStream(FILE_NAME))) {

            assertThrows(IndexOutOfBoundsException.class, () -> bufferedOutputStream.write(BYTES, 0, -1));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test write(byte[], off, len), IndexOutOfBoundsException is thrown when (len == 0)")
    @Test
    void testWriteWithThreeParametersThrowsIndexOutOfBoundsExceptionWhenLenEqualsNil() {
        try (var bufferedOutputStream = new BufferedOutputStream(3, new FileOutputStream(FILE_NAME))) {

            assertThrows(IndexOutOfBoundsException.class, () -> bufferedOutputStream.write(BYTES, 0, 0));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test write(byte[], off, len), does not flush() when buffer is not full")
    @Test
    void testWriteWithThreeParameters() {
        try (var bufferedOutputStream = new BufferedOutputStream(100, new FileOutputStream(FILE_NAME))) {

            bufferedOutputStream.write(BYTES, 0, BYTES.length);

            assertEquals(0, new File(FILE_NAME).length());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test write(byte[], off, len), write directly to file whole Src Array when buffer size less then (len)")
    @Test
    void testWriteWithThreeParametersWriteDirectlyToFile() {
        try (var bufferedOutputStream = new BufferedOutputStream(8, new FileOutputStream(FILE_NAME))) {

            bufferedOutputStream.write(BYTES, 0, BYTES.length);

            assertEquals(12, new File(FILE_NAME).length());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Test write(byte[], off, len), flushes to file when buffer is full and write directly to file new request to write" +
            "then after new request to write filling of buffer is occurred ")
    @Test
    void testWriteWithThreeParametersFlushesWhenBufferIsFull() {
        try (var bufferedOutputStream = new BufferedOutputStream(8, new FileOutputStream(FILE_NAME))) {

            var file = new File(FILE_NAME);
            bufferedOutputStream.write(BYTES, 0, 8);

            assertEquals(0, file.length());

            bufferedOutputStream.write(BYTES, 8, BYTES.length - 8);

            assertEquals(12, file.length());

            bufferedOutputStream.write("BYTES".getBytes(), 0, 5);

            assertEquals(12, file.length());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFlush() {
        try (var bufferedOutputStream = new BufferedOutputStream(8, new FileOutputStream(FILE_NAME))) {
            var file = new File(FILE_NAME);
            bufferedOutputStream.write(BYTES[0]);

            assertEquals(0, file.length());

            bufferedOutputStream.flush();

            assertEquals(1, file.length());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}