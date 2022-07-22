package net.frogmouth.rnd.eofff.isobmff.udta;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class UserDataBoxParser extends BaseBoxParser {
    public UserDataBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("udta");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        UserDataBox box = new UserDataBox(boxSize, boxName);
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
