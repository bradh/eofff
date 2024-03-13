package net.frogmouth.rnd.eofff.isobmff.stpp;

import com.google.auto.service.AutoService;
import java.util.Arrays;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.BaseSampleEntryParser;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser.class)
public class XMLSubtitleSampleEntryParser extends BaseSampleEntryParser
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
        this.parseBaseSampleEntry(parseContext, box);
        String namespaces = parseContext.readNullDelimitedString(boxSize);
        String[] namespaceList = namespaces.split(" ");
        box.setNamespaces(Arrays.asList(namespaceList));
        String schemas = parseContext.readNullDelimitedString(boxSize);
        String[] schemaList = schemas.split(" ");
        box.setSchemaLocations(Arrays.asList(schemaList));
        String auxiliaryMimeTypes = parseContext.readNullDelimitedString(boxSize);
        String[] auxiliaryMimeTypesList = auxiliaryMimeTypes.split(" ");
        box.setAuxiliaryMimeTypes(Arrays.asList(auxiliaryMimeTypesList));
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
