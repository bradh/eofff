package net.frogmouth.rnd.eofff.isobmff.hinf;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class HintImmediateBytesSentParser extends BoxParser {

    public HintImmediateBytesSentParser() {}

    @Override
    public FourCC getFourCC() {
        return HintImmediateBytesSent.DIMM_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HintImmediateBytesSent box = new HintImmediateBytesSent();
        box.setBytesSent(parseContext.readUnsignedInt64());
        return box;
    }
}
