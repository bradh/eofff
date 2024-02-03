package net.frogmouth.rnd.eofff.isobmff.free;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
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
