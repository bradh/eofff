package net.frogmouth.rnd.eofff.isobmff.sgpd;

/**
 * Sample Group Description Entry.
 *
 * <p>This is not valid by itself - it needs to be subclassed.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.9.3.
 */
public abstract class SampleGroupDescriptionEntry {
    public abstract String toString(int nestingLevel);
}
