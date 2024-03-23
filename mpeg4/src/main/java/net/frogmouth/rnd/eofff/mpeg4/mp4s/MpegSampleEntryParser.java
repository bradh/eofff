package net.frogmouth.rnd.eofff.mpeg4.mp4s;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.BaseSampleEntryParser;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser.class)
public class MpegSampleEntryParser extends BaseSampleEntryParser implements SampleEntryParser {

    public MpegSampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return MpegSampleEntry.MP4S_ATOM;
    }

    @Override
    public SampleEntry parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        long limit = boxSize + initialOffset;
        MpegSampleEntry box = new MpegSampleEntry();
        this.parseBaseSampleEntry(parseContext, box);
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
