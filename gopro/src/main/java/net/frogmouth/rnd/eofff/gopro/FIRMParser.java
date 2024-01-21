package net.frogmouth.rnd.eofff.gopro;

import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FIRMParser extends BaseBoxParser {

    private static final long BYTES_IN_BOX_HEADER = Integer.BYTES + FourCC.BYTES;
    private static final Logger LOG = LoggerFactory.getLogger(FIRMParser.class);

    public FIRMParser() {}

    @Override
    public FourCC getFourCC() {
        return FIRM.FIRM_ATOM;
    }

    @Override
    public FIRM parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        FIRM box = new FIRM();
        byte[] bytes = parseContext.getBytes(boxSize - BYTES_IN_BOX_HEADER);
        box.setValue(new String(bytes, StandardCharsets.US_ASCII));
        return box;
    }
}
