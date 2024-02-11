package net.frogmouth.rnd.eofff.nalvideo;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntry;

/**
 * HEVC Sample Entry.
 *
 * <p>See ISO/IEC 14496-15 Section 8.4.1.1.
 */
public class HEVCSampleEntry extends VisualSampleEntry {

    public static final FourCC HVC1_ATOM = new FourCC("hvc1");

    public HEVCSampleEntry() {
        super(HVC1_ATOM);
    }

    @Override
    public String getFullName() {
        return "HEVCSampleEntry";
    }

    @Override
    public long getBodySize() {
        return super.getBodySize();
    }
}
