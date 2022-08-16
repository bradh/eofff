package net.frogmouth.rnd.eofff.isobmff.ctts;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompositionOffsetBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(CompositionOffsetBoxParser.class);

    public CompositionOffsetBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("ctts");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        CompositionOffsetBox box = new CompositionOffsetBox(boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long itemCount = parseContext.readUnsignedInt32();
        for (long i = 0; i < itemCount; i++) {
            CompositionOffsetBoxEntry entry = parseCompositionOffsetBoxEntry(parseContext, version);
            box.addEntry(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }

    private CompositionOffsetBoxEntry parseCompositionOffsetBoxEntry(
            ParseContext parseContext, int version) {
        if (version == 0) {
            long sampleCount = parseContext.readUnsignedInt32();
            long sampleOffset = parseContext.readUnsignedInt32();
            return new CompositionOffsetBoxEntry(sampleCount, sampleOffset);
        } else {
            long sampleCount = parseContext.readUnsignedInt32();
            int sampleOffset = parseContext.readInt32();
            return new CompositionOffsetBoxEntry(sampleCount, sampleOffset);
        }
    }
}
