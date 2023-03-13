package net.frogmouth.rnd.eofff.uncompressed.cpal;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.PropertyTestSupport;
import org.testng.annotations.Test;

/** Unit tests for ComponentPaletteBox. */
public class ComponentPaletteBoxTest extends PropertyTestSupport {
    private static final byte[] CPAL_BYTES =
            new byte[] {
                0x00, 0x00, 0x00, 0x24, 0x63, 0x70, 0x61, 0x6c, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03,
                0x00, 0x01, 0x07, 0x00, 0x00, 0x02, 0x07, 0x00, 0x00, 0x03, 0x07, 0x00, 0x00, 0x00,
                0x00, 0x02, 0x10, 0x20, 0x30, 0x11, 0x21, 0x31
            };

    @Test
    public void checkParse() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(CPAL_BYTES);
        assertTrue(prop instanceof ComponentPaletteBox);
        ComponentPaletteBox cpal = (ComponentPaletteBox) prop;
        assertTrue(cpal.getFourCC().toString().equals("cpal"));
        assertEquals(cpal.getFullName(), "ComponentPaletteBox");
        assertEquals(cpal.getComponents().size(), 3);
        assertEquals(cpal.getComponents().get(0).getComponentIndex(), 1);
        assertEquals(cpal.getComponents().get(1).getComponentIndex(), 2);
        assertEquals(cpal.getComponents().get(2).getComponentIndex(), 3);
        assertEquals(cpal.getComponentValues().length, 2);
        assertEquals(cpal.getComponentValues()[0].length, 3);
        assertEquals(cpal.getComponentValues()[0][0], 16);
        assertEquals(cpal.getComponentValues()[0][1], 32);
        assertEquals(cpal.getComponentValues()[0][2], 48);
        assertEquals(cpal.getComponentValues()[1][0], 17);
        assertEquals(cpal.getComponentValues()[1][1], 33);
        assertEquals(cpal.getComponentValues()[1][2], 49);
        assertEquals(
                cpal.toString(),
                "ComponentPaletteBox 'cpal': {1, 7, 0}, {2, 7, 0}, {3, 7, 0}" + "");
    }

    @Test
    public void checkWrite() throws IOException {
        ComponentPaletteBox box = new ComponentPaletteBox();
        box.addComponent(new PaletteComponent(1, 7, 0));
        box.addComponent(new PaletteComponent(2, 7, 0));
        box.addComponent(new PaletteComponent(3, 7, 0));
        box.setComponentValues(new byte[][] {{0x10, 0x20, 0x30}, {0x11, 0x21, 0x31}});
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, CPAL_BYTES);
    }
}
