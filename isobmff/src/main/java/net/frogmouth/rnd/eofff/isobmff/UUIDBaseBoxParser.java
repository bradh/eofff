package net.frogmouth.rnd.eofff.isobmff;

public class UUIDBaseBoxParser extends BoxParser {

    @Override
    public FourCC getFourCC() {
        throw new UnsupportedOperationException(
                "BaseBox getFourCC() should not be called directly");
    }

    @Override
    public UUIDBaseBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        UUIDBaseBox box = new UUIDBaseBox(boxName);
        return box;
    }
}
