package net.frogmouth.rnd.eofff.nalvideo;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntry;

/**
 * HEVC Sample Entry.
 *
 * <p>See ISO/IEC 14496-15:2022 Section 9.5.3.
 */
public class HVC2SampleEntry extends VisualSampleEntry {

    public static final FourCC HVC2_ATOM = new FourCC("hvc2");

    public HVC2SampleEntry() {
        super(HVC2_ATOM);
    }

    @Override
    public String getFullName() {
        return "HEVCSampleEntry";
    }
}
