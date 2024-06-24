package net.frogmouth.rnd.eofff.imagefileformat.properties.pixi;

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

    private static final byte[] VERSION1_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x17,
                0x70,
                0x69,
                0x78,
                0x69,
                0x01,
                0x00,
                0x00,
                0x00,
                0x03,
                0x09,
                0x08,
                0x0a,
                0b00000100,
                0x72,
                0x65,
                0x64,
                0x00,
                0b00000000,
                0b00000000
            };

    private static final byte[] VERSION1_ALPHA_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x2A,
                0x70,
                0x69,
                0x78,
                0x69,
                0x01,
                0x00,
                0x00,
                0x03,
                0x04,
                0x08,
                0x08,
                0x08,
                0x08,
                0b00000100,
                0x72,
                0x65,
                0x64,
                0x00,
                0b00000100,
                0x67,
                0x72,
                0x65,
                0x65,
                0x6e,
                0x00,
                0b00000100,
                0x62,
                0x6c,
                0x75,
                0x65,
                0x00,
                0b00100100,
                0x61,
                0x6c,
                0x70,
                0x68,
                0x61,
                0x00,
            };

    private static final byte[] VERSION1_DEPTH_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x2A,
                0x70,
                0x69,
                0x78,
                0x69,
                0x01,
                0x00,
                0x00,
                0x00,
                0x04,
                0x08,
                0x08,
                0x08,
                0x08,
                0b00000100,
                0x72,
                0x65,
                0x64,
                0x00,
                0b00000100,
                0x67,
                0x72,
                0x65,
                0x65,
                0x6e,
                0x00,
                0b00000100,
                0x62,
                0x6c,
                0x75,
                0x65,
                0x00,
                0b01010100,
                0x64,
                0x65,
                0x70,
                0x74,
                0x68,
                0x00,
            };

    public PixelInformationPropertyParserTest() {}

    @Test
    public void version0Parse() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(VERSION0_BYTES);
        assertTrue(prop instanceof PixelInformationProperty);
        PixelInformationProperty pixi = (PixelInformationProperty) prop;
        assertEquals(pixi.getVersion(), 0);
        assertEquals(pixi.getChannels().size(), 3);
        assertEquals(pixi.getChannels().get(0), 9);
        assertEquals(pixi.getChannels().get(1), 8);
        assertEquals(pixi.getChannels().get(2), 10);
        assertEquals(pixi.toString(0), "PixelInformationProperty 'pixi': '[9, 8, 10, ]");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        pixi.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, VERSION0_BYTES);
    }

    @Test
    public void version1Parse() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(VERSION1_BYTES);
        assertTrue(prop instanceof PixelInformationProperty);
        PixelInformationProperty pixi = (PixelInformationProperty) prop;
        assertEquals(pixi.getVersion(), 1);
        assertEquals(pixi.getChannels().size(), 3);
        assertEquals(pixi.getChannels().get(0), 9);
        assertEquals(pixi.getChannels().get(1), 8);
        assertEquals(pixi.getChannels().get(2), 10);
        assertEquals(
                pixi.toString(0),
                """
                PixelInformationProperty 'pixi': '[9, 8, 10, ], has_alpha=false, alpha_premultiplied=false, has_subsampling=false, channels:
                    channel_idc=0 (colour/grayscale), channel_data_type=0 (unsigned integer), channel_label_present=true, channel_label=red
                    channel_idc=0 (colour/grayscale), channel_data_type=0 (unsigned integer), channel_label_present=false
                    channel_idc=0 (colour/grayscale), channel_data_type=0 (unsigned integer), channel_label_present=false""");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        pixi.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, VERSION1_BYTES);
    }

    @Test
    public void version1AlphaParse() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(VERSION1_ALPHA_BYTES);
        assertTrue(prop instanceof PixelInformationProperty);
        PixelInformationProperty pixi = (PixelInformationProperty) prop;
        assertEquals(pixi.getVersion(), 1);
        assertEquals(pixi.getChannels().size(), 4);
        assertEquals(pixi.getChannels().get(0), 8);
        assertEquals(pixi.getChannels().get(1), 8);
        assertEquals(pixi.getChannels().get(2), 8);
        assertEquals(pixi.getChannels().get(3), 8);
        assertEquals(
                pixi.toString(0),
                """
                PixelInformationProperty 'pixi': '[8, 8, 8, 8, ], has_alpha=true, alpha_premultiplied=true, has_subsampling=false, channels:
                    channel_idc=0 (colour/grayscale), channel_data_type=0 (unsigned integer), channel_label_present=true, channel_label=red
                    channel_idc=0 (colour/grayscale), channel_data_type=0 (unsigned integer), channel_label_present=true, channel_label=green
                    channel_idc=0 (colour/grayscale), channel_data_type=0 (unsigned integer), channel_label_present=true, channel_label=blue
                    channel_idc=1 (alpha), channel_data_type=0 (unsigned integer), channel_label_present=true, channel_label=alpha""");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        pixi.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, VERSION1_ALPHA_BYTES);
    }

    @Test
    public void version1DepthParse() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(VERSION1_DEPTH_BYTES);
        assertTrue(prop instanceof PixelInformationProperty);
        PixelInformationProperty pixi = (PixelInformationProperty) prop;
        assertEquals(pixi.getVersion(), 1);
        assertEquals(pixi.getChannels().size(), 4);
        assertEquals(pixi.getChannels().get(0), 8);
        assertEquals(pixi.getChannels().get(1), 8);
        assertEquals(pixi.getChannels().get(2), 8);
        assertEquals(pixi.getChannels().get(3), 8);
        assertEquals(
                pixi.toString(0),
                """
                PixelInformationProperty 'pixi': '[8, 8, 8, 8, ], has_alpha=false, alpha_premultiplied=false, has_subsampling=false, channels:
                    channel_idc=0 (colour/grayscale), channel_data_type=0 (unsigned integer), channel_label_present=true, channel_label=red
                    channel_idc=0 (colour/grayscale), channel_data_type=0 (unsigned integer), channel_label_present=true, channel_label=green
                    channel_idc=0 (colour/grayscale), channel_data_type=0 (unsigned integer), channel_label_present=true, channel_label=blue
                    channel_idc=2 (depth), channel_data_type=2 (floating point), channel_label_present=true, channel_label=depth""");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        pixi.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, VERSION1_DEPTH_BYTES);
    }
}
