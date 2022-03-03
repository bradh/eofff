package net.frogmouth.rnd.eofff.isobmff;

import static org.testng.Assert.assertEquals;

import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import org.testng.annotations.Test;

public class FtypBoxTest {
    @Test
    public void checkFullName() {
        FileTypeBox box = new FileTypeBox(28, new FourCC("ftyp"));
        assertEquals(box.getFullName(), "File Type Box");
    }
}
