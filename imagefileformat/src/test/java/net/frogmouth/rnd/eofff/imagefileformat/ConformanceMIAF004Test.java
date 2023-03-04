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

public class ConformanceMIAF004Test {
    private static final Logger LOG = LoggerFactory.getLogger(ConformanceMIAF004Test.class);
    private List<Box> boxes;

    private Path getExample() {
        String fileName = "heif_conformance/conformance_files/MIAF004.heic";
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
        assertEquals(ftyp.getCompatibleBrands().size(), 5);
        assertEquals(ftyp.getCompatibleBrands().get(0), ImageFileFormatBrand.HEIC);
        assertEquals(ftyp.getCompatibleBrands().get(1), ImageFileFormatBrand.MIF1);
        assertEquals(ftyp.getCompatibleBrands().get(2), new Brand("miaf"));
        assertEquals(ftyp.getCompatibleBrands().get(3), new Brand("MiHB"));
        assertEquals(ftyp.getCompatibleBrands().get(4), new Brand("MiPr"));
    }
}