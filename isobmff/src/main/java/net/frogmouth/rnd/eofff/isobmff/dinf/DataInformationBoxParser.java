package net.frogmouth.rnd.eofff.isobmff.dinf;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class DataInformationBoxParser extends BaseBoxParser {
    public DataInformationBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("dinf");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        DataInformationBox box = new DataInformationBox(boxSize, boxName);
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
