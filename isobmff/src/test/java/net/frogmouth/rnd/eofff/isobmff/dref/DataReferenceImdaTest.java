package net.frogmouth.rnd.eofff.isobmff.dref;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.ByteArrayParser;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

/** Unit test for DataEntryImdaBox. */
public class DataReferenceImdaTest {
    private static final byte[] DREF_BYTES =
            new byte[] {
                0x00, 0x00, 0x00, 0x20, 0x64, 0x72, 0x65, 0x66, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x01, 0x00, 0x00, 0x00, 0x10, 0x69, 0x6d, 0x64, 0x74, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x7F
            };

    @Test
    public void checkWrite() throws IOException {
        DataEntryImdaBox imda = new DataEntryImdaBox();
        imda.setImdaRefIdentifier(127);
        DataReferenceBox box = new DataReferenceBoxBuilder().withDataReference(imda).build();
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
        assertTrue(dref.getEntries().get(0) instanceof DataEntryImdaBox);
        DataEntryImdaBox imda = (DataEntryImdaBox) dref.getEntries().get(0);
        assertEquals(imda.getFullName(), "DataEntryImdaBox");
        assertEquals(imda.getFourCC().toString(), "imdt");
        assertEquals(imda.toString(1), "    DataEntryImdaBox 'imdt': imda_ref_identifier: 127");
    }
}
