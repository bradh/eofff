package net.frogmouth.rnd.eofff.quicktime;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class TimeCodeContainerBoxParser extends BaseBoxParser {
    public TimeCodeContainerBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return TimeCodeContainerBox.TMCD_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TimeCodeContainerBox box = new TimeCodeContainerBox();
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
