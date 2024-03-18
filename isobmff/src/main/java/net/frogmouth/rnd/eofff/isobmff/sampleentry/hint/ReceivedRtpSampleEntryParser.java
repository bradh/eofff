package net.frogmouth.rnd.eofff.isobmff.sampleentry.hint;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser.class)
public class ReceivedRtpSampleEntryParser extends HintSampleEntryParser
        implements SampleEntryParser {

    public ReceivedRtpSampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return ReceivedRtpHintSampleEntry.RRTP_ATOM;
    }

    @Override
    public SampleEntry parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ReceivedRtpHintSampleEntry box = new ReceivedRtpHintSampleEntry();
        parseBaseSampleEntry(parseContext, box);
        box.setHintTrackVersion(parseContext.readUnsignedInt16());
        box.setHighestCompatibleVersion(parseContext.readUnsignedInt16());
        box.setMaxPacketSize(parseContext.readUnsignedInt32());
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
