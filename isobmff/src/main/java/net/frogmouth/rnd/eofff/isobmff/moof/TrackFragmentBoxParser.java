package net.frogmouth.rnd.eofff.isobmff.moof;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class TrackFragmentBoxParser extends BaseBoxParser {
    public TrackFragmentBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("traf");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TrackFragmentBox box = new TrackFragmentBox(boxSize, boxName);
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
