package net.frogmouth.rnd.eofff.isobmff.hinf;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class HintRepeatedBytesSentParser extends BoxParser {

    public HintRepeatedBytesSentParser() {}

    @Override
    public FourCC getFourCC() {
        return HintRepeatedBytesSent.DREP_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HintRepeatedBytesSent box = new HintRepeatedBytesSent();
        box.setBytesSent(parseContext.readUnsignedInt64());
        return box;
    }
}
