package net.frogmouth.rnd.eofff.cenc.auxr;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Auxiliary item reference.
 *
 * <p>See ISO/IEC 23001-7:2023 Section 8.4.1
 */
public class AuxiliaryReference extends SingleItemReferenceBox {
    public static final FourCC AUXR_ATOM = new FourCC("auxr");

    public AuxiliaryReference() {
        super(AUXR_ATOM);
    }

    @Override
    public String getFullName() {
        return "Auxiliary item reference";
    }
}
