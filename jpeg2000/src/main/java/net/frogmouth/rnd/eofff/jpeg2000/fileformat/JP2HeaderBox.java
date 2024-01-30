package net.frogmouth.rnd.eofff.jpeg2000.fileformat;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * JP2 Header Box.
 *
 * <p>See ISO/IEC 15444-1:2019 Section I.5.3.
 */
public class JP2HeaderBox extends AbstractContainerBox {

    public static final FourCC JP2H_ATOM = new FourCC("jp2h");

    public JP2HeaderBox() {
        super(JP2H_ATOM);
    }

    @Override
    public String getFullName() {
        return "JP2 Header Box";
    }
}
