package net.frogmouth.rnd.eofff.isobmff.udta;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class UserDataBox extends AbstractContainerBox {

    public UserDataBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "UserDataBox";
    }
}
