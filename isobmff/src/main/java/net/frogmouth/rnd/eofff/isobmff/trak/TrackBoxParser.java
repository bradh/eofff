package net.frogmouth.rnd.eofff.isobmff.trak;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class TrackBoxParser extends BaseBoxParser {
    public TrackBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("trak");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TrackBox box = new TrackBox();
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
