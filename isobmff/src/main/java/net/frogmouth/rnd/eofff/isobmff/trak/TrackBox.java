package net.frogmouth.rnd.eofff.isobmff.trak;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class TrackBox extends AbstractContainerBox {

    public TrackBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "TrackBox";
    }
}
