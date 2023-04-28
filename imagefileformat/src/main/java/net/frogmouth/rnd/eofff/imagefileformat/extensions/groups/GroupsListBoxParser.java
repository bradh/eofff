package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import static net.frogmouth.rnd.eofff.imagefileformat.extensions.groups.GroupsListBox.GRPL_ATOM;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

// TODO: move to isobmff module
public class GroupsListBoxParser extends BaseBoxParser {
    public GroupsListBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return GRPL_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        GroupsListBox box = new GroupsListBox(boxName);
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
