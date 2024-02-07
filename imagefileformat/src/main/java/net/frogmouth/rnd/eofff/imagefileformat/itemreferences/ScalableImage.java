package net.frogmouth.rnd.eofff.imagefileformat.itemreferences;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Scalable image item reference.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 6.4.8
 */
public class ScalableImage extends SingleItemReferenceBox {
    public static final FourCC EXBL_ATOM = new FourCC("exbl");

    public ScalableImage() {
        super(EXBL_ATOM);
    }

    @Override
    public String getFullName() {
        return "Scalable Image";
    }
}
