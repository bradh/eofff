package net.frogmouth.rnd.eofff.isobmff.mebx;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.MetaDataSampleEntry;

/**
 * Boxed Metadata Sample Entry.
 *
 * <p>See ISO/IEC 14496-12 Section 12.9.4.
 */
public class BoxedMetadataSampleEntry extends MetaDataSampleEntry {

    public static final FourCC MEBX_ATOM = new FourCC("mebx");

    public BoxedMetadataSampleEntry() {
        super(MEBX_ATOM);
    }

    @Override
    public String getFullName() {
        return "BoxedMetadataSampleEntry";
    }
}
