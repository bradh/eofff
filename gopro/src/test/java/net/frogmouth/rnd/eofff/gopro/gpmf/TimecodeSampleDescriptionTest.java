package net.frogmouth.rnd.eofff.gopro.gpmf;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.ByteArrayParser;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.stsd.SampleDescriptionBox;
import org.testng.annotations.Test;

public class TimecodeSampleDescriptionTest {

    private static final byte[] STSD_BYTES = {
        0x00, 0x00, 0x00, 0x32, 0x73, 0x74, 0x73, 0x64,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
        0x00, 0x00, 0x00, 0x22, 0x74, 0x6d, 0x63, 0x64,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02,
        0x00, 0x00, 0x75, 0x30, 0x00, 0x00, 0x03, (byte) 0xe9,
        0x1d, 0x00
    };

    public TimecodeSampleDescriptionTest() {}

    @Test
    public void checkParse() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(STSD_BYTES);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof SampleDescriptionBox);
        SampleDescriptionBox stsd = (SampleDescriptionBox) box;
        assertTrue(stsd.getFourCC().toString().equals("stsd"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, STSD_BYTES);
    }
}
