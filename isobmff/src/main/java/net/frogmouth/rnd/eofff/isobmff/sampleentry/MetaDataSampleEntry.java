package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public abstract class MetaDataSampleEntry extends BaseSampleEntry implements SampleEntry {

    public MetaDataSampleEntry(FourCC codingname) {
        super(codingname);
    }
}
