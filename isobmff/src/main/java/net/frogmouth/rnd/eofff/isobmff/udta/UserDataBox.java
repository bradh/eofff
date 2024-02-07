package net.frogmouth.rnd.eofff.isobmff.udta;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * User Data Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.10.1.
 */
public class UserDataBox extends AbstractContainerBox {
    public static final FourCC UDTA_ATOM = new FourCC("udta");

    public UserDataBox() {
        super(UDTA_ATOM);
    }

    @Override
    public String getFullName() {
        return "UserDataBox";
    }
}
