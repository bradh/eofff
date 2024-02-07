package net.frogmouth.rnd.eofff.imagefileformat.itemreferences;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Region reference item reference.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 6.10.3
 */
public class RegionReference extends SingleItemReferenceBox {
    public static final FourCC EROI_ATOM = new FourCC("eroi");

    public RegionReference() {
        super(EROI_ATOM);
    }

    @Override
    public String getFullName() {
        return "Region Reference";
    }
}
