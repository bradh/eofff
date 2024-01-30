package net.frogmouth.rnd.eofff.jpeg2000.fileformat;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class SignatureBoxParser extends BaseBoxParser {

    @Override
    public SignatureBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SignatureBox box = new SignatureBox();
        box.setData(parseContext.readUnsignedInt32());
        return box;
    }

    @Override
    public FourCC getFourCC() {
        return SignatureBox.JP_ATOM;
    }
}
