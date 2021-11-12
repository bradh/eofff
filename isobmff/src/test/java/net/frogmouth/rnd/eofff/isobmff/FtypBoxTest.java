package net.frogmouth.rnd.eofff.isobmff;

import static org.testng.Assert.assertEquals;

import net.frogmouth.rnd.eofff.isobmff.ftyp.FtypBox;
import org.testng.annotations.Test;

public class FtypBoxTest {
    @Test
    public void checkFullName() {
        FtypBox box = new FtypBox(28, "ftyp");
        assertEquals(box.getFullName(), "FileTypeBox");
    }

    @Test
    public void checkBoxName() {
        FtypBox box = new FtypBox(28, "ftyp");
        assertEquals(box.getBoxName(), "ftyp");
    }
}
