package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class AudioSampleEntryParser extends BaseSampleEntryParser {

    public AudioSampleEntryParser() {}

    protected Box parse(ParseContext parseContext, long limit, AudioSampleEntry box) {
        parseBaseSampleEntry(parseContext, box);
        parseContext.skipBytes(2 * Integer.BYTES); // reserved
        box.setChannelCount(parseContext.readUnsignedInt16());
        box.setSampleSize(parseContext.readUnsignedInt16());
        parseContext.skipBytes(Short.BYTES); // pre_defined
        parseContext.skipBytes(Short.BYTES); // reserved
        box.setSampleRate(parseContext.readUnsignedInt32());
        box.addNestedBoxes(parseContext.parseNestedBoxes(limit));
        return box;
    }
}