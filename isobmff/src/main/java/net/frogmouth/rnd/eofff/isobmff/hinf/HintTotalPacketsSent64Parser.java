package net.frogmouth.rnd.eofff.isobmff.hinf;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class HintTotalPacketsSent64Parser extends BoxParser {

    public HintTotalPacketsSent64Parser() {}

    @Override
    public FourCC getFourCC() {
        return HintTotalPacketsSent64.NUMP_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HintTotalPacketsSent64 box = new HintTotalPacketsSent64();
        box.setPacketsSent(parseContext.readUnsignedInt64());
        return box;
    }
}
