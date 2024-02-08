package net.frogmouth.rnd.eofff.jpeg2000.fileformat.jp2c;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class ContiguousCodestreamBoxParser extends BoxParser {

    public ContiguousCodestreamBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ContiguousCodestreamBox.JP2C_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ContiguousCodestreamBox box = new ContiguousCodestreamBox();
        box.setInitialOffset(initialOffset + 8);
        byte[] data =
                parseContext.getBytes(boxSize - (parseContext.getCursorPosition() - initialOffset));
        box.setData(data);
        return box;
    }
}
