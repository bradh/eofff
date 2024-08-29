package net.frogmouth.rnd.eofff.uncompressed.itai;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.PropertyTestSupport;
import org.testng.annotations.Test;

/** Unit tests for TAITimeStampBox. */
public class TAITimeStampBoxTest extends PropertyTestSupport {
    private static final byte[] ITAI_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x15,
                0x69,
                0x74,
                0x61,
                0x69,
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
                0x00
            };

    @Test
    public void checkParse() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(ITAI_BYTES);
        assertTrue(prop instanceof TAITimeStampBox);
        TAITimeStampBox itai = (TAITimeStampBox) prop;
        assertEquals(itai.getFullName(), "TAITimeStampBox");
        assertTrue(itai.getFourCC().toString().equals("itai"));
        assertEquals(
                itai.toString(0),
                "TAITimeStampBox 'itai': TAI_time_stamp: invalid, status_bits: 0x00");
    }

    @Test
    public void checkWrite() throws IOException {
        TAITimeStampBox box = new TAITimeStampBox();
        assertEquals(box.getFullName(), "TAITimeStampBox");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, ITAI_BYTES);
    }
}
