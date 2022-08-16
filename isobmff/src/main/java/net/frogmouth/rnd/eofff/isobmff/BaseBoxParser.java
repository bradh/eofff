package net.frogmouth.rnd.eofff.isobmff;

public class BaseBoxParser extends BoxParser {

    @Override
    public FourCC getFourCC() {
        throw new UnsupportedOperationException(
                "BaseBox getFourCC() should not be called directly");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        BaseBox box = new BaseBox(boxName);
        return box;
    }
}
