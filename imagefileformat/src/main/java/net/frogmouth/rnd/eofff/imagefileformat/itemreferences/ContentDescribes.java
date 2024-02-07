package net.frogmouth.rnd.eofff.imagefileformat.itemreferences;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Content describes item reference.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 8.2.1.
 */
public class ContentDescribes extends SingleItemReferenceBox {
    public static final FourCC CDSC_ATOM = new FourCC("cdsc");

    public ContentDescribes() {
        super(CDSC_ATOM);
    }

    @Override
    public String getFullName() {
        return "Content Describes";
    }
}
