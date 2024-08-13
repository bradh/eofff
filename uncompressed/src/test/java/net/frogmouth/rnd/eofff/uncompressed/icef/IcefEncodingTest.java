package net.frogmouth.rnd.eofff.uncompressed.icef;

import static org.testng.Assert.*;

import java.io.IOException;
import org.testng.annotations.Test;

/** Unit tests for GenericallyCompressedUnitsInfo encoding. */
public class IcefEncodingTest {

    @Test
    public void checkMinimum() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(0, 1);
        assertEquals(uut.getEncoding(), 0b00000000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 0 + 1);
    }

    public void checkMinimumOffset() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(1, 1);
        assertEquals(uut.getEncoding(), 0b00100000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 1);
    }

    public void checkMinimumOffset2() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(0, 1);
        uut.update(1, 1);
        assertEquals(uut.getEncoding(), 0b00000000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 0 + 1);
    }

    public void checkMinimumOffset3() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(2, 1);
        uut.update(1, 1);
        assertEquals(uut.getEncoding(), 0b00100000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 1);
    }

    @Test
    public void checkForcedOffset() throws IOException {
        IcefEncoding uut = new IcefEncoding(true);
        uut.update(0, 1);
        assertEquals(uut.getEncoding(), 0b00100000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 1);
    }

    @Test
    public void checkForcedOffset1_1() throws IOException {
        IcefEncoding uut = new IcefEncoding(true);
        uut.update(1, 1);
        assertEquals(uut.getEncoding(), 0b00100000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 1);
    }

    @Test
    public void checkForcedOffset_size255() throws IOException {
        IcefEncoding uut = new IcefEncoding(true);
        uut.update(0, 255);
        assertEquals(uut.getEncoding(), 0b00100000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 1);
    }

    @Test
    public void checkForcedOffset_size256() throws IOException {
        IcefEncoding uut = new IcefEncoding(true);
        uut.update(0, 256);
        assertEquals(uut.getEncoding(), 0b00100100);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 2);
    }

    @Test
    public void checkForcedOffset_size257() throws IOException {
        IcefEncoding uut = new IcefEncoding(true);
        uut.update(0, 257);
        assertEquals(uut.getEncoding(), 0b00100100);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 2);
    }

    @Test
    public void checkForcedOffset_size512() throws IOException {
        IcefEncoding uut = new IcefEncoding(true);
        uut.update(0, 512);
        assertEquals(uut.getEncoding(), 0b00100100);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 2);
    }

    @Test
    public void checkForcedOffset_size65535() throws IOException {
        IcefEncoding uut = new IcefEncoding(true);
        uut.update(0, 65535);
        assertEquals(uut.getEncoding(), 0b00100100);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 2);
    }

    @Test
    public void checkForcedOffset_size65536() throws IOException {
        IcefEncoding uut = new IcefEncoding(true);
        uut.update(0, 65536);
        assertEquals(uut.getEncoding(), 0b00101000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 3);
    }

    @Test
    public void checkForcedOffset_size16777215() throws IOException {
        IcefEncoding uut = new IcefEncoding(true);
        uut.update(0, 16777215);
        assertEquals(uut.getEncoding(), 0b00101000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 3);
    }

    @Test
    public void checkForcedOffset_size16777216() throws IOException {
        IcefEncoding uut = new IcefEncoding(true);
        uut.update(0, 16777216);
        assertEquals(uut.getEncoding(), 0b00101100);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 4);
    }

    @Test
    public void checkForcedOffset_size4294967295() throws IOException {
        IcefEncoding uut = new IcefEncoding(true);
        uut.update(0, 4294967295L);
        assertEquals(uut.getEncoding(), 0b00101100);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 4);
    }

    @Test
    public void checkForcedOffset_size4294967296() throws IOException {
        IcefEncoding uut = new IcefEncoding(true);
        uut.update(0, 4294967296L);
        assertEquals(uut.getEncoding(), 0b00110000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 8);
    }

    @Test
    public void checkOffset1_1() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(1, 1);
        assertEquals(uut.getEncoding(), 0b00100000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 1);
    }

    @Test
    public void check_size255() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(0, 255);
        assertEquals(uut.getEncoding(), 0b00000000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 1);
    }

    @Test
    public void check_size256() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(0, 256);
        assertEquals(uut.getEncoding(), 0b00000100);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2);
    }

    @Test
    public void check_size257() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(0, 257);
        assertEquals(uut.getEncoding(), 0b00000100);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2);
    }

    @Test
    public void check_size512() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(0, 512);
        assertEquals(uut.getEncoding(), 0b00000100);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2);
    }

    @Test
    public void check_size65535() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(0, 65535);
        assertEquals(uut.getEncoding(), 0b00000100);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2);
    }

    @Test
    public void check_size65536() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(0, 65536);
        assertEquals(uut.getEncoding(), 0b00001000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 3);
    }

    @Test
    public void check_size16777215() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(0, 16777215);
        assertEquals(uut.getEncoding(), 0b00001000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 3);
    }

    @Test
    public void check_size16777216() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(0, 16777216);
        assertEquals(uut.getEncoding(), 0b00001100);
        assertEquals(uut.getNumberOfBytesPerEntry(), 4);
    }

    @Test
    public void check_size4294967295() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(0, 4294967295L);
        assertEquals(uut.getEncoding(), 0b00001100);
        assertEquals(uut.getNumberOfBytesPerEntry(), 4);
    }

    @Test
    public void check_size4294967296() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(0, 4294967296L);
        assertEquals(uut.getEncoding(), 0b00010000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 8);
    }

    @Test
    public void check_offset1_size255() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(1, 255);
        assertEquals(uut.getEncoding(), 0b00100000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 1);
    }

    @Test
    public void check_offset256_size255() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(256, 255);
        assertEquals(uut.getEncoding(), 0b00100000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 1);
    }

    @Test
    public void check_offset65535_size255() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(65535, 255);
        assertEquals(uut.getEncoding(), 0b00100000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 2 + 1);
    }

    @Test
    public void check_offset65536_size255() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(65536, 255);
        assertEquals(uut.getEncoding(), 0b01000000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 3 + 1);
    }

    @Test
    public void check_offset16777215_size255() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(16777215, 255);
        assertEquals(uut.getEncoding(), 0b01000000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 3 + 1);
    }

    @Test
    public void check_offset16777216_size255() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(16777216, 255);
        assertEquals(uut.getEncoding(), 0b01100000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 4 + 1);
    }

    @Test
    public void check_offset4294967295_size255() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(4294967295L, 255);
        assertEquals(uut.getEncoding(), 0b01100000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 4 + 1);
    }

    @Test
    public void check_offset4294967296_size255() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(4294967296L, 255);
        assertEquals(uut.getEncoding(), 0b10000000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 8 + 1);
    }

    @Test
    public void check_offset4294967296_size256() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(4294967296L, 256);
        assertEquals(uut.getEncoding(), 0b10000100);
        assertEquals(uut.getNumberOfBytesPerEntry(), 8 + 2);
    }

    public void check_offset4294967296_size65535() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(4294967296L, 65535L);
        assertEquals(uut.getEncoding(), 0b100001000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 8 + 2);
    }

    @Test
    public void check_offset4294967296_size65536() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(4294967296L, 65536L);
        assertEquals(uut.getEncoding(), 0b10001000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 8 + 3);
    }

    @Test
    public void check_offset4294967296_size16777215() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(4294967296L, 16777215L);
        assertEquals(uut.getEncoding(), 0b10001000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 8 + 3);
    }

    @Test
    public void check_offset4294967296_size4294967295() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(4294967296L, 4294967295L);
        assertEquals(uut.getEncoding(), 0b10001100);
        assertEquals(uut.getNumberOfBytesPerEntry(), 8 + 4);
    }

    @Test
    public void check_offset4294967296_size4294967296() throws IOException {
        IcefEncoding uut = new IcefEncoding(false);
        uut.update(4294967296L, 4294967296L);
        assertEquals(uut.getEncoding(), 0b10010000);
        assertEquals(uut.getNumberOfBytesPerEntry(), 8 + 8);
    }
}
