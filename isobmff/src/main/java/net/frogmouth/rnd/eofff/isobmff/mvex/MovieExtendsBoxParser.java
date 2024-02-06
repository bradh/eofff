package net.frogmouth.rnd.eofff.isobmff.mvex;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class MovieExtendsBoxParser extends BaseBoxParser {
    public MovieExtendsBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return MovieExtendsBox.MVEX_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MovieExtendsBox box = new MovieExtendsBox();
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
