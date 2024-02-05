package net.frogmouth.rnd.eofff.isobmff.dref;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.ByteArrayParser;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

/** Unit test for DataReferenceBox. */
public class DataReferenceBoxTest {
    private static final byte[] DREF_BYTES =
            new byte[] {
                0x00, 0x00, 0x00, 0x1c, 0x64, 0x72, 0x65, 0x66, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x01, 0x00, 0x00, 0x00, 0x0c, 0x75, 0x72, 0x6c, 0x20, 0x00, 0x00, 0x00, 0x01
            };

    @Test
    public void checkWrite() throws IOException {
        DataEntryUrlBox url = new DataEntryUrlBox();
        url.setFlags(DataEntryUrlBox.MEDIA_DATA_IN_SAME_FILE_FLAG);
        DataReferenceBox box = new DataReferenceBoxBuilder().withDataReference(url).build();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, DREF_BYTES);
    }

    @Test
    public void checkParse() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(DREF_BYTES);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof DataReferenceBox);
        DataReferenceBox dref = (DataReferenceBox) box;
        assertTrue(dref.getFourCC().toString().equals("dref"));
        assertEquals(dref.getEntries().size(), 1);
        assertTrue(dref.getEntries().get(0) instanceof DataEntryUrlBox);
        DataEntryUrlBox url = (DataEntryUrlBox) dref.getEntries().get(0);
        assertEquals(url.getFullName(), "DataEntryUrlBox");
        assertEquals(url.getFourCC().toString(), "url ");
        assertEquals(url.toString(1), "    DataEntryUrlBox 'url ': location: [in same file]");
        assertNull(url.getLocation());
    }
}
