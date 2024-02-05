package net.frogmouth.rnd.eofff.isobmff.styp;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.ftyp.GeneralTypeBoxParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class SegmentTypeBoxParser extends GeneralTypeBoxParser {

    public SegmentTypeBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return SegmentTypeBox.STYP_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SegmentTypeBox box = new SegmentTypeBox();
        doParse(box, parseContext, initialOffset, boxSize);
        return box;
    }
}
