package net.frogmouth.rnd.eofff.imagefileformat.itemreferences;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Tile basis item reference.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 6.5.7
 */
public class TileBasis extends SingleItemReferenceBox {
    public static final FourCC TBAS_ATOM = new FourCC("tbas");

    public TileBasis() {
        super(TBAS_ATOM);
    }

    @Override
    public String getFullName() {
        return "Tile track base";
    }
}
