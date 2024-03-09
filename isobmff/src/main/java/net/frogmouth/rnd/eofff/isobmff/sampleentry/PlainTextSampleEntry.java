package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Plain Text Sample Entry.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 12.5.3
 */
public abstract class PlainTextSampleEntry extends BaseSampleEntry implements SampleEntry {

    public PlainTextSampleEntry(FourCC format) {
        super(format);
    }
}
