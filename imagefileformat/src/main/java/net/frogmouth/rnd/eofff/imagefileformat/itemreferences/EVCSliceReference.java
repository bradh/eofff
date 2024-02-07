package net.frogmouth.rnd.eofff.imagefileformat.itemreferences;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * EVC slice item reference.
 *
 * <p>See ISO/IEC 23008-12:2022 Section M.2.4
 */
public class EVCSliceReference extends SingleItemReferenceBox {
    public static final FourCC EVIR_ATOM = new FourCC("evir");

    public EVCSliceReference() {
        super(EVIR_ATOM);
    }

    @Override
    public String getFullName() {
        return "EVC Slice Reference";
    }
}
