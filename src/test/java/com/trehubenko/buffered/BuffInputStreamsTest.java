package com.trehubenko.buffered;

import com.trehubenko.AbstractInputStreamTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;


public class BuffInputStreamsTest extends AbstractInputStreamTest {


    public BuffInputStreamsTest() throws FileNotFoundException {
        inputStream = new BufferedInputStream(new FileInputStream(Path.of("").toAbsolutePath() + Path.of("/src/main/resources/text.txt").toString()));
    }
}
