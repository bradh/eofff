package net.frogmouth.rnd.eofff.isobmff.dinf;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class DataInformationBox extends AbstractContainerBox {

    public DataInformationBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "Data Information Box";
    }
}
