package net.frogmouth.rnd.eofff.isobmff.minf;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class MediaInformationBox extends AbstractContainerBox {

    public MediaInformationBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "MediaInformationBox";
    }
}
