package net.frogmouth.rnd.eofff.imagefileformat.itemreferences;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Predictively coded image reference.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 6.4.9
 */
public class PredictivelyCoded extends SingleItemReferenceBox {
    public static final FourCC PRED_ATOM = new FourCC("pred");

    public PredictivelyCoded() {
        super(PRED_ATOM);
    }

    @Override
    public String getFullName() {
        return "Predictively coded";
    }
}
