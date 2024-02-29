package net.frogmouth.rnd.eofff.isobmff.mfra;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Movie Fragment Random Access Box (mfra).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.8.9.
 */
public class MovieFragmentRandomAccessBox extends AbstractContainerBox {

    public static final FourCC MFRA_ATOM = new FourCC("mfra");

    public MovieFragmentRandomAccessBox() {
        super(MFRA_ATOM);
    }

    @Override
    public String getFullName() {
        return "MovieFragmentRandomAccessBox";
    }
}
