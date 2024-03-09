package net.frogmouth.rnd.eofff.nalvideo;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntry;

/**
 * HEVC Sample Entry.
 *
 * <p>See ISO/IEC 14496-15:2022 Section 8.4.1.1.
 */
public class HEV1SampleEntry extends VisualSampleEntry {

    public static final FourCC HEV1_ATOM = new FourCC("hev1");

    public HEV1SampleEntry() {
        super(HEV1_ATOM);
    }

    @Override
    public String getFullName() {
        return "HEVCSampleEntry";
    }
}
