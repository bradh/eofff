package net.frogmouth.rnd.eofff.gopro;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

// TODO: not really a string, because there is a leading uint32_t item
public class XYZPosition extends GoProStringValueBox {

    // This is ©xyz in some rendering
    public static final FourCC XYZPosition_ATOM = new FourCC(0xA978797A);

    public XYZPosition() {
        super(XYZPosition_ATOM);
    }

    @Override
    public String getFullName() {
        return "GoPro Location";
    }
}
