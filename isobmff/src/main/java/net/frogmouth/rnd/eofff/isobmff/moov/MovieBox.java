package net.frogmouth.rnd.eofff.isobmff.moov;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class MovieBox extends AbstractContainerBox {

    public MovieBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "MovieBox";
    }
}
