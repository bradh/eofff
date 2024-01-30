package net.frogmouth.rnd.eofff.mpeg4.iods;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.ByteArrayParser;
import org.junit.jupiter.api.Test;

/**
 * @author bradh
 */
public class ObjectDescriptorBoxTest {
    private static final byte[] IODS_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x21,
                0x69,
                0x6f,
                0x64,
                0x73,
                0x00,
                0x00,
                0x00,
                0x00,
                0x10,
                0x13,
                0x00,
                0x4f,
                0x01,
                0x01,
                0x28,
                (byte) 0xf5,
                0x01,
                0x0e,
                0x04,
                0x00,
                0x00,
                0x00,
                0x01,
                0x0e,
                0x04,
                0x00,
                0x00,
                0x00,
                0x02
            };

    /*
        @Test
        public void checkWrite() throws IOException {
            TrackReferenceBox box =
                    new TrackReferenceBoxBuilder()
                            .withReference(
                                    new TrackReferenceTypeBox(TrackReference.CDSC, new long[] {3, 7}))
                            .build();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
            box.writeTo(streamWriter);
            byte[] bytes = baos.toByteArray();
            assertEquals(bytes, TREF_CDSC_BYTES);
            File testTref = new File("tref.bin");
            Files.write(testTref.toPath(), bytes, StandardOpenOption.CREATE);
            assertTrue(box.toString(0).startsWith("TrackReferenceBox 'tref': reference count=1"));
            assertTrue(box.toString(0).endsWith("reference_type=cdsc, track_IDs=[3, 7]"));
        }
    */

    @Test
    public void checkParse() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(IODS_BYTES);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof ObjectDescriptorBox);
        ObjectDescriptorBox iods = (ObjectDescriptorBox) box;
        assertTrue(iods.getFourCC().toString().equals("iods"));
    }
}
