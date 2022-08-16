package net.frogmouth.rnd.eofff.isobmff.trgr;

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

/** Unit test for TrackGroupBox. */
public class TrackGroupBoxTest {
    private static final byte[] TRGR_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x18,
                0x74,
                0x72,
                0x67,
                0x72,
                0x00,
                0x00,
                0x00,
                0x10,
                0x6d,
                0x73,
                0x72,
                0x63,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                (byte) 0x80,
                (byte) 0x8F
            };

    @Test
    public void checkWrite() throws IOException {
        TrackGroupBox box =
                new TrackGroupBoxBuilder()
                        .withGroup(new TrackGroupTypeBox(TrackGroupType.MSRC, 32911))
                        .build();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, TRGR_BYTES);
        File testTref = new File("trgr.bin");
        Files.write(testTref.toPath(), bytes, StandardOpenOption.CREATE);
        assertTrue(box.toString().startsWith("TrackGroupBox 'trgr' : group count=1"));
        assertTrue(box.toString().endsWith("group_type=msrc, track_group_id=32911"));
    }

    @Test
    public void checkParse() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(TRGR_BYTES);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof TrackGroupBox);
        TrackGroupBox trgr = (TrackGroupBox) box;
        assertTrue(trgr.getFourCC().toString().equals("trgr"));
        assertEquals(trgr.getEntries().size(), 1);
        TrackGroupTypeBox child = trgr.getEntries().get(0);
        assertEquals(child.groupType(), TrackGroupType.MSRC);
        assertEquals(child.groupID(), 32911);
    }
}
