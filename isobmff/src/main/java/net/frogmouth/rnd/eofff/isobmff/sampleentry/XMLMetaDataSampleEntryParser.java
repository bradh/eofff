package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser.class)
public class XMLMetaDataSampleEntryParser extends BaseSampleEntryParser
        implements SampleEntryParser {
    public XMLMetaDataSampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return XMLMetaDataSampleEntry.METX_ATOM;
    }

    @Override
    public SampleEntry parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        XMLMetaDataSampleEntry box = new XMLMetaDataSampleEntry();
        parseBaseSampleEntry(parseContext, box);
        box.setContentEncoding(parseContext.readNullDelimitedString(boxSize));
        box.setNamespace(parseContext.readNullDelimitedString(boxSize));
        box.setSchemaLocation(parseContext.readNullDelimitedString(boxSize));
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
