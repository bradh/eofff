package net.frogmouth.rnd.eofff.tools;

import static org.testng.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class HEIFTest {
    private static final Logger LOG = LoggerFactory.getLogger(HEIFTest.class);
    private List<Box> boxes;

    public HEIFTest() {}

    @BeforeTest
    public void parseFile() throws IOException {
        Path testFile =
                Path.of(
                        "/home/bradh/eofff/imagefileformat/src/test/resources/heif_conformance/conformance_files/C007.heic");
        FileParser fileParser = new FileParser();
        boxes = fileParser.parse(testFile);
        for (Box box : boxes) {
            LOG.info(box.toString());
        }
    }

    @Test
    public void checkFtypBox() {
        Box box0 = boxes.get(0);
        assertTrue(box0 instanceof FileTypeBox);
    }
}
