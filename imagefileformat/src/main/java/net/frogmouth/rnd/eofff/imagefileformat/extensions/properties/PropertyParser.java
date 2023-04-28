package net.frogmouth.rnd.eofff.imagefileformat.extensions.properties;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public interface PropertyParser {
    public abstract FourCC getFourCC();

    public abstract AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName);
}
