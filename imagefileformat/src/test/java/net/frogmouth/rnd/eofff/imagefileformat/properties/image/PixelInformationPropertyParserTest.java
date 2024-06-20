package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.frogmouth.rnd.eofff.imagefileformat.PropertyTestSupport;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import org.testng.annotations.Test;

public class PixelInformationPropertyParserTest extends PropertyTestSupport {
    private static final byte[] VERSION0_BYTES =
            new byte[] {
                0x00, 0x00, 0x00, 0x10, 0x70, 0x69, 0x78, 0x69,
                0x00, 0x00, 0x00, 0x00, 0x03, 0x09, 0x08, 0x0a
            };

    public PixelInformationPropertyParserTest() {}

    @Test
    public void version0Parse() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(VERSION0_BYTES);
        assertTrue(prop instanceof PixelInformationProperty);
        PixelInformationProperty pixi = (PixelInformationProperty) prop;
        assertEquals(pixi.getChannels().size(), 3);
        assertEquals(pixi.getChannels().get(0), 9);
        assertEquals(pixi.getChannels().get(1), 8);
        assertEquals(pixi.getChannels().get(2), 10);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        pixi.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, VERSION0_BYTES);
    }
}
