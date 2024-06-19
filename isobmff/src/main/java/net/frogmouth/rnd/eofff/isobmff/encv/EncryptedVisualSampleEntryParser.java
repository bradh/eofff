package net.frogmouth.rnd.eofff.mpeg4.mp4v;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntryParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser.class)
public class MP4VisualSampleEntryParser extends VisualSampleEntryParser
        implements SampleEntryParser {

    public MP4VisualSampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return MP4VisualSampleEntry.MP4V_ATOM;
    }

    @Override
    public SampleEntry parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        VisualSampleEntry box = new MP4VisualSampleEntry();
        this.parseVisualSampleEntry(parseContext, box);
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
