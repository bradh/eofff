package net.frogmouth.rnd.eofff.isobmff.mdia;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class MediaBoxParser extends BaseBoxParser {
    public MediaBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return MediaBox.MDIA_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MediaBox box = new MediaBox();
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}