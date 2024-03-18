package net.frogmouth.rnd.eofff.isobmff.sampleentry.hint;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.BaseSampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;

/**
 * Hint Sample Entry.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 12.4.4
 */
public abstract class HintSampleEntry extends BaseSampleEntry implements SampleEntry {

    public HintSampleEntry(FourCC format) {
        super(format);
    }
}
