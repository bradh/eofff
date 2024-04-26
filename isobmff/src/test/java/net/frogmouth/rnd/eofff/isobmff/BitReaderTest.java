package net.frogmouth.rnd.eofff.isobmff;

import static org.testng.Assert.*;

import java.io.IOException;
import org.testng.annotations.Test;

/** Unit test for DataReferenceBox. */
public class BitReaderTest {
    private static final byte[] SOURCE_BYTES = new byte[] {(byte) 0xC7, (byte) 0xD4, 0x0C};

    @Test
    public void checkReaderBase() throws IOException {
        BitReader uut = new BitReader(new byte[] {0b01000000});
        assertTrue(uut.hasRemaining());
        assertEquals(uut.readBits(1), 0);
        assertEquals(uut.readBits(1), 1);
        assertTrue(uut.hasRemaining());
        assertEquals(uut.readBits(6), 0);
        assertFalse(uut.hasRemaining());
    }

    @Test
    public void checkReader() throws IOException {
        BitReader uut = new BitReader(SOURCE_BYTES);
        assertTrue(uut.hasRemaining());
        assertEquals(uut.readBits(1), 1);
        assertEquals(uut.readBits(1), 1);
        assertEquals(uut.readBits(4), 0b0001);
        assertEquals(uut.readBits(9), 0b111101010);
        assertEquals(uut.readBits(1), 0);
        assertTrue(uut.hasRemaining());
        assertEquals(uut.readBits(7), 0b0000110);
        assertTrue(uut.hasRemaining());
        assertEquals(uut.readBits(1), 0);
        assertFalse(uut.hasRemaining());
    }
}
