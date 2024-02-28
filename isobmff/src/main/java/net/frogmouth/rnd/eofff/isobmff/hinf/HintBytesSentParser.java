package net.frogmouth.rnd.eofff.isobmff.hinf;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class HintBytesSentParser extends BoxParser {

    public HintBytesSentParser() {}

    @Override
    public FourCC getFourCC() {
        return HintBytesSent.TPAY_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HintBytesSent box = new HintBytesSent();
        box.setBytesSent(parseContext.readUnsignedInt32());
        return box;
    }
}
