package net.frogmouth.rnd.eofff.isobmff.tref;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.ByteArrayParser;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

/** Unit test for TrackReferenceBox. */
public class TrackReferenceBoxTest {
    private static final byte[] TREF_CDSC_BYTES =
            new byte[] {
                0x00, 0x00, 0x00, 0x18, 0x74, 0x72, 0x65, 0x66, 0x00, 0x00, 0x00, 0x10, 0x63, 0x64,
                0x73, 0x63, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x07
            };

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
        assertTrue(box.toString().startsWith("TrackReferenceBox 'tref' : reference count=1"));
        assertTrue(box.toString().endsWith("reference_type=cdsc, track_IDs=[3, 7]"));
    }

    @Test
    public void checkParse() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(TREF_CDSC_BYTES);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof TrackReferenceBox);
        TrackReferenceBox tref = (TrackReferenceBox) box;
        assertTrue(tref.getFourCC().toString().equals("tref"));
        assertEquals(tref.getEntries().size(), 1);
        TrackReferenceTypeBox child = tref.getEntries().get(0);
        assertEquals(child.referenceType(), TrackReference.CDSC);
        assertEquals(child.trackIDs().length, 2);
        assertEquals(child.trackIDs()[0], 3);
        assertEquals(child.trackIDs()[1], 7);
    }
}
