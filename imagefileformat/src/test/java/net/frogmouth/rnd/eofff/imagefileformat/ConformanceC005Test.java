package net.frogmouth.rnd.eofff.imagefileformat;

import static org.testng.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import net.frogmouth.rnd.eofff.imagefileformat.brands.ImageFileFormatBrand;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ConformanceC005Test {
    private static final Logger LOG = LoggerFactory.getLogger(ConformanceC005Test.class);
    private List<Box> boxes;

    public ConformanceC005Test() {}

    private Path getExample() {
        String fileName = "heif_conformance/conformance_files/C005.heic";
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
        assertEquals(boxes.size(), 3);
    }

    @Test
    public void checkFtypBox() {
        Box box0 = boxes.get(0);
        assertTrue(box0 instanceof FileTypeBox);
        FileTypeBox ftyp = (FileTypeBox) box0;
        assertEquals(ftyp.getFourCC(), new FourCC("ftyp"));
        assertEquals(ftyp.getMajorBrand(), ImageFileFormatBrand.MIF1);
        assertEquals(ftyp.getMinorVersion(), 0);
        assertEquals(ftyp.getCompatibleBrands().size(), 2);
        assertEquals(ftyp.getCompatibleBrands().get(0), ImageFileFormatBrand.HEIC);
        assertEquals(ftyp.getCompatibleBrands().get(1), ImageFileFormatBrand.MIF1);
    }

    @Test
    public void checkMetaBox() {
        Box box1 = boxes.get(1);
        assertTrue(box1 instanceof MetaBox);
        MetaBox meta = (MetaBox) box1;
        assertEquals(meta.getFourCC(), new FourCC("meta"));
        assertEquals(meta.getFullName(), "MetaBox");
        assertEquals(meta.getVersion(), 0);
        assertEquals(meta.getFlags(), 0x000000);
        assertEquals(meta.getNestedBoxes().size(), 6);
        Box nestedBox0 = meta.getNestedBoxes().get(0);
        assertTrue(nestedBox0 instanceof HandlerBox);
        assertEquals(nestedBox0.getFourCC(), new FourCC("hdlr"));
        HandlerBox hdlr = (HandlerBox) nestedBox0;
        assertEquals(hdlr.getFullName(), "HandlerBox");
        assertEquals(hdlr.getHandlerType(), "pict");
        assertEquals(hdlr.getPreDefined(), 0);
        assertEquals(hdlr.getName(), "");
        assertEquals(hdlr.getReserved0(), 0);
        assertEquals(hdlr.getReserved1(), 0);
        assertEquals(hdlr.getReserved2(), 0);
        // TODO: check more boxes
    }
}
