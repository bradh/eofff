package net.frogmouth.rnd.eofff.isobmff.stsc;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleToChunkBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(SampleToChunkBoxParser.class);

    public SampleToChunkBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return SampleToChunkBox.STSC_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SampleToChunkBox box = new SampleToChunkBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long entryCount = parseContext.readUnsignedInt32();
        for (long i = 0; i < entryCount; i++) {
            SampleToChunkEntry entry = parseSampleToChunkEntry(parseContext);
            box.addEntry(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }

    private SampleToChunkEntry parseSampleToChunkEntry(ParseContext parseContext) {
        long firstChunk = parseContext.readUnsignedInt32();
        long samplesPerChunk = parseContext.readUnsignedInt32();
        long sampleDescriptionIndex = parseContext.readUnsignedInt32();
        return new SampleToChunkEntry(firstChunk, samplesPerChunk, sampleDescriptionIndex);
    }
}
