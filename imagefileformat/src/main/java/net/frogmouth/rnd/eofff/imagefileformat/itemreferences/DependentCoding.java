package net.frogmouth.rnd.eofff.imagefileformat.itemreferences;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Dependent coding item reference.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 8.2.2 and B.2.5.
 */
public class DependentCoding extends SingleItemReferenceBox {
    public static final FourCC DPND_ATOM = new FourCC("dpnd");

    public DependentCoding() {
        super(DPND_ATOM);
    }

    @Override
    public String getFullName() {
        return "Dependent coding";
    }
}
