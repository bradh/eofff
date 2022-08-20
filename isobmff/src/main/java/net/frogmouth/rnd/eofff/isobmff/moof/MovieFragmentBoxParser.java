package net.frogmouth.rnd.eofff.isobmff.moof;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class MovieFragmentBoxParser extends BaseBoxParser {
    public MovieFragmentBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return MovieFragmentBox.MOOF_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MovieFragmentBox box = new MovieFragmentBox();
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
