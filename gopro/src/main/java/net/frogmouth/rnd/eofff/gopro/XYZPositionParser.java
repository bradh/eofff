package net.frogmouth.rnd.eofff.gopro;

import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class XYZPositionParser extends BaseBoxParser {

    private static final long BYTES_IN_BOX_HEADER = Integer.BYTES + FourCC.BYTES;

    public XYZPositionParser() {}

    @Override
    public FourCC getFourCC() {
        return XYZPosition.XYZPosition_ATOM;
    }

    @Override
    public XYZPosition parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        XYZPosition box = new XYZPosition();
        // TODO: fix parsing
        byte[] bytes = parseContext.getBytes(boxSize - BYTES_IN_BOX_HEADER);
        box.setValue(new String(bytes, StandardCharsets.US_ASCII));
        return box;
    }
}
