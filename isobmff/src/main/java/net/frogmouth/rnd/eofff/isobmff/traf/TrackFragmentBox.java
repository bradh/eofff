package net.frogmouth.rnd.eofff.isobmff.traf;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class TrackFragmentBox extends AbstractContainerBox {
    public static final FourCC TRAF_ATOM = new FourCC("traf");

    public TrackFragmentBox() {
        super(TRAF_ATOM);
    }

    @Override
    public String getFullName() {
        return "Track Fragment Box";
    }
}
