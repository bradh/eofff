package net.frogmouth.rnd.eofff.isobmff.samplegroup;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

/**
 * @author bradh
 */
public class FallbackSampleGroupEntryParser implements SampleGroupEntryParser {

    public FallbackSampleGroupEntryParser() {}

    @Override
    public FourCC getFourCC() {
        throw new UnsupportedOperationException(
                "getFourCC() should not be called for the falllback sample groun entry parser");
    }

    @Override
    public SampleGroupEntry parse(ParseContext parseContext, long descriptionLength) {
        parseContext.skipBytes(descriptionLength);
        return null;
    }
}
