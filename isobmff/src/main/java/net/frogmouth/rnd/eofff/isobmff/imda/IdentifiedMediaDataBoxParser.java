package net.frogmouth.rnd.eofff.isobmff.imda;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.idat.*;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class IdentifiedMediaDataBoxParser extends BoxParser {

    public IdentifiedMediaDataBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return IdentifiedMediaDataBox.IMDA_ATOM;
    }

    @Override
    public IdentifiedMediaDataBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        IdentifiedMediaDataBox box = new IdentifiedMediaDataBox();
        box.setIdentifier(parseContext.readUnsignedInt32());
        int dataLen = (int) (initialOffset + boxSize - parseContext.getCursorPosition());
        box.setData(parseContext.getBytes(dataLen));
        return box;
    }
}
