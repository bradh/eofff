package net.frogmouth.rnd.eofff.nalvideo;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntry;

/**
 * AVC video stream Sample Entry.
 *
 * <p>See ISO/IEC 14496-15:2022 Section 5.4.2
 */
public class AVC1SampleEntry extends VisualSampleEntry {

    public static final FourCC AVC1_ATOM = new FourCC("avc1");

    public AVC1SampleEntry() {
        super(AVC1_ATOM);
    }

    @Override
    public String getFullName() {
        return "AVCSampleEntry";
    }
}
