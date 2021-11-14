package net.frogmouth.rnd.eofff.isobmff.meta.property;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public abstract class ItemFullPropertyParser extends PropertyParser {

    protected AbstractItemProperty parseAsUnknownProperty(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        UnknownPropertyParser parser = new UnknownPropertyParser();
        return parser.parse(parseContext, initialOffset, boxSize, boxName);
    }

    protected byte[] parseFlags(ParseContext parseContext) {
        byte[] flags = new byte[3];
        parseContext.readBytes(flags);
        return flags;
    }
}
