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
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ConformanceC048Test {
    private static final Logger LOG = LoggerFactory.getLogger(ConformanceC048Test.class);
    private List<Box> boxes;

    private Path getExample() {
        String fileName = "heif_conformance/conformance_files/C048.heic";
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
    public void checkFtypBox() {
        Box box0 = boxes.get(0);
        assertTrue(box0 instanceof FileTypeBox);
        FileTypeBox ftyp = (FileTypeBox) box0;
        assertEquals(ftyp.getFourCC(), new FourCC("ftyp"));
        assertEquals(ftyp.getMajorBrand(), ImageFileFormatBrand.MSF1);
        assertEquals(ftyp.getMinorVersion(), 0);
        assertEquals(ftyp.getCompatibleBrands().size(), 5);
        assertEquals(ftyp.getCompatibleBrands().get(0), ImageFileFormatBrand.MIF1);
        assertEquals(ftyp.getCompatibleBrands().get(1), new Brand("msf1"));
        assertEquals(ftyp.getCompatibleBrands().get(2), new Brand("hevx"));
        assertEquals(ftyp.getCompatibleBrands().get(3), new Brand("heix"));
        assertEquals(ftyp.getCompatibleBrands().get(4), new Brand("iso8"));
    }
}
