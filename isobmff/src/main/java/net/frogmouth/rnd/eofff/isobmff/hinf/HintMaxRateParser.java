package net.frogmouth.rnd.eofff.isobmff.hinf;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class HintMaxRateParser extends BoxParser {

    public HintMaxRateParser() {}

    @Override
    public FourCC getFourCC() {
        return HintMaxRate.MAXR_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HintMaxRate box = new HintMaxRate();
        box.setPeriod(parseContext.readUnsignedInt32());
        box.setBytes(parseContext.readUnsignedInt32());
        return box;
    }
}
