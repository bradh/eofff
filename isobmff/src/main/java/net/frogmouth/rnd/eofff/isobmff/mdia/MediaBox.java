package net.frogmouth.rnd.eofff.isobmff.mdia;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class MediaBox extends AbstractContainerBox {

    public MediaBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "MediaBox";
    }
}
