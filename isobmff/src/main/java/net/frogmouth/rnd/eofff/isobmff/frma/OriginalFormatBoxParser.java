package net.frogmouth.rnd.eofff.isobmff.frma;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class OriginalFormatBoxParser extends BoxParser {

    public OriginalFormatBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return OriginalFormatBox.FRMA_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        OriginalFormatBox box = new OriginalFormatBox();
        box.setDataFormat(parseContext.readFourCC());
        return box;
    }
}
