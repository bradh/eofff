package net.frogmouth.rnd.eofff.isobmff;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HdlrBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HdlrBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemDataBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoBox;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoEntry;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoEntryBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.moov.MovieBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ParseTest {
    private static final Logger LOG = LoggerFactory.getLogger(ParseTest.class);
    private List<Box> boxes;

    public ParseTest() {}

    @BeforeTest
    public void parseFile() throws IOException {
        File file = new File("/home/bradh/meva/2018-03-07.16-55-00.17-00-00.bus.G340.recoded.mp4");
        Path testFile = file.toPath();
        FileParser fileParser = new FileParser();
        boxes = fileParser.parse(testFile);
        for (Box box : boxes) {
            LOG.info(box.toString());
        }
    }

    @Test
    public void checkRightNumberOfBoxes() {
        assertEquals(boxes.size(), 4);
    }

    @Test
    public void checkFtypBox() {
        Box box0 = boxes.get(0);
        assertTrue(box0 instanceof FileTypeBox);
        FileTypeBox ftyp = (FileTypeBox) box0;
        assertEquals(ftyp.getFourCC(), new FourCC("ftyp"));
        assertEquals(ftyp.getMajorBrand(), new FourCC("isom"));
        assertEquals(ftyp.getMinorVersion(), 0x200);
        assertEquals(ftyp.getCompatibleBrands().size(), 4);
        assertEquals(ftyp.getCompatibleBrands().get(0), new FourCC("isom"));
        assertEquals(ftyp.getCompatibleBrands().get(1), new FourCC("iso2"));
        assertEquals(ftyp.getCompatibleBrands().get(2), new FourCC("avc1"));
        assertEquals(ftyp.getCompatibleBrands().get(3), new FourCC("mp41"));
    }

    @Test
    public void test() {
        Box box1 = boxes.get(1);
        assertEquals(box1.getFourCC(), new FourCC("free"));

        Box box2 = boxes.get(2);
        assertEquals(box2.getFourCC(), new FourCC("mdat"));

        Box box3 = boxes.get(3);
        assertEquals(box3.getFourCC(), new FourCC("moov"));
    }

    @Test
    public void writeOut() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Box box : boxes) {
            box.writeTo(baos);
        }
        File testOut = new File("G340_out.mp4");
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
    }

    @Test
    public void hackBoxes() throws IOException {
        MovieBox moov = null;
        for (Box box : boxes) {
            if (box.getFourCC().equals(new FourCC("moov"))) {
                moov = (MovieBox) box;
                break;
            }
        }
        if (moov != null) {
            Box udta = null;
            for (Box box : moov.getNestedBoxes()) {
                if (box.getFourCC().equals(new FourCC("udta"))) {
                    udta = box;
                    break;
                }
            }
            if (udta != null) {
                moov.removeNestedBox(udta);
            }
            HdlrBox hdlr =
                    new HdlrBoxBuilder()
                            .withVersion(0)
                            .withFlags(0)
                            .withHandlerType("meta")
                            .withName("MIMD static metadata")
                            .build();
            ItemInfoEntry infe0 =
                    new ItemInfoEntryBuilder()
                            .withVersion(2)
                            .withItemType("uri ")
                            .withItemUriType("urn:misb.KLV.ul.060EE2B24.02050101.0E010503.00000000")
                            .build();
            ItemInfoBox iinf =
                    new ItemInfoBoxBuilder()
                            .withVersion(0)
                            .withFlags(0)
                            .withItemInfo(infe0)
                            .build();
            // TODO: make something meaningful in MIMD
            ItemDataBox idat =
                    new ItemDataBoxBuilder().withData(new byte[] {0x31, 0x32, 0x33}).build();
            MetaBox metaBox =
                    new MetaBoxBuilder()
                            .withVersion(0)
                            .withFlags(0)
                            .withNesteBox(hdlr)
                            .withNesteBox(iinf)
                            .withNesteBox(idat)
                            .build();
            moov.appendNestedBox(metaBox);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Box box : boxes) {
            box.writeTo(baos);
        }
        File testOut = new File("G340_out_no_udta.mp4");
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
    }
}
