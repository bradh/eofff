package net.frogmouth.rnd.eofff.isobmff.dref;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public abstract class DataEntryBox extends FullBox {

    public DataEntryBox(long size, FourCC name) {
        super(size, name);
    }
}
