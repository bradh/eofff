package net.frogmouth.rnd.eofff.quicktime;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class BaseMediaInformationHeader extends AbstractContainerBox {

    public static final FourCC GMHD_ATOM = new FourCC("gmhd");

    public BaseMediaInformationHeader() {
        super(GMHD_ATOM);
    }

    @Override
    public String getFullName() {
        return "Base media information header";
    }
}
