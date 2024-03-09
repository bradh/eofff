package net.frogmouth.rnd.eofff.nalvideo;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntryParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser.class)
public class HVC2SampleEntryParser extends VisualSampleEntryParser implements SampleEntryParser {
    public HVC2SampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return HVC2SampleEntry.HVC2_ATOM;
    }

    @Override
    public SampleEntry parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HVC2SampleEntry box = new HVC2SampleEntry();
        parseVisualSampleEntry(parseContext, box);
        // The HEVCConfigurationBox is nested in here.
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
