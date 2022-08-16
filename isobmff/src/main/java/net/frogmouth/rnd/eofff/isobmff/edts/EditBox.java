package net.frogmouth.rnd.eofff.isobmff.edts;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Edit Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.6.5.
 */
public class EditBox extends AbstractContainerBox {
    public EditBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "EditBox";
    }
}
