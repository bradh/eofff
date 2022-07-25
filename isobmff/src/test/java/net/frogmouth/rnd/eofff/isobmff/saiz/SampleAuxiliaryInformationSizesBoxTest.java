package net.frogmouth.rnd.eofff.isobmff.saiz;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import org.testng.annotations.Test;

/** Unit test for saiz. */
public class SampleAuxiliaryInformationSizesBoxTest {
    @Test
    public void checkWrite() throws IOException {
        SampleAuxiliaryInformationSizesBox box =
                new SampleAuxiliaryInformationSizesBoxBuilder()
                        .withVersion(0)
                        .withFlags(1)
                        .withAuxInfoType(new FourCC("abcd"))
                        .withAuxInfoTypeParameter(0)
                        .withDefaultSampleInfoSize(8)
                        .withSampleCount(5 * 60 * 30)
                        .build();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        box.writeTo(baos);
        byte[] bytes = baos.toByteArray();
        assertEquals(
                bytes,
                new byte[] {
                    0x00, 0x00, 0x00, 0x14, 0x73, 0x61, 0x69, 0x7A, 0x00, 0x00, 0x00, 0x01, 0x61,
                    0x62, 0x63, 0x64, 0x00, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x23, 0x28
                });
        File testFile = new File("saiz.bin");
        Files.write(testFile.toPath(), bytes, StandardOpenOption.CREATE);
    }

    @Test
    public void checkWriteMisb() throws IOException {
        SampleAuxiliaryInformationSizesBox box =
                new SampleAuxiliaryInformationSizesBoxBuilder()
                        .withVersion(0)
                        .withFlags(1)
                        .withAuxInfoType(new FourCC("misb"))
                        .withAuxInfoTypeParameter(0)
                        .withURI("urn:misb.KLV.ul:060E2B34.02050101.0E010505.00000000")
                        .withDefaultSampleInfoSize(8)
                        .withSampleCount(5 * 60 * 30)
                        .build();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        box.writeTo(baos);
        byte[] bytes = baos.toByteArray();
        assertEquals(
                bytes,
                new byte[] {
                    0x00, 0x00, 0x00, 0x4C, 0x73, 0x61, 0x69, 0x7A, 0x00, 0x00, 0x00, 0x01, 0x6D,
                    0x69, 0x73, 0x62, 0x00, 0x00, 0x00, 0x00, 0x75, 0x72, 0x6e, 0x3a, 0x6d, 0x69,
                    0x73, 0x62, 0x2e, 0x4b, 0x4c, 0x56, 0x2e, 0x75, 0x6c, 0x3a, 0x30, 0x36, 0x30,
                    0x45, 0x32, 0x42, 0x33, 0x34, 0x2e, 0x30, 0x32, 0x30, 0x35, 0x30, 0x31, 0x30,
                    0x31, 0x2e, 0x30, 0x45, 0x30, 0x31, 0x30, 0x35, 0x30, 0x35, 0x2e, 0x30, 0x30,
                    0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x00, 0x08, 0x00, 0x00, 0x23, 0x28
                });
        File testFile = new File("saiz_misb.bin");
        Files.write(testFile.toPath(), bytes, StandardOpenOption.CREATE);
    }
}
