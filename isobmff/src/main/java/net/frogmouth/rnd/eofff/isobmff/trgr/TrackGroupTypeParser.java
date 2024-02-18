package net.frogmouth.rnd.eofff.isobmff.trgr;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public interface TrackGroupTypeParser {
    public FourCC getFourCC();

    public TrackGroupTypeBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName);
}
