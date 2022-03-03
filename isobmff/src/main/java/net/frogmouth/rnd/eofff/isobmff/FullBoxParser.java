package net.frogmouth.rnd.eofff.isobmff;

public abstract class FullBoxParser extends BoxParser {
    protected Box parseAsBaseBox(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        BaseBoxParser parser = new BaseBoxParser();
        return parser.parse(parseContext, initialOffset, boxSize, boxName);
    }

    protected int parseFlags(ParseContext parseContext) {
        byte[] flags = new byte[3];
        parseContext.readBytes(flags);
        return ((flags[0] & 0xFF) << 16) | ((flags[1] & 0xFF) << 8) | (flags[2] & 0xFF);
    }
}
