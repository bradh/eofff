package net.frogmouth.rnd.eofff.mpeg4.mp4a;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.AudioSampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.AudioSampleEntryParser;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser.class)
public class MP4AudioSampleEntryParser extends AudioSampleEntryParser implements SampleEntryParser {

    public MP4AudioSampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return MP4AudioSampleEntry.MP4A_ATOM;
    }

    @Override
    public SampleEntry parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        long limit = boxSize + initialOffset;
        AudioSampleEntry box = new MP4AudioSampleEntry();
        return parse(parseContext, limit, box);
    }
}
