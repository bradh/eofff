package net.frogmouth.rnd.eofff.isobmff.stsz;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleSizeBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(SampleSizeBoxParser.class);

    public SampleSizeBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("stsz");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SampleSizeBox box = new SampleSizeBox(boxSize, boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setSampleSize(parseContext.readUnsignedInt32());
        long sampleCount = parseContext.readUnsignedInt32();
        for (long i = 0; i < sampleCount; i++) {
            Long entry = parseContext.readUnsignedInt32();
            box.addEntry(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
