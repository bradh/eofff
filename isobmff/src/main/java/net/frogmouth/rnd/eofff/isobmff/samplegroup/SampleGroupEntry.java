package net.frogmouth.rnd.eofff.isobmff.samplegroup;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Interface for Sample Group Description Entry implementation.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.9.3.
 */
public interface SampleGroupEntry {
    public FourCC getGroupingType();

    public abstract String getFullName();

    public abstract String toString(int nestingLevel);
}
