package net.frogmouth.rnd.eofff.isobmff.ftyp;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

public class FtypBoxTest {
    @Test
    public void checkFullName() {
        FileTypeBox box = new FileTypeBox();
        assertEquals(box.getFullName(), "FileTypeBox");
    }

    @Test
    public void checkWrite() throws IOException {
        FileTypeBox box =
                new FileTypeBoxBuilder()
                        .withMajorBrand(Brand.ISO6)
                        .withMinorVersion(0)
                        .addCompatibleBrand(new Brand("misb"))
                        .build();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(
                bytes,
                new byte[] {
                    0x00, 0x00, 0x00, 0x14, 0x66, 0x74, 0x79, 0x70,
                    0x69, 0x73, 0x6f, 0x36, 0x00, 0x00, 0x00, 0x00,
                    0x6d, 0x69, 0x73, 0x62,
                });
        File testFtyp = new File("ftyp.bin");
        Files.write(testFtyp.toPath(), bytes, StandardOpenOption.CREATE);
    }
}
