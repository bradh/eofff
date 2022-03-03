package net.frogmouth.rnd.eofff.isobmff.moof;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class TrackFragmentBox extends AbstractContainerBox {

    public TrackFragmentBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "Track Fragment Box";
    }
}
