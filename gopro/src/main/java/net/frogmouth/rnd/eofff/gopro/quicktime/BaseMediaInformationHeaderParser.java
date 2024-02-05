package net.frogmouth.rnd.eofff.gopro.quicktime;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

// @AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class BaseMediaInformationHeaderParser extends BaseBoxParser {
    public BaseMediaInformationHeaderParser() {}

    @Override
    public FourCC getFourCC() {
        return BaseMediaInformationHeader.GMHD_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        BaseMediaInformationHeader box = new BaseMediaInformationHeader();
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
