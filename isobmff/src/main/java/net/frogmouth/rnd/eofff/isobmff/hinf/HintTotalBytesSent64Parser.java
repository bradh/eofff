package net.frogmouth.rnd.eofff.isobmff.hinf;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class HintTotalBytesSent64Parser extends BoxParser {

    public HintTotalBytesSent64Parser() {}

    @Override
    public FourCC getFourCC() {
        return HintTotalBytesSent64.TRPY_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HintTotalBytesSent64 box = new HintTotalBytesSent64();
        box.setBytesSent(parseContext.readUnsignedInt64());
        return box;
    }
}
