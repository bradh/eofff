package net.frogmouth.rnd.eofff.isobmff.hinf;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class HintTotalPacketsSentParser extends BoxParser {

    public HintTotalPacketsSentParser() {}

    @Override
    public FourCC getFourCC() {
        return HintTotalPacketsSent.NPCK_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HintTotalPacketsSent box = new HintTotalPacketsSent();
        box.setPacketsSent(parseContext.readUnsignedInt32());
        return box;
    }
}
