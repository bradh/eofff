package net.frogmouth.rnd.eofff.isobmff.ttyp;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.ftyp.GeneralTypeBoxParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class TrackTypeBoxParser extends GeneralTypeBoxParser {

    public TrackTypeBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return TrackTypeBox.TTYP_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TrackTypeBox box = new TrackTypeBox();
        doParse(box, parseContext, initialOffset, boxSize);
        return box;
    }
}
