package net.frogmouth.rnd.eofff.isobmff.free;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class FreeBox extends FreeSpaceBox {

    public static final FourCC FREE_ATOM = new FourCC("free");

    public FreeBox() {
        super(FREE_ATOM);
    }
}
