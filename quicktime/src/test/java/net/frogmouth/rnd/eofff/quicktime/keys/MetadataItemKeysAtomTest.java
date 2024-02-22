package net.frogmouth.rnd.eofff.quicktime.keys;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.ByteArrayParser;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

public class MetadataItemKeysAtomTest {

    private static final byte[] KEYS_BYTES_S23 = {
        0x00, 0x00, 0x00, 0x4a, 0x6b, 0x65, 0x79, 0x73,
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02,
        0x00, 0x00, 0x00, 0x1b, 0x6d, 0x64, 0x74, 0x61,
        0x63, 0x6f, 0x6d, 0x2e, 0x61, 0x6e, 0x64, 0x72,
        0x6f, 0x69, 0x64, 0x2e, 0x76, 0x65, 0x72, 0x73,
        0x69, 0x6f, 0x6e, 0x00, 0x00, 0x00, 0x1f, 0x6d,
        0x64, 0x74, 0x61, 0x63, 0x6f, 0x6d, 0x2e, 0x61,
        0x6e, 0x64, 0x72, 0x6f, 0x69, 0x64, 0x2e, 0x63,
        0x61, 0x70, 0x74, 0x75, 0x72, 0x65, 0x2e, 0x66,
        0x70, 0x73
    };

    @Test
    public void checkParseS23() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(KEYS_BYTES_S23);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof MetadataItemKeysAtom);
        MetadataItemKeysAtom keys = (MetadataItemKeysAtom) box;
        assertEquals(keys.getFourCC().toString(), "keys");
        assertEquals(keys.getEntries().size(), 2);
        assertEquals(keys.getEntries().get(0).namespace().toString(), "mdta");
        assertEquals(keys.getEntries().get(0).value(), "com.android.version");
        assertEquals(keys.getEntries().get(1).namespace().toString(), "mdta");
        assertEquals(keys.getEntries().get(1).value(), "com.android.capture.fps");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, KEYS_BYTES_S23);
    }
}
