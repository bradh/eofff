package net.frogmouth.rnd.eofff.uncompressed.sbpm;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.PropertyTestSupport;
import org.testng.annotations.Test;

/** Unit tests for SensorBadPixelsMapBox. */
public class SensorBadPixelsMapBoxTest extends PropertyTestSupport {
    private static final byte[] SBPM_BYTES =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x5d,
                0x73,
                0x62,
                0x70,
                0x6d,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                0x03,
                0x00,
                0x00,
                0x00,
                0x01,
                0x00,
                0x02,
                (byte) 0x80,
                0x00,
                0x00,
                0x00,
                0x02,
                0x00,
                0x00,
                0x00,
                0x03,
                0x00,
                0x00,
                0x00,
                0x05,
                0x00,
                0x00,
                0x00,
                0x08,
                0x00,
                0x00,
                0x01,
                0x04,
                0x00,
                0x00,
                0x02,
                0x01,
                0x00,
                0x00,
                0x00,
                0x40,
                0x00,
                0x00,
                0x03,
                (byte) 0xFF,
                0x00,
                0x00,
                0x00,
                0x03,
                0x00,
                0x00,
                0x00,
                0x10,
                0x00,
                0x00,
                0x01,
                0x03,
                0x00,
                0x00,
                0x01,
                0x10,
                0x00,
                0x00,
                0x02,
                0x03,
                0x00,
                0x00,
                0x02,
                0x10,
                0x00,
                0x00,
                0x03,
                0x03,
                0x00,
                0x00,
                0x03,
                0x10,
                0x00,
                0x00,
                0x04,
                0x03,
                0x00,
                0x00,
                0x04,
                0x10,
            };

    private static final byte[] SBPM_BYTES_NOT_CORRECTED =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x5d,
                0x73,
                0x62,
                0x70,
                0x6d,
                0x00,
                0x00,
                0x00,
                0x00,
                0x00,
                0x03,
                0x00,
                0x00,
                0x00,
                0x01,
                0x00,
                0x02,
                (byte) 0x00,
                0x00,
                0x00,
                0x00,
                0x02,
                0x00,
                0x00,
                0x00,
                0x03,
                0x00,
                0x00,
                0x00,
                0x05,
                0x00,
                0x00,
                0x00,
                0x08,
                0x00,
                0x00,
                0x01,
                0x04,
                0x00,
                0x00,
                0x02,
                0x01,
                0x00,
                0x00,
                0x00,
                0x40,
                0x00,
                0x00,
                0x03,
                (byte) 0xFF,
                0x00,
                0x00,
                0x00,
                0x03,
                0x00,
                0x00,
                0x00,
                0x10,
                0x00,
                0x00,
                0x01,
                0x03,
                0x00,
                0x00,
                0x01,
                0x10,
                0x00,
                0x00,
                0x02,
                0x03,
                0x00,
                0x00,
                0x02,
                0x10,
                0x00,
                0x00,
                0x03,
                0x03,
                0x00,
                0x00,
                0x03,
                0x10,
                0x00,
                0x00,
                0x04,
                0x03,
                0x00,
                0x00,
                0x04,
                0x10,
            };

    @Test
    public void checkParse() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(SBPM_BYTES);
        assertTrue(prop instanceof SensorBadPixelsMapBox);
        SensorBadPixelsMapBox sbpm = (SensorBadPixelsMapBox) prop;
        assertEquals(sbpm.getFullName(), "SensorBadPixelsMap");
        assertTrue(sbpm.getFourCC().toString().equals("sbpm"));
        assertTrue(sbpm.isCorrectionApplied());
        assertEquals(sbpm.getComponentIndexes().size(), 3);
        assertEquals(sbpm.getComponentIndexes().get(0), 0);
        assertEquals(sbpm.getComponentIndexes().get(1), 1);
        assertEquals(sbpm.getComponentIndexes().get(2), 2);
        assertEquals(sbpm.getBadRows().size(), 2);
        assertEquals(sbpm.getBadColumns().size(), 3);
        assertEquals(sbpm.getBadPixels().size(), 5);
        assertEquals(sbpm.getBadRows().get(0), 8);
        assertEquals(sbpm.getBadRows().get(1), 260);
        assertEquals(sbpm.getBadColumns().get(0), 513);
        assertEquals(sbpm.getBadColumns().get(1), 64);
        assertEquals(sbpm.getBadColumns().get(2), 1023);
        assertEquals(sbpm.getBadPixels().get(0), new PixelCoordinate(3, 16));
        assertEquals(sbpm.getBadPixels().get(1), new PixelCoordinate(259, 272));
        assertEquals(sbpm.getBadPixels().get(2), new PixelCoordinate(515, 528));
        assertEquals(sbpm.getBadPixels().get(3), new PixelCoordinate(771, 784));
        assertEquals(sbpm.getBadPixels().get(4), new PixelCoordinate(1027, 1040));
        assertEquals(
                sbpm.toString(),
                "SensorBadPixelsMap 'sbpm': corrections applied: true, bad rows: [8, 260], bad columns: [513, 64, 1023], bad pixels: [(3, 16), (259, 272), (515, 528), (771, 784), (1027, 1040)]");
    }

    @Test
    public void checkParseNoCorrections() throws IOException {
        AbstractItemProperty prop = parseBytesToSingleProperty(SBPM_BYTES_NOT_CORRECTED);
        assertTrue(prop instanceof SensorBadPixelsMapBox);
        SensorBadPixelsMapBox sbpm = (SensorBadPixelsMapBox) prop;
        assertEquals(sbpm.getFullName(), "SensorBadPixelsMap");
        assertTrue(sbpm.getFourCC().toString().equals("sbpm"));
        assertFalse(sbpm.isCorrectionApplied());
        assertEquals(sbpm.getComponentIndexes().size(), 3);
        assertEquals(sbpm.getComponentIndexes().get(0), 0);
        assertEquals(sbpm.getComponentIndexes().get(1), 1);
        assertEquals(sbpm.getComponentIndexes().get(2), 2);
        assertEquals(sbpm.getBadRows().size(), 2);
        assertEquals(sbpm.getBadColumns().size(), 3);
        assertEquals(sbpm.getBadPixels().size(), 5);
        assertEquals(sbpm.getBadRows().get(0), 8);
        assertEquals(sbpm.getBadRows().get(1), 260);
        assertEquals(sbpm.getBadColumns().get(0), 513);
        assertEquals(sbpm.getBadColumns().get(1), 64);
        assertEquals(sbpm.getBadColumns().get(2), 1023);
        assertEquals(sbpm.getBadPixels().get(0), new PixelCoordinate(3, 16));
        assertEquals(sbpm.getBadPixels().get(1), new PixelCoordinate(259, 272));
        assertEquals(sbpm.getBadPixels().get(2), new PixelCoordinate(515, 528));
        assertEquals(sbpm.getBadPixels().get(3), new PixelCoordinate(771, 784));
        assertEquals(sbpm.getBadPixels().get(4), new PixelCoordinate(1027, 1040));
        assertEquals(
                sbpm.toString(),
                "SensorBadPixelsMap 'sbpm': corrections applied: false, bad rows: [8, 260], bad columns: [513, 64, 1023], bad pixels: [(3, 16), (259, 272), (515, 528), (771, 784), (1027, 1040)]");
    }

    @Test
    public void checkWrite() throws IOException {
        SensorBadPixelsMapBox box = new SensorBadPixelsMapBox();
        assertEquals(box.getFullName(), "SensorBadPixelsMap");
        box.addComponentIndex(0);
        box.addComponentIndex(1);
        box.addComponentIndex(2);
        box.setCorrectionApplied(true);
        box.addBadRow(8);
        box.addBadRow(260);
        box.addBadColumn(513);
        box.addBadColumn(64);
        box.addBadColumn(1023);
        box.addBadPixel(new PixelCoordinate(3, 16));
        box.addBadPixel(new PixelCoordinate(259, 272));
        box.addBadPixel(new PixelCoordinate(515, 528));
        box.addBadPixel(new PixelCoordinate(771, 784));
        box.addBadPixel(new PixelCoordinate(1027, 1040));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, SBPM_BYTES);
    }

    @Test
    public void checkWriteNoCorrections() throws IOException {
        SensorBadPixelsMapBox box = new SensorBadPixelsMapBox();
        assertEquals(box.getFullName(), "SensorBadPixelsMap");
        box.addComponentIndex(0);
        box.addComponentIndex(1);
        box.addComponentIndex(2);
        box.setCorrectionApplied(false);
        box.addBadRow(8);
        box.addBadRow(260);
        box.addBadColumn(513);
        box.addBadColumn(64);
        box.addBadColumn(1023);
        box.addBadPixel(new PixelCoordinate(3, 16));
        box.addBadPixel(new PixelCoordinate(259, 272));
        box.addBadPixel(new PixelCoordinate(515, 528));
        box.addBadPixel(new PixelCoordinate(771, 784));
        box.addBadPixel(new PixelCoordinate(1027, 1040));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, SBPM_BYTES_NOT_CORRECTED);
    }
}
