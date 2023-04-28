package net.frogmouth.rnd.eofff.jpegxl.fileformat;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

/**
 * @author bradh
 */
public class BitsExtractorTest {

    @Test
    public void simple() {
        BitsExtractor uut = new BitsExtractor(new byte[] {(byte) 0x83, (byte) 0x0F, (byte) 0x00});
        assertEquals(uut.readBits(4), 0x03);
        assertEquals(uut.readBits(3), 0x00);
        assertEquals(uut.readBits(2), 0x03);
        assertEquals(uut.readBits(9), 0b000011100);
    }
}
