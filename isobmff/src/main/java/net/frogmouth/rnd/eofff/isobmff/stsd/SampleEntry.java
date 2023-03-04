package net.frogmouth.rnd.eofff.isobmff.stsd;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public abstract class SampleEntry extends AbstractContainerBox {

    public SampleEntry(FourCC format) {
        super(format);
    }
}
