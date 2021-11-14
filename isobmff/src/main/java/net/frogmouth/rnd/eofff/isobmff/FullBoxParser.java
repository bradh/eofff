package net.frogmouth.rnd.eofff.isobmff;

public abstract class FullBoxParser extends BoxParser {
    protected Box parseAsBaseBox(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        BaseBoxParser parser = new BaseBoxParser();
        return parser.parse(parseContext, initialOffset, boxSize, boxName);
    }

    protected byte[] parseFlags(ParseContext parseContext) {
        byte[] flags = new byte[3];
        parseContext.readBytes(flags);
        return flags;
    }
}
