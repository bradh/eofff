package net.frogmouth.rnd.eofff.isobmff;

public abstract class BoxParser extends AbstractParser {
    public abstract FourCC getFourCC();

    public abstract Box parse(
            ParseContext parseContext, long initialOffset, long boxSize, String boxName);
}
