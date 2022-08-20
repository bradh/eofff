package net.frogmouth.rnd.eofff.isobmff.stsd;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleDescriptionBoxParser extends FullBoxParser {

    private static final Logger LOG = LoggerFactory.getLogger(SampleDescriptionBoxParser.class);

    public SampleDescriptionBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("stsd");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SampleDescriptionBox box = new SampleDescriptionBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long entryCount = parseContext.readUnsignedInt32();
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return version == 0x00;
    }
}
