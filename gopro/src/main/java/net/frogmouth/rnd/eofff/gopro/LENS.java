package net.frogmouth.rnd.eofff.gopro;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class LENS extends GoProStringValueBox {

    public static final FourCC LENS_ATOM = new FourCC("LENS");

    public LENS() {
        super(LENS_ATOM);
    }

    @Override
    public String getFullName() {
        return "GoPro Lens";
    }
}
