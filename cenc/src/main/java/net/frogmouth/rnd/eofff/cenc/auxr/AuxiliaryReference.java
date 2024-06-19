package net.frogmouth.rnd.eofff.imagefileformat.itemreferences;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Auxiliary Image item reference.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 6.4.5.
 */
public class AuxiliaryImage extends SingleItemReferenceBox {
    public static final FourCC AUXL_ATOM = new FourCC("auxl");

    public AuxiliaryImage() {
        super(AUXL_ATOM);
    }

    @Override
    public String getFullName() {
        return "Auxiliary Image";
    }
}
