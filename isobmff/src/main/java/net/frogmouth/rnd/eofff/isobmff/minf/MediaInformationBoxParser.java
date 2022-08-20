package net.frogmouth.rnd.eofff.isobmff.minf;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class MediaInformationBoxParser extends BaseBoxParser {
    public MediaInformationBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return MediaInformationBox.MINF_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MediaInformationBox box = new MediaInformationBox();
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
