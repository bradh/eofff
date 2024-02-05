package net.frogmouth.rnd.eofff.isobmff.moov;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Movie Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.2.1
 */
public class MovieBox extends AbstractContainerBox {

    public static final FourCC MOOV_ATOM = new FourCC("moov");

    public MovieBox() {
        super(MOOV_ATOM);
    }

    @Override
    public String getFullName() {
        return "MovieBox";
    }
}
