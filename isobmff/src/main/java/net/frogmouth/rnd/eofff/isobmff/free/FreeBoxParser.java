package net.frogmouth.rnd.eofff.isobmff.free;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class FreeBoxParser extends FreeSpaceBoxParser {
    @Override
    public FourCC getFourCC() {
        return FreeBox.FREE_ATOM;
    }

    protected FreeSpaceBox getBox() {
        return new FreeBox();
    }
}
