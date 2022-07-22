package net.frogmouth.rnd.eofff.isobmff.meta;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class IlistBoxParser extends BaseBoxParser {

    public IlistBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("ilst");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        IlstBox box = new IlstBox(boxSize, boxName);
        byte[] data = parseContext.getBytes(boxSize - (Integer.BYTES + FourCC.BYTES));
        box.setData(data);
        return box;
    }
}
