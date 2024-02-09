package net.frogmouth.rnd.eofff.isobmff.saiz;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.ByteArrayParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

/** Unit test for saiz. */
public class SampleAuxiliaryInformationSizesBoxTest {
    private static final byte[] SAIZ_BYTES_MINIMAL =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x11,
                0x73,
                0x61,
                0x69,
                0x7A,
                0x00,
                0x00,
                0x00,
                0x00,
                0x17,
                0x00,
                0x00,
                0x07,
                (byte) 0xd0
            };

    private static final byte[] SAIZ_BYTES_FLAGS =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x19,
                0x73,
                0x61,
                0x69,
                0x7A,
                0x00,
                0x00,
                0x00,
                0x01,
                0x74,
                0x65,
                0x73,
                0x74,
                (byte) 0xFF,
                (byte) 0xEE,
                (byte) 0xDD,
                (byte) 0xAA,
                0x17,
                0x00,
                0x00,
                0x07,
                (byte) 0xd0
            };

    private static final byte[] SAIZ_BYTES_VARIABLE =
            new byte[] {
                0x00,
                0x00,
                0x00,
                0x1c,
                0x73,
                0x61,
                0x69,
                0x7A,
                0x00,
                0x00,
                0x00,
                0x01,
                0x74,
                0x65,
                0x73,
                0x74,
                (byte) 0xFF,
                (byte) 0xEE,
                (byte) 0xDD,
                (byte) 0xAA,
                0x00,
                0x00,
                0x00,
                0x00,
                0x03,
                0x05,
                0x09,
                (byte) 0xFF
            };

    @Test
    public void checkWrite() throws IOException {
        SampleAuxiliaryInformationSizesBox box = new SampleAuxiliaryInformationSizesBox();
        box.setDefaultSampleInfoSize(23);
        box.setSampleCount(2000);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, SAIZ_BYTES_MINIMAL);
        File testFile = new File("saiz.bin");
        Files.write(testFile.toPath(), bytes, StandardOpenOption.CREATE);
    }

    @Test
    public void checkParse() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(SAIZ_BYTES_MINIMAL);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof SampleAuxiliaryInformationSizesBox);
        SampleAuxiliaryInformationSizesBox saiz = (SampleAuxiliaryInformationSizesBox) box;
        assertTrue(saiz.getFourCC().toString().equals("saiz"));
        assertEquals(saiz.getFlags(), 0);
        assertEquals(saiz.getDefaultSampleInfoSize(), 23);
        assertEquals(saiz.getSampleCount(), 2000);
    }

    @Test
    public void checkWriteWithFlags() throws IOException {
        SampleAuxiliaryInformationSizesBox box = new SampleAuxiliaryInformationSizesBox();
        box.setFlags(0x01);
        box.setAuxInfoType(new FourCC("test"));
        box.setAuxInfoTypeParameter(0xFFEEDDAA);
        box.setDefaultSampleInfoSize(23);
        box.setSampleCount(2000);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, SAIZ_BYTES_FLAGS);
        File testFile = new File("saiz.bin");
        Files.write(testFile.toPath(), bytes, StandardOpenOption.CREATE);
    }

    @Test
    public void checkParseWithFlags() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(SAIZ_BYTES_FLAGS);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof SampleAuxiliaryInformationSizesBox);
        SampleAuxiliaryInformationSizesBox saiz = (SampleAuxiliaryInformationSizesBox) box;
        assertTrue(saiz.getFourCC().toString().equals("saiz"));
        assertEquals(saiz.getFlags(), 1);
        assertEquals(saiz.getAuxInfoType(), new FourCC("test"));
        assertEquals(saiz.getAuxInfoTypeParameter(), 0xFFEEDDAAL);
        assertEquals(saiz.getDefaultSampleInfoSize(), 23);
        assertEquals(saiz.getSampleCount(), 2000);
    }

    @Test
    public void checkWriteWithFlagsVariable() throws IOException {
        SampleAuxiliaryInformationSizesBox box = new SampleAuxiliaryInformationSizesBox();
        box.setFlags(0x01);
        box.setAuxInfoType(new FourCC("test"));
        box.setAuxInfoTypeParameter(0xFFEEDDAA);
        box.setDefaultSampleInfoSize(0);
        box.setSampleCount(999); // should be ignored
        box.appendSampleInfoSize(5);
        box.appendSampleInfoSize(9);
        box.appendSampleInfoSize(255);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, SAIZ_BYTES_VARIABLE);
        File testFile = new File("saiz.bin");
        Files.write(testFile.toPath(), bytes, StandardOpenOption.CREATE);
    }

    @Test
    public void checkParseWithFlagsVariable() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(SAIZ_BYTES_VARIABLE);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof SampleAuxiliaryInformationSizesBox);
        SampleAuxiliaryInformationSizesBox saiz = (SampleAuxiliaryInformationSizesBox) box;
        assertTrue(saiz.getFourCC().toString().equals("saiz"));
        assertEquals(saiz.getFlags(), 1);
        assertEquals(saiz.getAuxInfoType(), new FourCC("test"));
        assertEquals(saiz.getAuxInfoTypeParameter(), 0xFFEEDDAAL);
        assertEquals(saiz.getDefaultSampleInfoSize(), 0);
        assertEquals(saiz.getSampleCount(), 3);
        assertEquals(saiz.getSampleInfoSizes().size(), 3);
        assertEquals(saiz.getSampleInfoSizes().get(0), 5);
        assertEquals(saiz.getSampleInfoSizes().get(1), 9);
        assertEquals(saiz.getSampleInfoSizes().get(2), 255);
    }
}
