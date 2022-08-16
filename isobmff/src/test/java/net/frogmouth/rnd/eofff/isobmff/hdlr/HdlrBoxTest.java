package net.frogmouth.rnd.eofff.isobmff.hdlr;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

/** Unit test for HdlrBox. */
public class HdlrBoxTest {
    @Test
    public void checkWrite() throws IOException {
        HdlrBox box =
                new HdlrBoxBuilder().withVersion(0).withFlags(0).withHandlerType("meta").build();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(
                bytes,
                new byte[] {
                    0x00, 0x00, 0x00, 0x21, 0x68, 0x64, 0x6c, 0x72, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00, 0x00, 0x6d, 0x65, 0x74, 0x61, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
                });
        File testHdlr = new File("hdlr.bin");
        Files.write(testHdlr.toPath(), bytes, StandardOpenOption.CREATE);
    }
}
