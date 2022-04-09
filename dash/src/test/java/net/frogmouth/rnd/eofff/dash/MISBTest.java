package net.frogmouth.rnd.eofff.dash;

import static org.testng.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class MISBTest {

    public MISBTest() {}

    private static final Logger LOG = LoggerFactory.getLogger(MISBTest.class);
    private List<Box> boxes;

    private Path getExample() {
        String fileName = "segment_1.m4s";
        return getPathFromResourceName(fileName);
    }

    private Path getPathFromResourceName(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return Paths.get(classLoader.getResource(fileName).getPath());
    }

    @BeforeTest
    public void parseFile() throws IOException {
        Path testFile = getExample();
        FileParser fileParser = new FileParser();
        boxes = fileParser.parse(testFile);
        for (Box box : boxes) {
            LOG.info(box.toString());
        }
    }

    @Test
    public void checkRightNumberOfBoxes() {
        assertEquals(boxes.size(), 56);
    }
}
