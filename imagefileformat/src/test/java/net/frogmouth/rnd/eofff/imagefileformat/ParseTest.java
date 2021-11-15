package net.frogmouth.rnd.eofff.imagefileformat;

import static org.testng.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FtypBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HdlrBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ParseTest {
    private static final Logger LOG = LoggerFactory.getLogger(ParseTest.class);
    private List<Box> boxes;

    public ParseTest() {}

    private Path getExample() {
        String fileName = "example.heic";
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
        assertEquals(boxes.size(), 7);
    }

    @Test
    public void checkFtypBox() {
        Box box0 = boxes.get(0);
        assertTrue(box0 instanceof FtypBox);
        FtypBox ftyp = (FtypBox) box0;
        assertEquals(ftyp.getFourCC(), new FourCC("ftyp"));
        assertEquals(ftyp.getMajorBrand(), new FourCC("mif1"));
        assertEquals(ftyp.getMinorVersion(), 0);
        assertEquals(ftyp.getCompatibleBrands().size(), 3);
        assertEquals(ftyp.getCompatibleBrands().get(0), new FourCC("mif1"));
        assertEquals(ftyp.getCompatibleBrands().get(1), new FourCC("heic"));
        assertEquals(ftyp.getCompatibleBrands().get(2), new FourCC("hevc"));
    }

    @Test
    public void checkMetaBox() {
        Box box1 = boxes.get(1);
        assertTrue(box1 instanceof MetaBox);
        MetaBox meta = (MetaBox) box1;
        assertEquals(meta.getFourCC(), new FourCC("meta"));
        assertEquals(meta.getFullName(), "MetaBox");
        assertEquals(meta.getVersion(), 0);
        assertEquals(meta.getFlags(), new byte[] {0x00, 0x00, 0x00});
        assertEquals(meta.getNestedBoxes().size(), 6);
        Box nestedBox0 = meta.getNestedBoxes().get(0);
        assertTrue(nestedBox0 instanceof HdlrBox);
        assertEquals(nestedBox0.getFourCC(), new FourCC("hdlr"));
        HdlrBox hdlr = (HdlrBox) nestedBox0;
        assertEquals(hdlr.getFullName(), "HandlerBox");
        assertEquals(hdlr.getHandlerType(), "pict");
        assertEquals(hdlr.getPreDefined(), 0);
        assertEquals(hdlr.getName(), "");
        assertEquals(hdlr.getReserved0(), 0);
        assertEquals(hdlr.getReserved1(), 0);
        assertEquals(hdlr.getReserved2(), 0);
    }

    @Test
    public void test() {
        Box box2 = boxes.get(2);
        assertEquals(box2.getFourCC(), new FourCC("mdat"));

        Box box3 = boxes.get(3);
        assertEquals(box3.getFourCC(), new FourCC("mdat"));

        Box box4 = boxes.get(4);
        assertEquals(box4.getFourCC(), new FourCC("mdat"));

        Box box5 = boxes.get(5);
        assertEquals(box5.getFourCC(), new FourCC("mdat"));

        Box box6 = boxes.get(6);
        assertEquals(box6.getFourCC(), new FourCC("mdat"));
    }
}
