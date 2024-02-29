package net.frogmouth.rnd.eofff.isobmff.mfra;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.dinf.*;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class MovieFragmentRandomAccessBoxParser extends BaseBoxParser {
    public MovieFragmentRandomAccessBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return MovieFragmentRandomAccessBox.MFRA_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MovieFragmentRandomAccessBox box = new MovieFragmentRandomAccessBox();
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
