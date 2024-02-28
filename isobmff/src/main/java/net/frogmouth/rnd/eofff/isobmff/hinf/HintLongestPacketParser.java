package net.frogmouth.rnd.eofff.isobmff.hinf;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class HintLongestPacketParser extends BoxParser {

    public HintLongestPacketParser() {}

    @Override
    public FourCC getFourCC() {
        return HintLongestPacket.DMAX_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HintLongestPacket box = new HintLongestPacket();
        box.setTime(parseContext.readUnsignedInt32());
        return box;
    }
}