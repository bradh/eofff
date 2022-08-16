package net.frogmouth.rnd.eofff.isobmff.stts;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeToSampleBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(TimeToSampleBoxParser.class);

    public TimeToSampleBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("stts");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TimeToSampleBox box = new TimeToSampleBox(boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long itemCount = parseContext.readUnsignedInt32();
        for (long i = 0; i < itemCount; i++) {
            TimeToSampleEntry entry = parseTimeToSampleEntry(parseContext);
            box.addEntry(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }

    private TimeToSampleEntry parseTimeToSampleEntry(ParseContext parseContext) {
        long sampleCount = parseContext.readUnsignedInt32();
        long sampleDelta = parseContext.readUnsignedInt32();
        return new TimeToSampleEntry(sampleCount, sampleDelta);
    }
}
