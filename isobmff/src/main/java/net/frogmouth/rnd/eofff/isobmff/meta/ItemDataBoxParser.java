package net.frogmouth.rnd.eofff.isobmff.meta;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemDataBoxParser extends BoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(ItemDataBoxParser.class);

    public ItemDataBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("idat");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ItemDataBox box = new ItemDataBox(boxSize, boxName);
        int dataLen = (int) (initialOffset + boxSize - parseContext.getCursorPosition());
        byte[] data = new byte[dataLen];
        parseContext.readBytes(data);
        box.setData(data);
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
