package net.frogmouth.rnd.eofff.isobmff.free;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class SkipBox extends FreeSpaceBox {

    public static final FourCC SKIP_ATOM = new FourCC("skip");

    public SkipBox() {
        super(SKIP_ATOM);
    }
}
