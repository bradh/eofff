package net.frogmouth.rnd.eofff.jpeg2000.fileformat;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class JP2HeaderBoxParser extends BaseBoxParser {
    public JP2HeaderBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return JP2HeaderBox.JP2H_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        JP2HeaderBox box = new JP2HeaderBox();
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
