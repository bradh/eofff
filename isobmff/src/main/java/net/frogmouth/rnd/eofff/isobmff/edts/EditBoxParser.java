package net.frogmouth.rnd.eofff.isobmff.edts;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class EditBoxParser extends BaseBoxParser {
    public EditBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return EditBox.EDTS_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        EditBox box = new EditBox();
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
