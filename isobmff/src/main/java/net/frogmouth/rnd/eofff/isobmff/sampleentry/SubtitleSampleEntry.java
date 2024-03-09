package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Subtitle Sample Entry.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 12.6.3
 */
public abstract class SubtitleSampleEntry extends BaseSampleEntry implements SampleEntry {

    public SubtitleSampleEntry(FourCC format) {
        super(format);
    }
}
