package net.frogmouth.rnd.eofff.uncompressed.uncc;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.PropertyTestSupport;
import org.testng.annotations.Test;

/** Unit tests for UncompressedFrameConfigBox. */
public class UncompressedFrameConfigBoxTest extends PropertyTestSupport {
    private static final byte[] UNCC_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x36,
                0x75,
                0x6e,
                0x63,
                0x43,
                0x00,
                0x00,
                0x00,
                0x00,
                0x72,
                0x67,
                0x62,
                0x33,
                0x00,
                0x03,
                0x00,
                0x00,
                0x07,
                0x00,
                0x00,
                0x00,
                0x01,
                0x07,
                0x00,
                0x00,
                0x00,
                0x02,
                0x07,
                0x00,
                0x00,
                0x02,
                0x01,
                0x00,
                (byte) 0b11110000,
                0x03,
                0x00,
                0x00,
                0x00,
                0x05,
                0x00,
                0x00,
                0x00,
                0x07,
                0x00,
                0x00,
                0x00,
                (byte) 0xff,
                0x00,
                0x00,
                0x00,
                0x7f,
            };

    @Test
    public void checkParse() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(UNCC_BYTES);
        assertTrue(prop instanceof UncompressedFrameConfigBox);
        UncompressedFrameConfigBox uncc = (UncompressedFrameConfigBox) prop;
        assertTrue(uncc.getFourCC().toString().equals("uncC"));
        assertEquals(uncc.getFullName(), "UncompressedFrameConfigBox");
        assertEquals(uncc.getProfile(), new FourCC("rgb3"));
        assertEquals(uncc.getComponents().size(), 3);
        assertEquals(uncc.getComponents().get(0).getComponentIndex(), 0);
        assertEquals(uncc.getComponents().get(1).getComponentIndex(), 1);
        assertEquals(uncc.getComponents().get(2).getComponentIndex(), 2);
        assertEquals(uncc.getSamplingType(), SamplingType.YCbCr420);
        assertEquals(uncc.getInterleaveType(), Interleaving.Pixel);
        assertEquals(uncc.getBlockSize(), 0);
        assertTrue(uncc.isComponentLittleEndian());
        assertTrue(uncc.isBlockPadLSB());
        assertTrue(uncc.isBlockLittleEndian());
        assertTrue(uncc.isBlockReversed());
        assertFalse(uncc.isPadUnknown());
        assertEquals(uncc.getPixelSize(), 3);
        assertEquals(uncc.getRowAlignSize(), 5);
        assertEquals(uncc.getTileAlignSize(), 7);
        assertEquals(uncc.getNumTileColumnsMinusOne(), 255);
        assertEquals(uncc.getNumTileRowsMinusOne(), 127);
        assertEquals(
                uncc.toString(),
                "UncompressedFrameConfigBox 'uncC': profile=rgb3, Component{componentIndex=0, componentBitDepthMinusOne=7, componentFormat=UnsignedInteger, componentAlignSize=0},Component{componentIndex=1, componentBitDepthMinusOne=7, componentFormat=UnsignedInteger, componentAlignSize=0},Component{componentIndex=2, componentBitDepthMinusOne=7, componentFormat=UnsignedInteger, componentAlignSize=0}, sampling_type=YCbCr420, interleaveType=Pixel, blockSize=0, component_little_endian=true, block_pad_LSB=true, block_little_endian=true, block_reversed=true, pad_unknown=false, pixel_size=3, row_align_size=5, tile_align_size=7, num_tile_cols_minus_one=255, num_tile_rows_minus_one=127");
    }

    @Test
    public void checkWrite() throws IOException {
        UncompressedFrameConfigBox box = new UncompressedFrameConfigBox();
        box.setProfile(new FourCC("rgb3"));
        box.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
        box.addComponent(new Component(1, 7, ComponentFormat.UnsignedInteger, 0));
        box.addComponent(new Component(2, 7, ComponentFormat.UnsignedInteger, 0));
        box.setSamplingType(SamplingType.YCbCr420);
        box.setInterleaveType(Interleaving.Pixel);
        box.setBlockSize(0);
        box.setComponentLittleEndian(true);
        box.setBlockPadLSB(true);
        box.setBlockLittleEndian(true);
        box.setBlockReversed(true);
        box.setPadUnknown(false);
        box.setPixelSize(3);
        box.setRowAlignSize(5);
        box.setTileAlignSize(7);
        box.setNumTileColumnsMinusOne(255);
        box.setNumTileRowsMinusOne(127);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, UNCC_BYTES);
    }
}
