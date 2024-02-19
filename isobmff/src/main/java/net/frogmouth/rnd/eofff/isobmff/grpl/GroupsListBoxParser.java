package net.frogmouth.rnd.eofff.isobmff.grpl;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class GroupsListBoxParser extends BaseBoxParser {
    public GroupsListBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return GroupsListBox.GRPL_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        GroupsListBox box = new GroupsListBox(boxName);
        long offset = parseContext.getCursorPosition();
        while (parseContext.getCursorPosition() < (initialOffset + boxSize)) {
            long childOffset = parseContext.getCursorPosition();
            long childSize = parseContext.readUnsignedInt32();
            FourCC grouping_type = parseContext.readFourCC();
            EntityToGroupParser parser = EntityToGroupFactoryManager.getParser(grouping_type);
            EntityToGroup entityToGroup =
                    parser.parse(parseContext, childOffset, childSize, grouping_type);
            box.addGrouping(entityToGroup);
        }
        parseContext.setCursorPosition(offset + boxSize);
        return box;
    }
}
