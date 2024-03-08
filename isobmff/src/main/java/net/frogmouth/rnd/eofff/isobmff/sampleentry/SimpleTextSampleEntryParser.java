package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser.class)
public class SimpleTextSampleEntryParser extends PlainTextSampleEntryParser
        implements SampleEntryParser {
    public SimpleTextSampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return SimpleTextSampleEntry.STXT_ATOM;
    }

    @Override
    public SampleEntry parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SimpleTextSampleEntry box = new SimpleTextSampleEntry();
        parseBaseSampleEntry(parseContext, box);
        box.setContentEncoding(parseContext.readNullDelimitedString(boxSize));
        box.setMimeFormat(parseContext.readNullDelimitedString(boxSize));
        // The TextConfigBox is nested in here.
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
