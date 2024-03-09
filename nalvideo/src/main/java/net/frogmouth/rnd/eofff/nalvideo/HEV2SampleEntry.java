package net.frogmouth.rnd.eofff.nalvideo;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntry;

/**
 * HEVC Sample Entry.
 *
 * <p>See ISO/IEC 14496-15:2022 Section 9.5.3.
 */
public class HEV2SampleEntry extends VisualSampleEntry {

    public static final FourCC HEV2_ATOM = new FourCC("hev2");

    public HEV2SampleEntry() {
        super(HEV2_ATOM);
    }

    @Override
    public String getFullName() {
        return "HEVCSampleEntry";
    }
}
