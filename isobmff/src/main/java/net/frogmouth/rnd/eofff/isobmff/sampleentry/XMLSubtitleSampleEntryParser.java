package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser.class)
public class XMLSubtitleSampleEntryParser extends SubtitleSampleEntryParser
        implements SampleEntryParser {
    public XMLSubtitleSampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return XMLSubtitleSampleEntry.STPP_ATOM;
    }

    @Override
    public SampleEntry parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        XMLSubtitleSampleEntry box = new XMLSubtitleSampleEntry();
        parseBaseSampleEntry(parseContext, box);
        box.setNamespace(parseContext.readNullDelimitedString(boxSize));
        box.setSchemaLocation(parseContext.readNullDelimitedString(boxSize));
        box.setAuxiliaryMimeTypes(parseContext.readNullDelimitedString(boxSize));
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
