package net.frogmouth.rnd.eofff.isobmff.grpl;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public interface EntityToGroupParser {
    public abstract FourCC getFourCC();

    public abstract EntityToGroup parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName);
}
