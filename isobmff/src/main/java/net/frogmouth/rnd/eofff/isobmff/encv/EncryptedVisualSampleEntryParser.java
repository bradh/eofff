package net.frogmouth.rnd.eofff.isobmff.encv;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntryParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser.class)
public class EncryptedVisualSampleEntryParser extends VisualSampleEntryParser
        implements SampleEntryParser {

    public EncryptedVisualSampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return EncryptedVisualSampleEntry.ENCV_ATOM;
    }

    @Override
    public SampleEntry parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        VisualSampleEntry box = new EncryptedVisualSampleEntry();
        this.parseVisualSampleEntry(parseContext, box);
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
