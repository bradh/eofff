package net.frogmouth.rnd.eofff.isobmff.mebx;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoxedMetadataSampleEntryParser extends BoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(BoxedMetadataSampleEntryParser.class);

    public BoxedMetadataSampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return BoxedMetadataSampleEntry.MEBX_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        BoxedMetadataSampleEntry box = new BoxedMetadataSampleEntry();
        parseContext.skipBytes(6 * Byte.BYTES);
        box.setDataReferenceIndex(parseContext.readUnsignedInt16());
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
