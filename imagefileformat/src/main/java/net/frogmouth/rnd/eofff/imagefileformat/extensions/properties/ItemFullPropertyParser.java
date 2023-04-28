package net.frogmouth.rnd.eofff.imagefileformat.extensions.properties;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public abstract class ItemFullPropertyParser implements PropertyParser {

    protected AbstractItemProperty parseAsUnknownProperty(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        UnknownPropertyParser parser = new UnknownPropertyParser();
        return parser.parse(parseContext, initialOffset, boxSize, boxName);
    }

    protected int parseFlags(ParseContext parseContext) {
        byte[] flags = new byte[3];
        parseContext.readBytes(flags);
        return ((flags[0] & 0xFF) << 16) | ((flags[1] & 0xFF) << 8) | (flags[2] & 0xFF);
    }
}
