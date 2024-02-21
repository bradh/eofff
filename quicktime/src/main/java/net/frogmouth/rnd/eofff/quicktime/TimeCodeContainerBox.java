package net.frogmouth.rnd.eofff.quicktime;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class TimeCodeContainerBox extends AbstractContainerBox {

    public static final FourCC TMCD_ATOM = new FourCC("tmcd");

    public TimeCodeContainerBox() {
        super(TMCD_ATOM);
    }

    @Override
    public String getFullName() {
        return "Timecode atom";
    }
}
