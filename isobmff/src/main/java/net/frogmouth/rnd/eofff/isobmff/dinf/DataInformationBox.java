package net.frogmouth.rnd.eofff.isobmff.dinf;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class DataInformationBox extends AbstractContainerBox {

    public static final FourCC DINF_ATOM = new FourCC("dinf");

    public DataInformationBox() {
        super(DINF_ATOM);
    }

    @Override
    public String getFullName() {
        return "DataInformationBox";
    }
}
