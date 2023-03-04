package net.frogmouth.rnd.eofff.uncompressed.cloc;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.ByteArrayParser;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

/** Unit tests for ChromaLocationBox. */
public class ChromaLocationBoxTest {
    private static final byte[] CLOC_BYTES =
            new byte[] {
                0x00, 0x00, 0x00, 0xd, 0x63, 0x6c, 0x6f, 0x63, 0x00, 0x00, 0x00, 0x00, 0x02
            };

    @Test
    public void checkParseNoString() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(CLOC_BYTES);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof ChromaLocationBox);
        ChromaLocationBox cloc = (ChromaLocationBox) box;
        assertTrue(cloc.getFourCC().toString().equals("cloc"));
        assertEquals(cloc.getChromaLocation(), 2);
    }

    @Test
    public void checkWrite() throws IOException {
        ChromaLocationBox box = new ChromaLocationBox();
        box.setChromaLocation(2);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, CLOC_BYTES);
    }
}
