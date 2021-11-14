package net.frogmouth.rnd.eofff.isobmff.mdat;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class MediaDataBoxParser extends BoxParser {

    public MediaDataBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("mdat");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MediaDataBox box = new MediaDataBox(boxSize, boxName);
        box.setDataOffset(parseContext.getCursorPosition());
        box.setDataLength(initialOffset + boxSize - parseContext.getCursorPosition());
        parseContext.skipBytes(box.getDataLength());
        return box;
    }
}
