package net.frogmouth.rnd.eofff.isobmff.stsd;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public abstract class MetaDataSampleEntry extends SampleEntry {

    public MetaDataSampleEntry(FourCC codingname) {
        super(codingname);
    }
}
