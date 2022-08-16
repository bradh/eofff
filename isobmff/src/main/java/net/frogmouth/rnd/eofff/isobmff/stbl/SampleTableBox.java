package net.frogmouth.rnd.eofff.isobmff.stbl;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class SampleTableBox extends AbstractContainerBox {

    public SampleTableBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "SampleTableBox";
    }
}
