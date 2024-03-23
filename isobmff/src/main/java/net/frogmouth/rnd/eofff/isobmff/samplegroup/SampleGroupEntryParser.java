package net.frogmouth.rnd.eofff.isobmff.samplegroup;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public interface SampleGroupEntryParser {
    public FourCC getFourCC();

    public SampleGroupEntry parse(ParseContext parseContext, long descriptionLength);
}
