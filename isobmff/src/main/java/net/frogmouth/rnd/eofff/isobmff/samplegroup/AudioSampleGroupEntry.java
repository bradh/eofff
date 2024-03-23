package net.frogmouth.rnd.eofff.isobmff.samplegroup;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Base class for Audio Sample Group Description Entry.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.9.3.
 */
public abstract class AudioSampleGroupEntry extends SampleGroupDescriptionEntry
        implements SampleGroupEntry {

    public AudioSampleGroupEntry(FourCC grouping_type, String fullName) {
        super(grouping_type, fullName);
    }
}
