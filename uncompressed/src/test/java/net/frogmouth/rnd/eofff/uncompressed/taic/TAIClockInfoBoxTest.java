package net.frogmouth.rnd.eofff.uncompressed.taic;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.PropertyTestSupport;
import org.testng.annotations.Test;

/** Unit tests for TAIClockInfoBox. */
public class TAIClockInfoBoxTest extends PropertyTestSupport {
    private static final byte[] TAIC_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x21,
                0x74,
                0x61,
                0x69,
                0x63,
                0x00,
                0x00,
                0x00,
                0x00,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                0x7f,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                (byte) 0xff,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00
            };

    @Test
    public void checkParse() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(TAIC_BYTES);
        assertTrue(prop instanceof TAIClockInfoBox);
        TAIClockInfoBox taic = (TAIClockInfoBox) prop;
        assertEquals(taic.getFullName(), "TAIClockInfoBox");
        assertTrue(taic.getFourCC().toString().equals("taic"));
        assertEquals(
                taic.toString(),
                "TAIClockInfoBox 'taic': time_uncertainty: unknown, correction_offset: unknown, clock_drift_rate: unknown, reference_source_type: unknown");
    }

    @Test
    public void checkWrite() throws IOException {
        TAIClockInfoBox box = new TAIClockInfoBox();
        assertEquals(box.getFullName(), "TAIClockInfoBox");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, TAIC_BYTES);
    }
}
