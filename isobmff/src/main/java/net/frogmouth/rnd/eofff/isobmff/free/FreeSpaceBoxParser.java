package net.frogmouth.rnd.eofff.isobmff.free;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public abstract class FreeSpaceBoxParser extends BaseBoxParser {

    public FreeSpaceBoxParser() {}

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        FreeSpaceBox box = getBox();
        byte[] data = parseContext.getBytes(boxSize - (Integer.BYTES + FourCC.BYTES));
        box.setData(data);
        return box;
    }

    protected abstract FreeSpaceBox getBox();
}
