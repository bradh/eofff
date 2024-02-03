package net.frogmouth.rnd.eofff.mpeg4.mp4a;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.AudioSampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.AudioSampleEntryParser;

public class MP4AudioSampleEntryParser extends AudioSampleEntryParser {

    public MP4AudioSampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return MP4AudioSampleEntry.MP4A_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        long limit = boxSize + initialOffset;
        AudioSampleEntry box = new MP4AudioSampleEntry();
        return parse(parseContext, limit, box);
    }
}
