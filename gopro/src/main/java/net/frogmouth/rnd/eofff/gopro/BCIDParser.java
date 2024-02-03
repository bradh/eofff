package net.frogmouth.rnd.eofff.gopro;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class BCIDParser extends BaseBoxParser {

    private static final long BYTES_IN_BOX_HEADER = Integer.BYTES + FourCC.BYTES;

    public BCIDParser() {}

    @Override
    public FourCC getFourCC() {
        return BCID.BCID_ATOM;
    }

    @Override
    public BCID parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        BCID box = new BCID();
        byte[] bytes = parseContext.getBytes(boxSize - BYTES_IN_BOX_HEADER);
        box.setValue(bytes);
        return box;
    }
}
