package net.frogmouth.rnd.eofff.isobmff.trun;

import static net.frogmouth.rnd.eofff.isobmff.trun.TrackRunBox.FIRST_SAMPLE_FLAGS_PRESENT_FLAG;
import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.ByteArrayParser;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import org.testng.annotations.Test;

/** Unit test for TrackRunBox (trun). */
public class TrackRunBoxTest {

    private final byte[] TRUN_BYTES = {
        0x00,
        0x00,
        0x00,
        (byte) 0xdc,
        0x74,
        0x72,
        0x75,
        0x6e,
        0x00,
        0x00,
        0x0a,
        0x04,
        0x00,
        0x00,
        0x00,
        0x19,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x07,
        0x7f,
        0x00,
        0x00,
        0x03,
        (byte) 0xe8,
        0x00,
        0x00,
        0x00,
        (byte) 0xe6,
        0x00,
        0x00,
        0x0b,
        (byte) 0xb8,
        0x00,
        0x00,
        0x00,
        (byte) 0xab,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        (byte) 0x87,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x01,
        (byte) 0xc6,
        0x00,
        0x00,
        0x0b,
        (byte) 0xb8,
        0x00,
        0x00,
        0x00,
        (byte) 0xcc,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x67,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x01,
        0x58,
        0x00,
        0x00,
        0x0b,
        (byte) 0xb8,
        0x00,
        0x00,
        0x01,
        0x04,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        (byte) 0x84,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x03,
        (byte) 0xb8,
        0x00,
        0x00,
        0x0b,
        (byte) 0xb8,
        0x00,
        0x00,
        0x00,
        (byte) 0xe9,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x01,
        0x21,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x01,
        0x4b,
        0x00,
        0x00,
        0x0b,
        (byte) 0xb8,
        0x00,
        0x00,
        0x00,
        (byte) 0x8f,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x01,
        0x19,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x01,
        0x21,
        0x00,
        0x00,
        0x0b,
        (byte) 0xb8,
        0x00,
        0x00,
        0x00,
        0x79,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        (byte) 0xfa,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x02,
        0x05,
        0x00,
        0x00,
        0x0b,
        (byte) 0xb8,
        0x00,
        0x00,
        0x00,
        (byte) 0xb8,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        (byte) 0xbf,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x01,
        (byte) 0xda,
        0x00,
        0x00,
        0x0b,
        (byte) 0xb8,
        0x00,
        0x00,
        0x01,
        0x25,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x00,
        0x01,
        0x15,
        0x00,
        0x00,
        0x00,
        0x00
    };

    @Test
    public void checkWrite() throws IOException {
        TrackRunBox box = new TrackRunBox();
        box.setDataOffset(0);
        box.setFirstSampleFlags(0);
        box.setFlags(FIRST_SAMPLE_FLAGS_PRESENT_FLAG);
        box.addSample(new TrackRunSample(0, 1919, 0, 1000));
        box.addSample(new TrackRunSample(0, 230, 0, 3000));
        box.addSample(new TrackRunSample(0, 171, 0, 0));
        box.addSample(new TrackRunSample(0, 135, 0, 0));
        box.addSample(new TrackRunSample(0, 454, 0, 3000));
        box.addSample(new TrackRunSample(0, 204, 0, 0));
        box.addSample(new TrackRunSample(0, 103, 0, 0));
        box.addSample(new TrackRunSample(0, 344, 0, 3000));
        box.addSample(new TrackRunSample(0, 260, 0, 0));
        box.addSample(new TrackRunSample(0, 132, 0, 0));
        box.addSample(new TrackRunSample(0, 952, 0, 3000));
        box.addSample(new TrackRunSample(0, 233, 0, 0));
        box.addSample(new TrackRunSample(0, 289, 0, 0));
        box.addSample(new TrackRunSample(0, 331, 0, 3000));
        box.addSample(new TrackRunSample(0, 143, 0, 0));
        box.addSample(new TrackRunSample(0, 281, 0, 0));
        box.addSample(new TrackRunSample(0, 289, 0, 3000));
        box.addSample(new TrackRunSample(0, 121, 0, 0));
        box.addSample(new TrackRunSample(0, 250, 0, 0));
        box.addSample(new TrackRunSample(0, 517, 0, 3000));
        box.addSample(new TrackRunSample(0, 184, 0, 0));
        box.addSample(new TrackRunSample(0, 191, 0, 0));
        box.addSample(new TrackRunSample(0, 474, 0, 3000));
        box.addSample(new TrackRunSample(0, 293, 0, 0));
        box.addSample(new TrackRunSample(0, 277, 0, 0));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        box.writeTo(streamWriter);
        byte[] bytes = baos.toByteArray();
        assertEquals(bytes, TRUN_BYTES);
        assertTrue(
                box.toString(0)
                        .startsWith(
                                "TrackRunBox 'trun': flags=0x000a04, sample_count=25, data_offset=0, first_sample_flags=0"));
        assertTrue(
                box.toString(0)
                        .endsWith(
                                "sample_duration=0, sample_size=277, sample_flags=0x0, sample_composition_time_offset=0"));
    }

    @Test
    public void checkParse() throws IOException {
        ByteArrayParser parser = new ByteArrayParser();
        List<Box> boxes = parser.parse(TRUN_BYTES);
        assertEquals(boxes.size(), 1);
        Box box = boxes.get(0);
        assertTrue(box instanceof TrackRunBox);
        TrackRunBox trun = (TrackRunBox) box;
        assertTrue(trun.getFourCC().toString().equals("trun"));
        assertEquals(trun.getFullName(), "TrackRunBox");
        assertEquals(trun.getSize(), 220);
        assertEquals(trun.getSampleCount(), 25);
        assertEquals(trun.getDataOffset(), 0);
        assertEquals(trun.getFirstSampleFlags(), 0);
        assertEquals(trun.getSamples().size(), 25);
        assertEquals(trun.getSamples().get(0).sampleDuration(), 0);
        assertEquals(trun.getSamples().get(0).sampleSize(), 1919);
        assertEquals(trun.getSamples().get(0).sampleFlags(), 0);
        assertEquals(trun.getSamples().get(0).sampleCompositionTimeOffset(), 1000);
        assertEquals(trun.getSamples().get(10).sampleDuration(), 0);
        assertEquals(trun.getSamples().get(10).sampleSize(), 952);
        assertEquals(trun.getSamples().get(10).sampleFlags(), 0);
        assertEquals(trun.getSamples().get(10).sampleCompositionTimeOffset(), 3000);
        assertEquals(trun.getSamples().get(24).sampleDuration(), 0);
        assertEquals(trun.getSamples().get(24).sampleSize(), 277);
        assertEquals(trun.getSamples().get(24).sampleFlags(), 0);
        assertEquals(trun.getSamples().get(24).sampleCompositionTimeOffset(), 0);
    }
}
