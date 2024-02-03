package net.frogmouth.rnd.eofff.gopro;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

// TODO: this might not be working - not a string
public class HMMT extends GoProStringValueBox {

    public static final FourCC HMMT_ATOM = new FourCC("HMMT");

    public HMMT() {
        super(HMMT_ATOM);
    }

    @Override
    public String getFullName() {
        return "GoPro HMMT";
    }
}
