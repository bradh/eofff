package net.frogmouth.rnd.eofff.miaf.itemreferences;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Pre-multiplied alpha image item reference.
 *
 * <p>See ISO/IEC 23000-22:2019 Section 7.3.5.2
 */
public class PremultipliedImageReference extends SingleItemReferenceBox {
    public static final FourCC PREM_ATOM = new FourCC("prem");

    public PremultipliedImageReference() {
        super(PREM_ATOM);
    }

    @Override
    public String getFullName() {
        return "Premultiplied Image Reference";
    }
}
