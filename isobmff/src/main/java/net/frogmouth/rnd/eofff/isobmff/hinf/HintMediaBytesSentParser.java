package net.frogmouth.rnd.eofff.isobmff.hinf;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class HintMediaBytesSentParser extends BoxParser {

    public HintMediaBytesSentParser() {}

    @Override
    public FourCC getFourCC() {
        return HintMediaBytesSent.DMED_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HintMediaBytesSent box = new HintMediaBytesSent();
        box.setBytesSent(parseContext.readUnsignedInt64());
        return box;
    }
}
