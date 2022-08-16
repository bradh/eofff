package net.frogmouth.rnd.eofff.tools;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TearsTest {
    private static final Logger LOG = LoggerFactory.getLogger(TearsTest.class);
    private List<Box> boxes;

    public TearsTest() {}

    @BeforeTest
    public void parseFile() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Path testFile = Paths.get(classLoader.getResource("Tears_400_x265.mp4").getPath());
        FileParser fileParser = new FileParser();
        boxes = fileParser.parse(testFile);
        for (Box box : boxes) {
            // LOG.info(box.toString());
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
        assertEquals(ftyp.getMajorBrand(), Brand.ISO4);
        assertEquals(ftyp.getMinorVersion(), 1);
        assertEquals(ftyp.getCompatibleBrands().size(), 2);
        assertEquals(ftyp.getCompatibleBrands().get(0), Brand.ISO4);
        assertEquals(ftyp.getCompatibleBrands().get(1), new Brand("hvc1"));
    }

    @Test
    public void test() {
        Box box1 = boxes.get(1);
        assertEquals(box1.getFourCC(), new FourCC("moov"));

        Box box2 = boxes.get(2);
        assertEquals(box2.getFourCC(), new FourCC("mdat"));

        Box box3 = boxes.get(3);
        assertEquals(box3.getFourCC(), new FourCC("free"));
    }

    @Test
    public void writeOut() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (Box box : boxes) {
            box.writeTo(streamWriter);
        }
        File testOut = new File("Tears_out.mp4");
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
    }
}
