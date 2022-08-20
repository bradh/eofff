package net.frogmouth.rnd.eofff.isobmff.stbl;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class SampleTableBox extends AbstractContainerBox {

    public static final FourCC STBL_ATOM = new FourCC("stbl");

    public SampleTableBox() {
        super(STBL_ATOM);
    }

    @Override
    public String getFullName() {
        return "SampleTableBox";
    }
}
