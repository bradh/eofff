package net.frogmouth.rnd.eofff.isobmff.meta;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class MetaBoxParser extends BoxParser {

    public MetaBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("meta");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, String boxName) {
        MetaBox box = new MetaBox(boxSize, boxName);
        int version = parseContext.getByte();
        // TODO: check supported versions
        // if (version != 0) {
        //    byteBuffer.position((int) (initialOffset + boxSize));
        //    return new BaseBox(boxSize, boxName);
        // }
        byte[] flags = new byte[3];
        parseContext.getBytes(flags);
        box.setVersion(version);
        box.setFlags(flags);
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
