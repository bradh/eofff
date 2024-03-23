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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public SampleGroupEntry parse(ParseContext parseContext, long descriptionLength) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
