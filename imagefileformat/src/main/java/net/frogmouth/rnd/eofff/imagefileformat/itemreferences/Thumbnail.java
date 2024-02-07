package net.frogmouth.rnd.eofff.imagefileformat.itemreferences;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Thumbnail item reference.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 6.4.4.
 */
public class Thumbnail extends SingleItemReferenceBox {
    public static final FourCC THMB_ATOM = new FourCC("thmb");

    public Thumbnail() {
        super(THMB_ATOM);
    }

    @Override
    public String getFullName() {
        return "Thumbnail";
    }
}
