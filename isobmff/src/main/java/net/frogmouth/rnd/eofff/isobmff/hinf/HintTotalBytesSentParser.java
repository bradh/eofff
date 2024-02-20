package net.frogmouth.rnd.eofff.isobmff.hinf;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class HintTotalBytesSentParser extends BoxParser {

    public HintTotalBytesSentParser() {}

    @Override
    public FourCC getFourCC() {
        return HintTotalBytesSent.TOTL_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HintTotalBytesSent box = new HintTotalBytesSent();
        box.setBytesSent(parseContext.readUnsignedInt32());
        return box;
    }
}
