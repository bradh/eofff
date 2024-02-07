package net.frogmouth.rnd.eofff.imagefileformat.itemreferences;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Data integrity reference.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 8.4.1
 */
public class DataIntegrityReference extends SingleItemReferenceBox {
    public static final FourCC MINT_ATOM = new FourCC("mint");

    public DataIntegrityReference() {
        super(MINT_ATOM);
    }

    @Override
    public String getFullName() {
        return "Data Integrity";
    }
}
