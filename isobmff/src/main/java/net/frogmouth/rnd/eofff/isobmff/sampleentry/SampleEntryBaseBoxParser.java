package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.dref.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleEntryBaseBoxParser implements SampleEntryParser {

    private static final Logger LOG = LoggerFactory.getLogger(SampleEntryBaseBoxParser.class);

    @Override
    public FourCC getFourCC() {
        throw new UnsupportedOperationException(
                "SampleEntryBaseBoxParser getFourCC() should not be called directly");
    }

    @Override
    public SampleEntryBaseBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        return parseAsSampleEntryBaseBox(parseContext, initialOffset, boxSize, boxName);
    }

    protected SampleEntryBaseBox parseAsSampleEntryBaseBox(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC entry_name) {
        SampleEntryBaseBox sampleEntryBaseBox = new SampleEntryBaseBox(entry_name);
        return sampleEntryBaseBox;
    }
}
