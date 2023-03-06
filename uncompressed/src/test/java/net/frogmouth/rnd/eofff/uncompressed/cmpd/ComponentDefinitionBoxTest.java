package net.frogmouth.rnd.eofff.uncompressed.cmpd;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

/** Unit tests for ComponentDefinitionBox. */
public class ComponentDefinitionBoxTest extends PropertyTestSupport {
    private static final byte[] CMPD_BYTES_NO_STRING =
            new byte[] {
                0x00, 0x00, 0x00, 0x10, 0x63, 0x6d, 0x70, 0x64, 0x00, 0x03, 0x00, 0x04, 0x00, 0x06,
                0x00, 0x05
            };

    @Test
    public void checkParseNoString() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(CMPD_BYTES_NO_STRING);
        assertTrue(prop instanceof ComponentDefinitionBox);
        ComponentDefinitionBox cmpd = (ComponentDefinitionBox) prop;
        assertTrue(cmpd.getFourCC().toString().equals("cmpd"));
        assertEquals(cmpd.getComponentDefinitions().size(), 3);
        ComponentDefinition defn0 = cmpd.getComponentDefinitions().get(0);
        assertEquals(defn0.getComponentType(), 4);
        assertNull(defn0.getComponentTypeUri());
        ComponentDefinition defn1 = cmpd.getComponentDefinitions().get(1);
        assertEquals(defn1.getComponentType(), 6);
        assertNull(defn1.getComponentTypeUri());
        ComponentDefinition defn2 = cmpd.getComponentDefinitions().get(2);
        assertEquals(defn2.getComponentType(), 5);
        assertNull(defn2.getComponentTypeUri());
    }

    @Test
    public void checkWrite() throws IOException {
        ComponentDefinitionBox box = new ComponentDefinitionBox();
        box.addComponentDefinition(new ComponentDefinition(4, null));
        box.addComponentDefinition(new ComponentDefinition(6, null));
        box.addComponentDefinition(new ComponentDefinition(5, null));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, CMPD_BYTES_NO_STRING);
    }
}
