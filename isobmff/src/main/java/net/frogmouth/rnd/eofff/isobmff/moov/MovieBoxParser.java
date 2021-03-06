package net.frogmouth.rnd.eofff.isobmff.moov;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class MovieBoxParser extends BaseBoxParser {
    public MovieBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("moov");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MovieBox box = new MovieBox(boxSize, boxName);
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
