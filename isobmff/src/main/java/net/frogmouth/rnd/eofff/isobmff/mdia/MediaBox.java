package net.frogmouth.rnd.eofff.isobmff.mdia;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

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
