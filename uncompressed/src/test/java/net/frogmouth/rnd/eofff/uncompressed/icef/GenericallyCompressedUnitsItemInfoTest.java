package net.frogmouth.rnd.eofff.uncompressed.icef;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.PropertyTestSupport;
import org.testng.annotations.Test;

/** Unit tests for GenericallyCompressedUnitsItemInfo. */
public class GenericallyCompressedUnitsItemInfoTest extends PropertyTestSupport {
    private static final byte[] ICEF_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x19,
                0x69,
                0x63,
                0x65,
                0x66,
                0x00,
                0x00,
                0x00,
                0x00,
                0b01000000,
                0x00,
                0x00,
                0x00,
                0x02,
                0x00,
                0x0a,
                0x03,
                0x03,
                0x02,
                0x03,
                0x0a,
                0x07
            };

    @Test
    public void checkParse() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(ICEF_BYTES);
        assertTrue(prop instanceof GenericallyCompressedUnitsItemInfo);
        GenericallyCompressedUnitsItemInfo icef = (GenericallyCompressedUnitsItemInfo) prop;
        assertTrue(icef.getFourCC().toString().equals("icef"));
        assertEquals(icef.getFullName(), "GenericallyCompressedUnitsItemInfoBox");
        assertEquals(icef.getCompressedUnitInfos().size(), 2);
        assertEquals(icef.getCompressedUnitInfos().get(0).unitOffset(), 2563);
        assertEquals(icef.getCompressedUnitInfos().get(0).unitSize(), 3);
        assertEquals(icef.getCompressedUnitInfos().get(1).unitOffset(), 131850);
        assertEquals(icef.getCompressedUnitInfos().get(1).unitSize(), 7);
        assertEquals(
                icef.toString(0),
                "GenericallyCompressedUnitsItemInfoBox 'icef': num_compressed_units=2\n    unit_offset=2563, unit_size=3\n    unit_offset=131850, unit_size=7");
    }

    @Test
    public void checkWrite() throws IOException {
        GenericallyCompressedUnitsItemInfo box = new GenericallyCompressedUnitsItemInfo();
        box.addCompressedUnitInfo(new CompressedUnitInfo(0x0a03, 3));
        box.addCompressedUnitInfo(new CompressedUnitInfo(0x02030a, 7));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, ICEF_BYTES);
    }
}
