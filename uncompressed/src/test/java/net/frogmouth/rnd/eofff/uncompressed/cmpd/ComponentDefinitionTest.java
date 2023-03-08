package net.frogmouth.rnd.eofff.uncompressed.cmpd;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

/** Unit test for ComponentDefinition within ComponentDefinitionBox. */
public class ComponentDefinitionTest {
    @Test
    public void checkStandardComponent() {
        ComponentDefinition uut = new ComponentDefinition(3, null);
        assertEquals(uut.getComponentType(), 3);
        assertNull(uut.getComponentTypeUri());
        assertEquals(uut.toString(), " component=Chroma component (Cr) (3)");
    }

    @Test
    public void checkStandardComponentMonochrome() {
        ComponentDefinition uut = new ComponentDefinition(0, null);
        assertEquals(uut.getComponentType(), 0);
        assertNull(uut.getComponentTypeUri());
        assertEquals(uut.toString(), " component=Monochrome component (0)");
    }

    @Test
    public void checkStandardComponentCb() {
        ComponentDefinition uut = new ComponentDefinition(2, null);
        assertEquals(uut.getComponentType(), 2);
        assertNull(uut.getComponentTypeUri());
        assertEquals(uut.toString(), " component=Chroma component (Cb) (2)");
    }

    @Test
    public void checkStandardComponentAlpha() {
        ComponentDefinition uut = new ComponentDefinition(7, null);
        assertEquals(uut.getComponentType(), 7);
        assertNull(uut.getComponentTypeUri());
        assertEquals(uut.toString(), " component=Alpha / transparency component (A) (7)");
    }

    @Test
    public void checkStandardComponentDepth() {
        ComponentDefinition uut = new ComponentDefinition(8, null);
        assertEquals(uut.getComponentType(), 8);
        assertNull(uut.getComponentTypeUri());
        assertEquals(uut.toString(), " component=Depth component (D) (8)");
    }

    @Test
    public void checkStandardComponentDisparity() {
        ComponentDefinition uut = new ComponentDefinition(9, null);
        assertEquals(uut.getComponentType(), 9);
        assertNull(uut.getComponentTypeUri());
        assertEquals(uut.toString(), " component=Disparity component (Disp) (9)");
    }

    @Test
    public void checkStandardComponentPalette() {
        ComponentDefinition uut = new ComponentDefinition(10, null);
        assertEquals(uut.getComponentType(), 10);
        assertNull(uut.getComponentTypeUri());
        assertEquals(uut.toString(), " component=Palette component (P) (10)");
    }

    @Test
    public void checkStandardFilterArray() {
        ComponentDefinition uut = new ComponentDefinition(11, null);
        assertEquals(uut.getComponentType(), 11);
        assertNull(uut.getComponentTypeUri());
        assertEquals(uut.toString(), " component=Filter Array component (FA) (11)");
    }

    @Test
    public void checkStandardPadded() {
        ComponentDefinition uut = new ComponentDefinition(12, null);
        assertEquals(uut.getComponentType(), 12);
        assertNull(uut.getComponentTypeUri());
        assertEquals(uut.toString(), " component=Padded component (12)");
    }

    @Test
    public void checkReservedComponent() {
        ComponentDefinition uut = new ComponentDefinition(0x888, null);
        assertEquals(uut.getComponentType(), 0x888);
        assertNull(uut.getComponentTypeUri());
        assertEquals(uut.toString(), " component=ISO/IEC reserved (2184)");
    }

    @Test
    public void checkUserDefinedComponent0x8000() throws IOException {
        ComponentDefinition uut = new ComponentDefinition(0x8000, "http://example.com/comp3");
        assertEquals(uut.getComponentType(), 32768);
        assertEquals(uut.getComponentTypeUri(), "http://example.com/comp3");
        assertEquals(
                uut.toString(),
                " component=User-defined component (32768), URI:http://example.com/comp3");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(baos);
        uut.writeTo(writer);
        byte[] bytes = baos.toByteArray();
        assertEquals(
                bytes,
                new byte[] {
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
                    0x00
                });
    }
}
