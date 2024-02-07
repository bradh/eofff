package net.frogmouth.rnd.eofff.imagefileformat.itemreferences;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Mask image reference.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 6.10.1
 */
public class MaskReference extends SingleItemReferenceBox {
    public static final FourCC MASK_ATOM = new FourCC("mask");

    public MaskReference() {
        super(MASK_ATOM);
    }

    @Override
    public String getFullName() {
        return "Mask image";
    }
}
