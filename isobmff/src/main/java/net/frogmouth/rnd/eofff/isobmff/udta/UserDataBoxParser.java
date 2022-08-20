package net.frogmouth.rnd.eofff.isobmff.udta;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class UserDataBoxParser extends BaseBoxParser {
    public UserDataBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return UserDataBox.UDTA_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        UserDataBox box = new UserDataBox();
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
