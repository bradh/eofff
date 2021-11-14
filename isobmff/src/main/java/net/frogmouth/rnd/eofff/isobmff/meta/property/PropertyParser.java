package net.frogmouth.rnd.eofff.isobmff.meta.property;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public abstract class PropertyParser {
    public abstract FourCC getFourCC();

    public abstract AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName);
}
