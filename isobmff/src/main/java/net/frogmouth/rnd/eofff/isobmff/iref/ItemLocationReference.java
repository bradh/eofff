package net.frogmouth.rnd.eofff.isobmff.iref;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Item location reference.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 8.11.3.
 */
public class ItemLocationReference extends SingleItemReferenceBox {
    public static final FourCC ILOC_ATOM = new FourCC("iloc");

    public ItemLocationReference() {
        super(ILOC_ATOM);
    }

    @Override
    public String getFullName() {
        return "Item Location";
    }
}
