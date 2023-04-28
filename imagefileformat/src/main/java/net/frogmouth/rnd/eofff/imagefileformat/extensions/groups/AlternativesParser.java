package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

// TODO: move to isobmff module
public class AlternativesParser extends AbstractEntityToGroupBoxParser {

    public AlternativesParser() {}

    @Override
    public FourCC getFourCC() {
        return AlternativesBox.ALTR_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        AlternativesBox box = new AlternativesBox();
        return parseEntityToGroupBox(parseContext, box, initialOffset, boxSize, boxName);
    }
}
