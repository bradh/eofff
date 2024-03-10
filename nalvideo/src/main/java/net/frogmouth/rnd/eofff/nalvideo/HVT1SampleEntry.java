package net.frogmouth.rnd.eofff.nalvideo;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntry;

/**
 * HEVC Tile Track Sample Entry.
 *
 * <p>See ISO/IEC 14496-15:2022 Section 10.5.2
 */
public class HVT1SampleEntry extends VisualSampleEntry {

    public static final FourCC HVT1_ATOM = new FourCC("hvt1");

    public HVT1SampleEntry() {
        super(HVT1_ATOM);
    }

    @Override
    public String getFullName() {
        return "HEVCTileSampleEntry";
    }
}
