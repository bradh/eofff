package net.frogmouth.rnd.eofff.uncompressed.cloc;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.PropertyTestSupport;
import org.testng.annotations.Test;

/** Unit tests for ChromaLocationBox. */
public class ChromaLocationBoxTest extends PropertyTestSupport {
    private static final byte[] CLOC_BYTES =
            new byte[] {
                0x00, 0x00, 0x00, 0xd, 0x63, 0x6c, 0x6f, 0x63, 0x00, 0x00, 0x00, 0x00, 0x02
            };

    @Test
    public void checkParseNoString() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(CLOC_BYTES);
        assertTrue(prop instanceof ChromaLocationBox);
        ChromaLocationBox cloc = (ChromaLocationBox) prop;
        assertEquals(cloc.getFullName(), "ChromaLocationBox");
        assertTrue(cloc.getFourCC().toString().equals("cloc"));
        assertEquals(cloc.getChromaLocation(), 2);
        assertEquals(cloc.toString(), "ChromaLocationBox 'cloc':2");
    }

    @Test
    public void checkWrite() throws IOException {
        ChromaLocationBox box = new ChromaLocationBox();
        box.setChromaLocation(2);
        assertEquals(box.getFullName(), "ChromaLocationBox");
        assertEquals(box.toString(), "ChromaLocationBox 'cloc':2");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, CLOC_BYTES);
    }
}
