package net.frogmouth.rnd.eofff.isobmff.mdia;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * MediaBox.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.4.1
 */
public class MediaBox extends AbstractContainerBox {

    public static final FourCC MDIA_ATOM = new FourCC("mdia");

    public MediaBox() {
        super(MDIA_ATOM);
    }

    @Override
    public String getFullName() {
        return "MediaBox";
    }
}
