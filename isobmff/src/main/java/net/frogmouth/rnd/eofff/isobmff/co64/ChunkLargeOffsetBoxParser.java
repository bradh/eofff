package net.frogmouth.rnd.eofff.isobmff.co64;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.stco.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChunkLargeOffsetBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(ChunkLargeOffsetBoxParser.class);

    public ChunkLargeOffsetBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ChunkLargeOffsetBox.CO64_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ChunkLargeOffsetBox box = new ChunkLargeOffsetBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long entryCount = parseContext.readUnsignedInt32();
        for (long i = 0; i < entryCount; i++) {
            Long entry = parseContext.readUnsignedInt64();
            box.addEntry(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
