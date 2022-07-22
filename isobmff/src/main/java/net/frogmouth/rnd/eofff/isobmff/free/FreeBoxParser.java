package net.frogmouth.rnd.eofff.isobmff.free;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class FreeBoxParser extends BaseBoxParser {

    public FreeBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("free");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        FreeBox box = new FreeBox(boxSize, boxName);
        byte[] data = parseContext.getBytes(boxSize - (Integer.BYTES + FourCC.BYTES));
        box.setData(data);
        return box;
    }
}
