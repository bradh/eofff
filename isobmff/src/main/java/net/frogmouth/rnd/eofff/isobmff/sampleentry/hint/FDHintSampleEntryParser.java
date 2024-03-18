package net.frogmouth.rnd.eofff.isobmff.sampleentry.hint;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser.class)
public class FDHintSampleEntryParser extends HintSampleEntryParser implements SampleEntryParser {

    public FDHintSampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return FDHintSampleEntry.FDP_ATOM;
    }

    @Override
    public SampleEntry parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        FDHintSampleEntry box = new FDHintSampleEntry();
        parseBaseSampleEntry(parseContext, box);
        box.setHintTrackVersion(parseContext.readUnsignedInt16());
        box.setHighestCompatibleVersion(parseContext.readUnsignedInt16());
        box.setPartitionEntryID(parseContext.readUnsignedInt16());
        box.setFEC_overhead(parseContext.readUnsignedInt16());
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
