package net.frogmouth.rnd.eofff.quicktime.ilst;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.ByteArrayParser;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

public class MetadataItemListAtomTest {
    private static final byte[] ILST_BYTES = {
        0x00, 0x00, 0x00, 0x2d, 0x69, 0x6c, 0x73, 0x74,
        0x00, 0x00, 0x00, 0x25, (byte) 0xa9, 0x74, 0x6f, 0x6f,
        0x00, 0x00, 0x00, 0x1d, 0x64, 0x61, 0x74, 0x61,
        0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00,
        0x4c, 0x61, 0x76, 0x66, 0x35, 0x35, 0x2e, 0x32,
        0x36, 0x2e, 0x31, 0x30, 0x30
    };

    @Test
    public void checkParse() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(ILST_BYTES);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof MetadataItemListAtom);
        MetadataItemListAtom ilst = (MetadataItemListAtom) box;
        assertEquals(ilst.getFourCC().toString(), "ilst");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, ILST_BYTES);
    }

    private static final byte[] ILST_BYTES_S23 = {
        0x00,
        0x00,
        0x00,
        0x3e,
        0x69,
        0x6c,
        0x73,
        0x74,
        0x00,
        0x00,
        0x00,
        0x1a,
        0x00,
        0x00,
        0x00,
        0x01,
        0x00,
        0x00,
        0x00,
        0x12,
        0x64,
        0x61,
        0x74,
        0x61,
        0x00,
        0x00,
        0x00,
        0x01,
        0x00,
        0x00,
        0x00,
        0x00,
        0x31,
        0x34,
        0x00,
        0x00,
        0x00,
        0x1c,
        0x00,
        0x00,
        0x00,
        0x02,
        0x00,
        0x00,
        0x00,
        0x14,
        0x64,
        0x61,
        0x74,
        0x61,
        0x00,
        0x00,
        0x00,
        0x17,
        0x00,
        0x00,
        0x00,
        0x00,
        0x41,
        (byte) 0xf0,
        0x00,
        0x00
    };

    @Test
    public void checkParseS23() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(ILST_BYTES_S23);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof MetadataItemListAtom);
        MetadataItemListAtom ilst = (MetadataItemListAtom) box;
        assertEquals(ilst.getFourCC().toString(), "ilst");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, ILST_BYTES_S23);
    }
}
