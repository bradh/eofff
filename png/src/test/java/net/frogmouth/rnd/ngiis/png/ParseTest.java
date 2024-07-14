package net.frogmouth.rnd.ngiis.png;

import static org.testng.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import javax.imageio.ImageIO;
import org.testng.annotations.Test;

public class ParseTest {

    private static final byte[] PALETTE_BYTES =
            new byte[] {
                (byte) (byte) 0x89,
                0x50,
                0x4e,
                0x47,
                0x0d,
                0x0a,
                0x1a,
                0x0a,
                0x00,
                0x00,
                0x00,
                0x0d,
                0x49,
                0x48,
                0x44,
                0x52,
                0x00,
                0x00,
                0x01,
                0x00,
                0x00,
                0x00,
                0x01,
                0x00,
                0x01,
                0x03,
                0x00,
                0x00,
                0x00,
                0x66,
                (byte) 0xbc,
                0x3a,
                0x25,
                0x00,
                0x00,
                0x00,
                0x03,
                0x50,
                0x4c,
                0x54,
                0x45,
                (byte) 0xb2,
                (byte) 0xc2,
                (byte) 0x9d,
                0x16,
                (byte) 0xd2,
                0x4d,
                (byte) 0x91,
                0x00,
                0x00,
                0x00,
                0x1f,
                0x49,
                0x44,
                0x41,
                0x54,
                0x68,
                (byte) 0x81,
                (byte) 0xed,
                (byte) 0xc1,
                0x01,
                0x0d,
                0x00,
                0x00,
                0x00,
                (byte) 0xc2,
                (byte) 0xa0,
                (byte) 0xf7,
                0x4f,
                0x6d,
                0x0e,
                0x37,
                (byte) 0xa0,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                (byte) (byte) 0xbe,
                0x0d,
                0x21,
                0x00,
                0x00,
                0x01,
                (byte) 0x9a,
                0x60,
                (byte) (byte) 0xe1,
                (byte) 0xd5,
                0x00,
                0x00,
                0x00,
                0x00,
                0x49,
                0x45,
                0x4e,
                0x44,
                (byte) 0xae,
                0x42,
                0x60,
                (byte) (byte) 0x82
            };

    public ParseTest() {}

    @Test
    public void parseSimple() throws IOException {
        PngByteArrayParser parser = new PngByteArrayParser();
        List<PngChunk> chunks = parser.parse(PALETTE_BYTES);
        assertEquals(chunks.size(), 4);
    }

    @Test
    public void renderSimple() throws IOException {
        PngByteArrayParser parser = new PngByteArrayParser();
        BufferedImage bi = parser.render(PALETTE_BYTES);
        assertNotNull(bi);
        ImageIO.write(bi, "jpg", new File("simple.jpg"));
    }

    @Test
    public void parsePalette2() throws IOException {
        PngFileParser parser = new PngFileParser();
        List<PngChunk> chunks =
                parser.parse(
                        Paths.get(
                                "/home/bradh/testbed20/silvereye/smalltiles_2024-06-29/complex_osm_tile.png"));
        assertEquals(chunks.size(), 4);
    }

    @Test
    public void renderPalette2() throws IOException {
        PngFileParser parser = new PngFileParser();
        BufferedImage bi =
                parser.render(
                        Paths.get(
                                "/home/bradh/testbed20/silvereye/smalltiles_2024-06-29/complex_osm_tile.png"));
        assertNotNull(bi);
        ImageIO.write(bi, "jpg", new File("complex.jpg"));
    }
}
