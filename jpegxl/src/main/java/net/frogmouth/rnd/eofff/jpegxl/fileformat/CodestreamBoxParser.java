package net.frogmouth.rnd.eofff.jpegxl.fileformat;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class CodestreamBoxParser extends BaseBoxParser {

    @Override
    public CodestreamBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        CodestreamBox box = new CodestreamBox();
        int dataLen = (int) (initialOffset + boxSize - parseContext.getCursorPosition());
        byte[] data = new byte[dataLen];
        parseContext.readBytes(data);
        box.setData(data);
        return box;
    }

    @Override
    public FourCC getFourCC() {
        return CodestreamBox.JXLC_ATOM;
    }
}
