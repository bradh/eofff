package net.frogmouth.rnd.eofff.isobmff.moof;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class MovieFragmentBox extends AbstractContainerBox {

    public static final FourCC MOOF_ATOM = new FourCC("moof");

    public MovieFragmentBox() {
        super(MOOF_ATOM);
    }

    @Override
    public String getFullName() {
        return "MovieFragmentBox";
    }
}
