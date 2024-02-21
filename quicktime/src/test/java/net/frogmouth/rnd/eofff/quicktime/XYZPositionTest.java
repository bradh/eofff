package net.frogmouth.rnd.eofff.quicktime;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.ByteArrayParser;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

public class XYZPositionTest {
    private static final byte[] XYZ_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x1e,
                (byte) 0xa9,
                0x78,
                0x79,
                0x7a,
                0x00,
                0x12,
                0x15,
                (byte) 0xc7,
                0x2b,
                0x34,
                0x31,
                0x2e,
                0x33,
                0x37,
                0x35,
                0x38,
                0x2b,
                0x30,
                0x30,
                0x32,
                0x2e,
                0x31,
                0x34,
                0x39,
                0x32,
                0x2f
            };

    @Test
    public void checkParse() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(XYZ_BYTES);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof XYZPosition);
        XYZPosition xyz = (XYZPosition) box;
        assertEquals(xyz.getFourCC().toString(), "©xyz");
        assertEquals(xyz.getLanguage().getLanguage(), "eng");
        assertEquals(xyz.getValue(), "+41.3758+002.1492/");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, XYZ_BYTES);
    }
}
