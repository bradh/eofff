package net.frogmouth.rnd.eofff.imagefileformat.items.grid;

import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class GridItemParser {

    public GridItem parse(ParseContext parseContext) {
        int version = parseContext.readByte();
        int flags = parseContext.readByte();
        GridItem gridItem = new GridItem();
        gridItem.setRows(parseContext.readUnsignedInt8() + 1);
        gridItem.setColumns(parseContext.readUnsignedInt8());
        if ((flags & 0x01) == 0x01) {
            gridItem.setOutput_width(parseContext.readUnsignedInt32());
            gridItem.setOutput_height(parseContext.readUnsignedInt32());
        } else {
            gridItem.setOutput_width(parseContext.readUnsignedInt16());
            gridItem.setOutput_height(parseContext.readUnsignedInt16());
        }
        return gridItem;
    }
}
