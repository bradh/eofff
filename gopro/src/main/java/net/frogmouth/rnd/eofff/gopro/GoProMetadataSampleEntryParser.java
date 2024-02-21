package net.frogmouth.rnd.eofff.gopro;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.BaseSampleEntryParser;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser.class)
public class GoProMetadataSampleEntryParser extends BaseSampleEntryParser
        implements SampleEntryParser {

    public GoProMetadataSampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return GoProMetadataSampleEntry.GPMD_ATOM;
    }

    @Override
    public SampleEntry parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        GoProMetadataSampleEntry box = new GoProMetadataSampleEntry();
        parseContext.getBytes(6);
        int data_reference_index = parseContext.readUnsignedInt16();
        box.setDataReferenceIndex(data_reference_index);
        box.setValue(parseContext.readUnsignedInt32());
        return box;
    }
}