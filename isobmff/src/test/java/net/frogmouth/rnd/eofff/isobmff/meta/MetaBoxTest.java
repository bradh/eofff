package net.frogmouth.rnd.eofff.isobmff.meta;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HdlrBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HdlrBoxBuilder;
import org.testng.annotations.Test;

/** Unit test for MetaBox. */
public class MetaBoxTest {
    @Test
    public void checkWriteNoNested() throws IOException {
        MetaBox box = new MetaBoxBuilder().withVersion(0).withFlags(0).build();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        box.writeTo(baos);
        byte[] bytes = baos.toByteArray();
        assertEquals(
                bytes,
                new byte[] {
                    0x00, 0x00, 0x00, 0x0c, 0x6d, 0x65, 0x74, 0x61, 0x00, 0x00, 0x00, 0x00
                });
        File testMetaEmpty = new File("meta_empty.bin");
        Files.write(testMetaEmpty.toPath(), bytes, StandardOpenOption.CREATE);
    }

    @Test
    public void checkWrite() throws IOException {
        HdlrBox hdlr =
                new HdlrBoxBuilder().withVersion(0).withFlags(0).withHandlerType("meta").build();
        ItemInfoEntry infe0 =
                new ItemInfoEntryBuilder()
                        .withVersion(2)
                        .withItemType("uri ")
                        .withItemUriType("urn:misb.KLV.ul.060EE2B24.02050101.0E010503.00000000")
                        .build();
        ItemInfoBox iinf =
                new ItemInfoBoxBuilder().withVersion(0).withFlags(0).withItemInfo(infe0).build();
        ItemDataBox idat = new ItemDataBoxBuilder().addData(new byte[] {0x31, 0x32, 0x33}).build();
        MetaBox box =
                new MetaBoxBuilder()
                        .withVersion(0)
                        .withFlags(0)
                        .withNestedBox(hdlr)
                        .withNestedBox(iinf)
                        .withNestedBox(idat)
                        .build();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        box.writeTo(baos);
        byte[] bytes = baos.toByteArray();
        /*assertEquals(
                        bytes,
                        new byte[] {
                            0x00, 0x00, 0x00, 0x0c, 0x6d, 0x65, 0x74, 0x61, 0x00, 0x00, 0x00, 0x00
                        });
        */
        File testMeta = new File("meta.bin");
        Files.write(testMeta.toPath(), bytes, StandardOpenOption.CREATE);
    }
}
