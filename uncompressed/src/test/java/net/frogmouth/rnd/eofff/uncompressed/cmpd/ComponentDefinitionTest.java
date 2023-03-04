package net.frogmouth.rnd.eofff.uncompressed.cmpd;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

/** Unit test for ComponentDefinition within ComponentDefinitionBox. */
public class ComponentDefinitionTest {
    @Test
    public void checkStandardComponent() {
        ComponentDefinition uut = new ComponentDefinition(3, null);
        assertEquals(uut.getComponentType(), 3);
        assertNull(uut.getComponentTypeUri());
        assertEquals(uut.toString(), "component=Chroma component (Cr) (3)");
    }

    @Test
    public void checkUserDefinedComponent0x8000() {
        ComponentDefinition uut = new ComponentDefinition(0x8000, "http://example.com/comp3");
        assertEquals(uut.getComponentType(), 32768);
        assertEquals(uut.getComponentTypeUri(), "http://example.com/comp3");
        assertEquals(
                uut.toString(),
                "component=User-defined component (32768), URI:http://example.com/comp3");
    }
}
