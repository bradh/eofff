package net.frogmouth.rnd.eofff.isobmff.stss;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncSampleBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(SyncSampleBoxParser.class);

    public SyncSampleBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return SyncSampleBox.STSS_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SyncSampleBox box = new SyncSampleBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long entryCount = parseContext.readUnsignedInt32();
        for (long i = 0; i < entryCount; i++) {
            Long sampleNumber = parseContext.readUnsignedInt32();
            box.addEntry(sampleNumber);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
