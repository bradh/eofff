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
                0x00, 0x00, 0x00, 0x12, 0x63, 0x6d, 0x70, 0x64, 0x00, 0x00, 0x00, 0x03, 0x00, 0x04,
                0x00, 0x06, 0x00, 0x05
            };

    private static final byte[] CMPD_BYTES_STRING =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x29,
                0x63,
                0x6d,
                0x70,
                0x64,
                0x00,
                0x00,
                0x00,
                0x02,
                (byte) 0x80,
                0x00,
                0x68,
                0x74,
                0x74,
                0x70,
                0x3a,
                0x2f,
                0x2f,
                0x65,
                0x78,
                0x61,
                0x6d,
                0x70,
                0x6c,
                0x65,
                0x2e,
                0x63,
                0x6f,
                0x6d,
                0x2f,
                0x63,
                0x6f,
                0x6d,
                0x70,
                0x33,
                0x00,
                0x00,
                0x01
            };

    @Test
    public void checkParseNoString() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(CMPD_BYTES_NO_STRING);
        assertTrue(prop instanceof ComponentDefinitionBox);
        ComponentDefinitionBox cmpd = (ComponentDefinitionBox) prop;
        assertTrue(cmpd.getFourCC().toString().equals("cmpd"));
        assertEquals(cmpd.getFullName(), "ComponentDefinitionBox");
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
        assertEquals(
                cmpd.toString(),
                "ComponentDefinitionBox 'cmpd': component=Red component (R) (4), component=Blue component (B) (6), component=Green component (G) (5)");
    }

    @Test
    public void checkWrite() throws IOException {
        ComponentDefinitionBox box = new ComponentDefinitionBox();
        assertEquals(box.getFullName(), "ComponentDefinitionBox");
        box.addComponentDefinition(new ComponentDefinition(4, null));
        box.addComponentDefinition(new ComponentDefinition(6, null));
        box.addComponentDefinition(new ComponentDefinition(5, null));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, CMPD_BYTES_NO_STRING);
    }

    @Test
    public void checkWriteUserDefined() throws IOException {
        ComponentDefinitionBox box = new ComponentDefinitionBox();
        assertEquals(box.getFullName(), "ComponentDefinitionBox");
        box.addComponentDefinition(new ComponentDefinition(0x8000, "http://example.com/comp3"));
        box.addComponentDefinition(new ComponentDefinition(1, null));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(baos);
        box.writeTo(writer);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, CMPD_BYTES_STRING);
    }

    @Test
    public void checkParseString() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(CMPD_BYTES_STRING);
        assertTrue(prop instanceof ComponentDefinitionBox);
        ComponentDefinitionBox cmpd = (ComponentDefinitionBox) prop;
        assertTrue(cmpd.getFourCC().toString().equals("cmpd"));
        assertEquals(cmpd.getFullName(), "ComponentDefinitionBox");
        assertEquals(cmpd.getComponentDefinitions().size(), 2);
        ComponentDefinition defn0 = cmpd.getComponentDefinitions().get(0);
        assertEquals(defn0.getComponentType(), 0x8000);
        assertEquals(defn0.getComponentTypeUri(), "http://example.com/comp3");
        ComponentDefinition defn1 = cmpd.getComponentDefinitions().get(1);
        assertEquals(defn1.getComponentType(), 1);
        assertNull(defn1.getComponentTypeUri());
        assertEquals(
                cmpd.toString(),
                "ComponentDefinitionBox 'cmpd': component=User-defined component (32768), URI:http://example.com/comp3, component=Luma component (Y) (1)");
    }
}
