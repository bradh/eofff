package net.frogmouth.rnd.eofff.isobmff.stsd;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.MetaDataSampleEntry;

public class URIMetaSampleEntry extends MetaDataSampleEntry {

    public static final FourCC URIM_ATOM = new FourCC("urim");

    public URIMetaSampleEntry() {
        super(URIM_ATOM);
    }

    @Override
    public String getFullName() {
        return "URIMetadataSampleEntry";
    }
}
