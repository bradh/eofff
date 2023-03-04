package net.frogmouth.rnd.eofff.uncompressed.uncc;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.ByteArrayParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

/** Unit tests for UncompressedFrameConfigBox. */
public class UncompressedFrameConfigBoxTest {
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
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(UNCC_BYTES);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof UncompressedFrameConfigBox);
        UncompressedFrameConfigBox uncc = (UncompressedFrameConfigBox) box;
        assertTrue(uncc.getFourCC().toString().equals("uncC"));
        assertEquals(uncc.getProfile(), new FourCC("rgb3"));
        assertEquals(uncc.getComponents().size(), 3);
        assertEquals(uncc.getComponents().get(0).getComponentIndex(), 0);
        assertEquals(uncc.getComponents().get(1).getComponentIndex(), 1);
        assertEquals(uncc.getComponents().get(2).getComponentIndex(), 2);
        assertEquals(uncc.getSamplingType(), 2);
        assertEquals(uncc.getInterleaveType(), 1);
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
    }

    @Test
    public void checkWrite() throws IOException {
        UncompressedFrameConfigBox box = new UncompressedFrameConfigBox();
        box.setProfile(new FourCC("rgb3"));
        box.addComponent(new Component(0, 7, 0, 0));
        box.addComponent(new Component(1, 7, 0, 0));
        box.addComponent(new Component(2, 7, 0, 0));
        box.setSamplingType(2);
        box.setInterleaveType(1);
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
