package net.frogmouth.rnd.eofff.isobmff.stbl;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class SampleTableBoxParser extends BaseBoxParser {
    public SampleTableBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return SampleTableBox.STBL_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SampleTableBox box = new SampleTableBox();
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}