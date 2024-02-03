package net.frogmouth.rnd.eofff.gopro;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class FIRM extends GoProStringValueBox {

    public static final FourCC FIRM_ATOM = new FourCC("FIRM");

    public FIRM() {
        super(FIRM_ATOM);
    }

    @Override
    public String getFullName() {
        return "GoPro Firmware";
    }
}
