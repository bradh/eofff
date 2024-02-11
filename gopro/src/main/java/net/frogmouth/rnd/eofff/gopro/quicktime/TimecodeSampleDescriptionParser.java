package net.frogmouth.rnd.eofff.gopro.quicktime;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.BaseSampleEntryParser;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser.class)
public class TimecodeSampleDescriptionParser extends BaseSampleEntryParser
        implements SampleEntryParser {

    public TimecodeSampleDescriptionParser() {}

    @Override
    public FourCC getFourCC() {
        return TimecodeSampleDescription.TMCD_ATOM;
    }

    @Override
    public SampleEntry parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TimecodeSampleDescription box = new TimecodeSampleDescription();
        parseBaseSampleEntry(parseContext, box);
        long res = parseContext.readUnsignedInt32(); // reserved
        box.setTimecodeFlags(parseContext.readUnsignedInt32());
        box.setTimeScale(parseContext.readUnsignedInt32());
        box.setFrameDuration(parseContext.readUnsignedInt32());
        box.setNumberOfFrames(parseContext.readUnsignedInt8());
        parseContext.skipBytes(1); // Reserved
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
