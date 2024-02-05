package net.frogmouth.rnd.eofff.isobmff.idat;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class ItemDataBoxParser extends BoxParser {

    public ItemDataBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ItemDataBox.IDAT_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ItemDataBox box = new ItemDataBox();
        int dataLen = (int) (initialOffset + boxSize - parseContext.getCursorPosition());
        box.setData(parseContext.getBytes(dataLen));
        return box;
    }
}
