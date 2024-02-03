package net.frogmouth.rnd.eofff.gopro;

import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class HMMTParser extends BaseBoxParser {

    private static final long BYTES_IN_BOX_HEADER = Integer.BYTES + FourCC.BYTES;

    public HMMTParser() {}

    @Override
    public FourCC getFourCC() {
        return HMMT.HMMT_ATOM;
    }

    @Override
    public HMMT parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HMMT box = new HMMT();
        byte[] bytes = parseContext.getBytes(boxSize - BYTES_IN_BOX_HEADER);
        box.setValue(new String(bytes, StandardCharsets.US_ASCII));
        return box;
    }
}
