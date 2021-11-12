package net.frogmouth.rnd.eofff.isobmff;

import static org.testng.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FtypBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import org.testng.annotations.Test;

public class ParseTest {

    public ParseTest() {}

    private Path getExample() {
        String fileName = "example.heic";
        return getPathFromResourceName(fileName);
    }

    private Path getPathFromResourceName(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return Paths.get(classLoader.getResource(fileName).getPath());
    }

    @Test
    public void test() throws IOException {
        Path testFile = getExample();
        FileParser fileParser = new FileParser();
        List<Box> boxes = fileParser.parse(testFile);
        for (Box box : boxes) {
            System.out.println(box.toString());
        }
        assertEquals(boxes.size(), 7);

        Box box0 = boxes.get(0);
        assertTrue(box0 instanceof FtypBox);
        FtypBox ftyp = (FtypBox) box0;
        assertEquals(ftyp.getBoxName(), "ftyp");
        assertEquals(ftyp.getMajorBrand(), "mif1");
        assertEquals(ftyp.getMinorVersion(), 0);
        assertEquals(ftyp.getFourCC(), "ftyp");
        assertEquals(ftyp.getCompatibleBrands().size(), 3);
        assertEquals(ftyp.getCompatibleBrands().get(0), "mif1");
        assertEquals(ftyp.getCompatibleBrands().get(1), "heic");
        assertEquals(ftyp.getCompatibleBrands().get(2), "hevc");

        Box box1 = boxes.get(1);
        assertTrue(box1 instanceof MetaBox);
        MetaBox meta = (MetaBox) box1;
        assertEquals(meta.getFourCC(), "meta");
        assertEquals(meta.getFullName(), "MetaBox");
        assertEquals(meta.getVersion(), 0);
        assertEquals(meta.getFlags(), new byte[] {0x00, 0x00, 0x00});
        assertEquals(meta.getNestedBoxes().size(), 6);

        Box box2 = boxes.get(2);
        assertEquals(box2.getBoxName(), "mdat");

        Box box3 = boxes.get(3);
        assertEquals(box3.getBoxName(), "mdat");

        Box box4 = boxes.get(4);
        assertEquals(box4.getBoxName(), "mdat");

        Box box5 = boxes.get(5);
        assertEquals(box5.getBoxName(), "mdat");

        Box box6 = boxes.get(6);
        assertEquals(box6.getBoxName(), "mdat");
    }
}
