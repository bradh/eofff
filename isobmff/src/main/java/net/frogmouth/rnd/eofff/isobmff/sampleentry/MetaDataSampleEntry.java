package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Metadata Sample Entry.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 12.3.3.
 */
public abstract class MetaDataSampleEntry extends BaseSampleEntry implements SampleEntry {

    public MetaDataSampleEntry(FourCC codingname) {
        super(codingname);
    }
}
