package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public abstract class BaseSampleEntryParser extends BaseBoxParser {

    protected void parseBaseSampleEntry(ParseContext parseContext, BaseSampleEntry box) {
        byte[] bytes = parseContext.getBytes(6);
        int data_reference_index = parseContext.readUnsignedInt16();
        box.setDataReferenceIndex(data_reference_index);
    }
}
