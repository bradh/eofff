package net.frogmouth.rnd.eofff.jpegxl.fileformat;

import static org.testng.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ConformanceCafeTest {
    private static final Logger LOG = LoggerFactory.getLogger(ConformanceCafeTest.class);
    private List<Box> boxes;

    public ConformanceCafeTest() {}

    private Path getExample() {
        String fileName = "cafe/input.jxl";
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
        assertEquals(boxes.size(), 4);
    }

    @Test
    public void checkSignature() {
        Box box0 = boxes.get(0);
        assertTrue(box0 instanceof SignatureBox);
        SignatureBox signatureBox = (SignatureBox) box0;
        assertEquals(signatureBox.getData(), 0x0D0A870A);
    }

    @Test
    public void checkFtypBox() {
        Box box1 = boxes.get(1);
        assertTrue(box1 instanceof FileTypeBox);
        FileTypeBox ftyp = (FileTypeBox) box1;
        assertEquals(ftyp.getMajorBrand().toString(), "jxl ");
        assertEquals(ftyp.getMinorVersion(), 0);
        assertEquals(ftyp.getCompatibleBrands().size(), 1);
        assertEquals(ftyp.getCompatibleBrands().get(0).toString(), "jxl ");
    }

    @Test
    public void checkBitstreamReconstructionDataBox() {
        Box box2 = boxes.get(2);
        assertTrue(box2 instanceof BitstreamReconstructionDataBox);
        // TODO: make sense of this
    }

    @Test
    public void checkBox3() {
        Box box3 = boxes.get(3);
        assertTrue(box3 instanceof CodestreamBox);
    }
}
