package net.frogmouth.rnd.eofff.isobmff.iref;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * File delivery item reference.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 8.11.6
 */
public class FileDeliveryReference extends SingleItemReferenceBox {
    public static final FourCC FDEL_ATOM = new FourCC("fdel");

    public FileDeliveryReference() {
        super(FDEL_ATOM);
    }

    @Override
    public String getFullName() {
        return "File delivery";
    }
}
