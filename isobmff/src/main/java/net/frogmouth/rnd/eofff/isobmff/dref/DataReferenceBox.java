package net.frogmouth.rnd.eofff.isobmff.dref;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class DataReferenceBox extends FullBox {

    public DataReferenceBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "DataReferenceBox";
    }
}
