package net.frogmouth.rnd.eofff.isobmff.moof;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class MovieFragmentBox extends AbstractContainerBox {

    public MovieFragmentBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "MovieFragmentBox";
    }
}
