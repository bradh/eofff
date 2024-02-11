package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public interface SampleEntryParser {
    public FourCC getFourCC();

    public SampleEntry parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName);
}
