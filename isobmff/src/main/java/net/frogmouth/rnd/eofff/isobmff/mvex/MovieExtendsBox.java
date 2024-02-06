package net.frogmouth.rnd.eofff.isobmff.mvex;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Movie Extends Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.8.1
 */
public class MovieExtendsBox extends AbstractContainerBox {

    public static final FourCC MVEX_ATOM = new FourCC("mvex");

    public MovieExtendsBox() {
        super(MVEX_ATOM);
    }

    @Override
    public String getFullName() {
        return "MovieExtendsBox";
    }
}
