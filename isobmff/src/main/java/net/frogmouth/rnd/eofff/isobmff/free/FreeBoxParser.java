package net.frogmouth.rnd.eofff.isobmff.free;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class FreeBoxParser extends FreeSpaceBoxParser {
    @Override
    public FourCC getFourCC() {
        return FreeBox.FREE_ATOM;
    }

    protected FreeSpaceBox getBox() {
        return new FreeBox();
    }
}
