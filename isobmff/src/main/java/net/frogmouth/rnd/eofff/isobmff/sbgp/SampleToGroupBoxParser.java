package net.frogmouth.rnd.eofff.isobmff.sbgp;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.stsc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleToGroupBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(SampleToGroupBoxParser.class);

    public SampleToGroupBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("sbgp");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SampleToGroupBox box = new SampleToGroupBox(boxSize, boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setGroupingType(parseContext.readUnsignedInt32());
        if (version == 0x01) {
            box.setGroupingTypeParameter(parseContext.readUnsignedInt32());
        }
        long entryCount = parseContext.readUnsignedInt32();
        for (long i = 0; i < entryCount; i++) {
            SampleToGroupBoxEntry entry = parseSampleToGroupBoxEntry(parseContext);
            box.addEntry(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }

    private SampleToGroupBoxEntry parseSampleToGroupBoxEntry(ParseContext parseContext) {
        long sampleCount = parseContext.readUnsignedInt32();
        long groupDescriptionIndex = parseContext.readUnsignedInt32();
        return new SampleToGroupBoxEntry(sampleCount, groupDescriptionIndex);
    }
}
