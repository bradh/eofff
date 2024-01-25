package net.frogmouth.rnd.eofff.isobmff.stsd;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/*
 * TODO: This need to have the MetadataSampleEntry and SampleEntry stuff factored out
 */
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
