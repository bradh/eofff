package net.frogmouth.rnd.eofff.nalvideo;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntry;

/**
 * AVC video stream Sample Entry.
 *
 * <p>See ISO/IEC 14496-15:2022 Section 5.4.2
 */
public class AVC2SampleEntry extends VisualSampleEntry {

    public static final FourCC AVC2_ATOM = new FourCC("avc2");

    public AVC2SampleEntry() {
        super(AVC2_ATOM);
    }

    @Override
    public String getFullName() {
        return "AVC2SampleEntry";
    }
}
