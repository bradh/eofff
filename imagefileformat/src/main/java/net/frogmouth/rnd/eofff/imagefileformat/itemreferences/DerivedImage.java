package net.frogmouth.rnd.eofff.imagefileformat.itemreferences;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Derived Image item reference.
 *
 * <p>This is used for derived images, where the derived image references to the images it derives
 * from.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 6.6.1 and 6.6.2.
 */
public class DerivedImage extends SingleItemReferenceBox {
    public static final FourCC DIMG_ATOM = new FourCC("dimg");

    public DerivedImage() {
        super(DIMG_ATOM);
    }

    @Override
    public String getFullName() {
        return "Derived image";
    }
}
