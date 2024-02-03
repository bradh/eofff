package net.frogmouth.rnd.eofff.gopro;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class SETTParser extends BaseBoxParser {

    private static final long BYTES_IN_BOX_HEADER = Integer.BYTES + FourCC.BYTES;

    public SETTParser() {}

    @Override
    public FourCC getFourCC() {
        return SETT.SETT_ATOM;
    }

    @Override
    public SETT parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SETT box = new SETT();
        byte[] bytes = parseContext.getBytes(boxSize - BYTES_IN_BOX_HEADER);
        box.setValues(bytes);
        return box;
    }
}
