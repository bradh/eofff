package net.frogmouth.rnd.eofff.isobmff.dref;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public interface DataReferenceParser {
    public FourCC getFourCC();

    public DataEntryBaseBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName);
}
