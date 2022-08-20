package net.frogmouth.rnd.eofff.isobmff.minf;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class MediaInformationBox extends AbstractContainerBox {

    public static final FourCC MINF_ATOM = new FourCC("minf");

    public MediaInformationBox() {
        super(MINF_ATOM);
    }

    @Override
    public String getFullName() {
        return "MediaInformationBox";
    }
}
