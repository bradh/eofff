package net.frogmouth.rnd.eofff.gopro;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class GUMIParser extends BaseBoxParser {

    private static final long BYTES_IN_BOX_HEADER = Integer.BYTES + FourCC.BYTES;

    public GUMIParser() {}

    @Override
    public FourCC getFourCC() {
        return GUMI.GUMI_ATOM;
    }

    @Override
    public GUMI parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        GUMI box = new GUMI();
        byte[] bytes = parseContext.getBytes(boxSize - BYTES_IN_BOX_HEADER);
        box.setValues(bytes);
        return box;
    }
}
