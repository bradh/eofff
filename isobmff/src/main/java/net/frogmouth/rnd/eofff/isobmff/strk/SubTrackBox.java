package net.frogmouth.rnd.eofff.isobmff.strk;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Sub Track Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.14.3.
 */
public class SubTrackBox extends AbstractContainerBox {

    public static final FourCC STRK_ATOM = new FourCC("strk");

    public SubTrackBox() {
        super(STRK_ATOM);
    }

    @Override
    public String getFullName() {
        return "SubTrackBox";
    }
}
