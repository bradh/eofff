package net.frogmouth.rnd.eofff.isobmff.enca;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.AudioSampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.AudioSampleEntryParser;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser.class)
public class EncryptedAudioSampleEntryParser extends AudioSampleEntryParser
        implements SampleEntryParser {

    public EncryptedAudioSampleEntryParser() {}

    @Override
    public FourCC getFourCC() {
        return EncryptedAudioSampleEntry.ENCA_ATOM;
    }

    @Override
    public SampleEntry parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        long limit = boxSize + initialOffset;
        AudioSampleEntry box = new EncryptedAudioSampleEntry();
        return parse(parseContext, limit, box);
    }
}
