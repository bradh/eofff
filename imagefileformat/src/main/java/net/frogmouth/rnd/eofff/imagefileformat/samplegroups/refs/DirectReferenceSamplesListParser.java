package net.frogmouth.rnd.eofff.imagefileformat.samplegroups.refs;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.samplegroup.SampleGroupEntry;
import net.frogmouth.rnd.eofff.isobmff.samplegroup.SampleGroupEntryParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.samplegroup.SampleGroupEntryParser.class)
public class DirectReferenceSamplesListParser implements SampleGroupEntryParser {

    private static final Logger LOG =
            LoggerFactory.getLogger(DirectReferenceSamplesListParser.class);

    public DirectReferenceSamplesListParser() {}

    @Override
    public FourCC getFourCC() {
        return DirectReferenceSamplesList.REFS_ATOM;
    }

    @Override
    public SampleGroupEntry parse(ParseContext parseContext, long descriptionLength) {
        DirectReferenceSamplesList sampleEntry = new DirectReferenceSamplesList();
        sampleEntry.setSampleId(parseContext.readUnsignedInt32());
        int numDirectReferenceSamples = parseContext.readUnsignedInt8();
        for (int i = 0; i < numDirectReferenceSamples; i++) {
            sampleEntry.addDirectReferenceSampleId(parseContext.readUnsignedInt32());
        }
        return sampleEntry;
    }
}
