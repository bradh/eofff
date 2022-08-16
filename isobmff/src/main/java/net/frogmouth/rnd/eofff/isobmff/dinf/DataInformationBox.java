package net.frogmouth.rnd.eofff.isobmff.dinf;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class DataInformationBox extends AbstractContainerBox {

    public DataInformationBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "Data Information Box";
    }
}
