package net.frogmouth.rnd.eofff.uncompressed.depi;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.PropertyTestSupport;
import org.testng.annotations.Test;

/** Unit tests for DepthInfoBox. */
public class DepthInfoBoxTest extends PropertyTestSupport {
    private static final byte[] DEPI_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x16,
                0x64,
                0x65,
                0x70,
                0x69,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                0x03,
                0x00,
                0x01,
                0x00,
                0x02,
                0x00,
                0x04,
                0x30,
                (byte) 0xd0
            };

    @Test
    public void checkParse() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(DEPI_BYTES);
        assertTrue(prop instanceof DepthInfoBox);
        DepthInfoBox depi = (DepthInfoBox) prop;
        assertTrue(depi.getFourCC().toString().equals("depi"));
        assertEquals(depi.getFullName(), "DepthInfoBox");
        assertEquals(depi.getComponentIndexes().size(), 3);
        assertEquals(depi.getComponentIndexes().get(0), 1);
        assertEquals(depi.getComponentIndexes().get(1), 2);
        assertEquals(depi.getComponentIndexes().get(2), 4);
        assertEquals(depi.getNknear(), 48);
        assertEquals(depi.getNkfar(), 208);
        assertEquals(depi.toString(), "DepthInfoBox 'depi': nkNear: 48, nkFar: 208");
    }

    @Test
    public void checkWrite() throws IOException {
        DepthInfoBox box = new DepthInfoBox();
        box.addComponentIndex(1);
        box.addComponentIndex(2);
        box.addComponentIndex(4);
        box.setNknear(48);
        box.setNkfar(208);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, DEPI_BYTES);
    }
}
