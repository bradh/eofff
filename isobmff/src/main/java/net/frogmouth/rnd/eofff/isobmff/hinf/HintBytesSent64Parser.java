package net.frogmouth.rnd.eofff.isobmff.hinf;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class HintBytesSent64Parser extends BoxParser {

    public HintBytesSent64Parser() {}

    @Override
    public FourCC getFourCC() {
        return HintBytesSent64.TPYL_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HintBytesSent64 box = new HintBytesSent64();
        box.setBytesSent(parseContext.readUnsignedInt64());
        return box;
    }
}
