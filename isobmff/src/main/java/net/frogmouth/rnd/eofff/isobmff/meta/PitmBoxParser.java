package net.frogmouth.rnd.eofff.isobmff.meta;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PitmBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(PitmBoxParser.class);

    public PitmBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("pitm");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        PitmBox box = new PitmBox(boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        if (version == 0) {
            box.setItemID(parseContext.readUnsignedInt16());
        } else {
            box.setItemID(parseContext.readUnsignedInt32());
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
