package net.frogmouth.rnd.eofff.isobmff.iref;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Font item reference.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 8.11.12
 */
public class FontReference extends SingleItemReferenceBox {
    public static final FourCC FONT_ATOM = new FourCC("font");

    public FontReference() {
        super(FONT_ATOM);
    }

    @Override
    public String getFullName() {
        return "Font";
    }
}
