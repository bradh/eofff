package net.frogmouth.rnd.eofff.imagefileformat.itemreferences;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;

/**
 * Base Image item reference.
 *
 * <p>This is used for pre-derived coded images, where the derived image references to the images it
 * derives from.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 6.4.7.
 */
public class BaseImage extends SingleItemReferenceBox {
    public static final FourCC BASE_ATOM = new FourCC("base");

    public BaseImage() {
        super(BASE_ATOM);
    }

    @Override
    public String getFullName() {
        return "Base image";
    }
}
