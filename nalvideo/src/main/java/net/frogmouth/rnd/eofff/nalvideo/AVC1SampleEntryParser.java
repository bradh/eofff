package net.frogmouth.rnd.eofff.nalvideo;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntryParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser.class)
public class AVC1SampleEntryParser extends VisualSampleEntryParser implements SampleEntryParser {
    public AVC1SampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return AVC1SampleEntry.AVC1_ATOM;
    }

    @Override
    public SampleEntry parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        AVC1SampleEntry box = new AVC1SampleEntry();
        this.parseVisualSampleEntry(parseContext, box);
        // The AVCConfigurationBox is nested in here.
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
