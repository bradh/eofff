package net.frogmouth.rnd.eofff.isobmff.tfhd;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.ByteArrayParser;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

/** Unit test for tfhd. */
public class TrackFragmentHeaderBoxTest {
    private static final byte[] TFHD_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x18,
                0x74,
                0x66,
                0x68,
                0x64,
                0x00,
                0x00,
                0x00,
                0x01,
                0x00,
                0x00,
                0x00,
                (byte) 0xc9,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                0x09,
                0x24
            };

    @Test
    public void checkWrite() throws IOException {
        TrackFragmentHeaderBox box = new TrackFragmentHeaderBox();
        box.setFlags(1);
        box.setTrackID(201);
        box.setBaseDataOffset(2340);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, TFHD_BYTES);
    }

    @Test
    public void checkParse() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(TFHD_BYTES);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof TrackFragmentHeaderBox);
        TrackFragmentHeaderBox tfhd = (TrackFragmentHeaderBox) box;
        assertTrue(tfhd.getFourCC().toString().equals("tfhd"));
        assertEquals(tfhd.getFlags(), 1);
        assertEquals(tfhd.getTrackID(), 201);
        assertEquals(tfhd.getBaseDataOffset(), 2340);
    }
}
