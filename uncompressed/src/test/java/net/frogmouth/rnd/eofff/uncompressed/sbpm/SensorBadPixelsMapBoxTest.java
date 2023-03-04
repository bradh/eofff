package net.frogmouth.rnd.eofff.uncompressed.sbpm;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.ByteArrayParser;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

/** Unit tests for SensorBadPixelsMapBox. */
public class SensorBadPixelsMapBoxTest {
    private static final byte[] SBPM_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x15,
                0x73,
                0x62,
                0x70,
                0x6d,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                0x03,
                0x00,
                0x00,
                0x00,
                0x01,
                0x00,
                0x02,
                (byte) 0x80
            };

    @Test
    public void checkParseNoString() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(SBPM_BYTES);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof SensorBadPixelsMapBox);
        SensorBadPixelsMapBox sbpm = (SensorBadPixelsMapBox) box;
        assertTrue(sbpm.getFourCC().toString().equals("sbpm"));
        assertEquals(sbpm.getComponentIndexes().size(), 3);
        assertEquals(sbpm.getComponentIndexes().get(0), 0);
        assertEquals(sbpm.getComponentIndexes().get(1), 1);
        assertEquals(sbpm.getComponentIndexes().get(2), 2);
    }

    @Test
    public void checkWrite() throws IOException {
        SensorBadPixelsMapBox box = new SensorBadPixelsMapBox();
        box.addComponentIndex(0);
        box.addComponentIndex(1);
        box.addComponentIndex(2);
        box.setCorrectionApplied(true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, SBPM_BYTES);
    }
}
