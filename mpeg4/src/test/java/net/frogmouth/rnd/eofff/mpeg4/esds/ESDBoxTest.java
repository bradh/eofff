package net.frogmouth.rnd.eofff.mpeg4.esds;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.ByteArrayParser;
import org.junit.jupiter.api.Test;

/**
 * @author bradh
 */
public class ESDBoxTest {

    private static final byte[] ESDS_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x32,
                0x65,
                0x73,
                0x64,
                0x73,
                0x00,
                0x00,
                0x00,
                0x00,
                0x03,
                (byte) 0x80,
                (byte) 0x80,
                0x22,
                0x00,
                0x00,
                0x00,
                0x04,
                (byte) 0x80,
                (byte) 0x80,
                0x16,
                0x40,
                0x15,
                0x00,
                0x20,
                0x00,
                0x00,
                0x00,
                (byte) 0xbb,
                (byte) 0x80,
                0x00,
                0x00,
                (byte) 0xbb,
                (byte) 0x80,
                0x05,
                (byte) 0x80,
                (byte) 0x80,
                0x05,
                0x11,
                (byte) 0x90,
                0x00,
                0x00,
                0x00,
                0x06,
                (byte) 0x80,
                (byte) 0x80,
                0x01,
                0x02
            };

    @Test
    public void checkParse() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(ESDS_BYTES);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof ESDBox);
        ESDBox esds = (ESDBox) box;
        assertTrue(esds.getFourCC().toString().equals("esds"));
    }
}
