package net.frogmouth.rnd.eofff.isobmff.trak;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class TrackBox extends AbstractContainerBox {

    public static final FourCC TRAK_ATOM = new FourCC("trak");

    public TrackBox() {
        super(TRAK_ATOM);
    }

    @Override
    public String getFullName() {
        return "TrackBox";
    }
}
