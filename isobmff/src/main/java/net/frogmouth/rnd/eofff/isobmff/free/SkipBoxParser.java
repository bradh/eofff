package net.frogmouth.rnd.eofff.isobmff.free;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class SkipBoxParser extends FreeSpaceBoxParser {
    @Override
    public FourCC getFourCC() {
        return SkipBox.SKIP_ATOM;
    }

    @Override
    protected FreeSpaceBox getBox() {
        return new SkipBox();
    }
}
