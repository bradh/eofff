package net.frogmouth.rnd.eofff.isobmff.samplegroup;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Base class for Visual Sample Group Description Entry.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.9.3.
 */
public abstract class VisualSampleGroupEntry extends SampleGroupDescriptionEntry
        implements SampleGroupEntry {

    public VisualSampleGroupEntry(FourCC grouping_type, String fullName) {
        super(grouping_type, fullName);
    }
}
