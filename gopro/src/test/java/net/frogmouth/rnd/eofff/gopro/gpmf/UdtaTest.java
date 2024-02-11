package net.frogmouth.rnd.eofff.gopro.gpmf;

import static org.testng.Assert.*;

/** Test for wrapping GPMF in udta */
public class UdtaTest {

    /*
        @Test
        public void checkWrite() throws IOException {
            TrackGroupBox box =
                    new TrackGroupBoxBuilder()
                            .withGroup(new TrackGroupTypeBox(TrackGroupType.MSRC, 32911))
                            .build();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
            box.writeTo(streamWriter);
            byte[] bytes = baos.toByteArray();
            assertEquals(bytes, TRGR_BYTES);
            File testTref = new File("trgr.bin");
            Files.write(testTref.toPath(), bytes, StandardOpenOption.CREATE);
            assertTrue(box.toString().startsWith("TrackGroupBox 'trgr' : group count=1"));
            assertTrue(box.toString().endsWith("group_type=msrc, track_group_id=32911"));
        }
    */
    /*
        @Test
        public void checkParse() throws IOException {
            ByteArrayParser parser = new ByteArrayParser();
            List<Box> boxes = parser.parse(GPMF_BYTES);
            assertEquals(boxes.size(), 1);
            Box box = boxes.get(0);
            assertTrue(box instanceof GPMF);
            GPMF gpmf = (GPMF) box;
            assertTrue(gpmf.getFourCC().toString().equals("GPMF"));
            // TODO: check entries
        }
    */
}
