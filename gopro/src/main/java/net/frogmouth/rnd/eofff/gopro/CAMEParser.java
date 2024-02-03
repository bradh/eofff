package net.frogmouth.rnd.eofff.gopro;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class CAMEParser extends BaseBoxParser {

    private static final long BYTES_IN_BOX_HEADER = Integer.BYTES + FourCC.BYTES;

    public CAMEParser() {}

    @Override
    public FourCC getFourCC() {
        return CAME.CAME_ATOM;
    }

    @Override
    public CAME parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        CAME box = new CAME();
        byte[] bytes = parseContext.getBytes(boxSize - BYTES_IN_BOX_HEADER);
        box.setValues(bytes);
        return box;
    }
}
