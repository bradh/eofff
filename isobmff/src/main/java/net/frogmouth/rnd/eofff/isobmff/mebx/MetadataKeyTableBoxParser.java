package net.frogmouth.rnd.eofff.isobmff.mebx;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class MetadataKeyTableBoxParser extends BaseBoxParser {
    public MetadataKeyTableBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return MetadataKeyTableBox.KEYS_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MetadataKeyTableBox box = new MetadataKeyTableBox();
        // TODO: we need to do special case handling - its all keyboxes
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
