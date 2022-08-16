package net.frogmouth.rnd.eofff.isobmff.moov;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

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
